/*
 * $Id: ForwarderImpl.java 953 2005-11-04 15:10:56 +0100 (Fri, 04 Nov 2005) sim $
 *
 * ace - a collaborative editor
 * Copyright (C) 2005 Mark Bigler, Simon Raess, Lukas Zbinden
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package ch.iserver.ace.collaboration.jupiter.server;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 * A Forwarder implementation that forwards requests to several forwarders.
 * This implementation has a default forwarder, which is set as constructor
 * argument. The default forwarder is always passed the forwarding events
 * first. If it fails processing the event, the event is not forwarded
 * to the other forwarders in this composite forwarder. The collaboration
 * layer sets as default forwarder the 
 * {@link ch.iserver.ace.collaboration.jupiter.server.DocumentUpdater},
 * which is responsible to update the document. Further the participant
 * that caused the failure is excluded from the session through
 * a {@link ch.iserver.ace.collaboration.jupiter.server.FailureHandler}.
 */
class CompositeForwarderImpl implements CompositeForwarder {
	
	/**
	 * Logger instance used by this class.
	 */
	private static final Logger LOG = Logger.getLogger(CompositeForwarderImpl.class);
	
	/**
	 * List of forwarders.
	 */
	private final List forwarders;
	
	/**
	 * The set of participants that are currently in the session.
	 */
	private final Set participants;
	
	/**
	 * The default forwarder of this object.
	 */
	private final Forwarder forwarder;
	
	/**
	 * The failure handler of the session.
	 */
	private final FailureHandler failureHandler;
	
	/**
	 * Creates a new CompositeForwarder instance.
	 * 
	 * @param forwarder the default forwarder
	 */
	public CompositeForwarderImpl(Forwarder forwarder, FailureHandler failureHandler) {
		ParameterValidator.notNull("forwarder", forwarder);
		this.forwarders = new LinkedList();
		this.participants = new HashSet();
		this.participants.add(new Integer(0));
		this.forwarder = forwarder;
		this.failureHandler = failureHandler;
	}
	
	/**
	 * @return the FailureHandler of the session
	 */
	protected FailureHandler getFailureHandler() {
		return failureHandler;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.CompositeForwarder#addForwarder(ch.iserver.ace.collaboration.jupiter.server.Forwarder)
	 */
	public void addForwarder(Forwarder forwarder) {
		if (forwarder != null) {
			forwarders.add(forwarder);
		}
	}
		
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.CompositeForwarder#removeForwarder(ch.iserver.ace.collaboration.jupiter.server.Forwarder)
	 */
	public void removeForwarder(Forwarder forwarder) {
		if (forwarder != null) {
			forwarders.remove(forwarder);
		}
	}
		
	/**
	 * @return gets an iterator over all the forwarders
	 */
	public Iterator getForwarders() {
		return forwarders.iterator();
	}
		
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#sendCaretUpdate(int, ch.iserver.ace.CaretUpdate)
	 */
	public void sendCaretUpdate(int participantId, CaretUpdate update) {
		if (!acceptParticipant(participantId)) {
			LOG.info("filtering caret update from participant " + participantId);
			return;
		}
		
		try {
			forwarder.sendCaretUpdate(participantId, update);
		} catch (Exception e) {
			LOG.warn("forwarding caret update to main forwarder failed: " + e);
			getFailureHandler().handleFailure(participantId, Participant.DOCUMENT_MODIFICATION_FAILED);
			return;
		}

		Iterator it = getForwarders();
		while (it.hasNext()) {
			Forwarder forwarder = (Forwarder) it.next();
			forwarder.sendCaretUpdate(participantId, update);
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#sendOperation(int, ch.iserver.ace.algorithm.Operation)
	 */
	public void sendOperation(int participantId, Operation op) {
		if (!acceptParticipant(participantId)) {
			LOG.info("filtering operation from participant " + participantId);
			return;
		}
		
		try {
			forwarder.sendOperation(participantId, op);
		} catch (Exception e) {
			LOG.warn("forwarding operation to main forwarder failed: " + e);
			getFailureHandler().handleFailure(participantId, Participant.DOCUMENT_MODIFICATION_FAILED);
			return;
		}
		
		Iterator it = getForwarders();
		while (it.hasNext()) {
			Forwarder forwarder = (Forwarder) it.next();
			forwarder.sendOperation(participantId, op);			
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#sendParticipantLeft(int, int)
	 */
	public void sendParticipantLeft(int participantId, int reason) {
		removeParticipant(participantId);
		forwarder.sendParticipantLeft(participantId, reason);
		Iterator it = getForwarders();
		while (it.hasNext()) {
			Forwarder forwarder = (Forwarder) it.next();
			forwarder.sendParticipantLeft(participantId, reason);
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#sendParticipantJoined(int, ch.iserver.ace.net.RemoteUserProxy)
	 */
	public void sendParticipantJoined(int participantId, RemoteUserProxy user) {
		addParticipant(participantId);
		forwarder.sendParticipantJoined(participantId, user);
		Iterator it = getForwarders();
		while (it.hasNext()) {
			Forwarder forwarder = (Forwarder) it.next();
			forwarder.sendParticipantJoined(participantId, user);
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#close()
	 */
	public void close() {
		forwarder.close();
		Iterator it = getForwarders();
		while (it.hasNext()) {
			Forwarder forwarder = (Forwarder) it.next();
			forwarder.close();
			it.remove();
		}
	}
	
	/**
	 * Adds a participant to the set of current participants.
	 * 
	 * @param participantId the id of the participant
	 */
	protected void addParticipant(int participantId) {
		LOG.debug("adding participant " + participantId);
		participants.add(new Integer(participantId));
	}
	
	/**
	 * Removes a participant from the set of current participants.
	 * 
	 * @param participantId the id of the participant
	 */
	protected void removeParticipant(int participantId) {
		LOG.debug("remove participant " + participantId);
		participants.remove(new Integer(participantId));
	}
	
	/**
	 * Checks whether messages from the given participant should be accepted.
	 * Accepted messages are then forwarder to all the child forwarders
	 * and the default forwarder.
	 * 
	 * @param participantId the id of the participant
	 * @return true iff messages from that participant are allowed
	 */
	protected boolean acceptParticipant(int participantId) {
		return participants.contains(new Integer(participantId));
	}
	
}

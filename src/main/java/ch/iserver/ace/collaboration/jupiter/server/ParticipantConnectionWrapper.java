/*
 * $Id: ParticipantConnectionWrapper.java 2840 2006-03-28 17:35:57Z sim $
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

import org.jboss.logging.Logger;

import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.net.ParticipantConnection;
import ch.iserver.ace.net.ParticipantPort;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 * Fail-safe wrapper for ParticipantConnection objects. ParticipantConnections
 * can throw exceptions, most likely related to network failures. At the moment
 * of failure, several commands might be enqueued for that particular
 * participant connection. Further, other participants must be informed that
 * the failing participant is no longer part of the session.
 * 
 * <p>This wrapper introduces an indirection that greatly simplifies the
 * handling of failing participant connections. If the connection throws
 * an exception, the wrapper replaces the failing connection with a
 * null object implementation. The enqueue commands to be sent to
 * that connection are simply discarded. There is no need for complex
 * handling of this situation.</p>
 */
public class ParticipantConnectionWrapper implements ParticipantConnection {
	
	/**
	 * The logger instance used by this class.
	 */
	protected static final Logger LOG = Logger.getLogger(ParticipantConnectionWrapper.class);
	
	/**
	 * The target ParticipantConnection. Initially this is the real target
	 * connection. If the connection fails, a null object is set as
	 * target.
	 */
	private ParticipantConnection target;
	
	/**
	 * The participant id of the connection.
	 */
	private int participantId;
	
	/**
	 * The FailureHandler that is notified if the connection fails.
	 */
	private final FailureHandler handler;
	
	/**
	 * Creates a new ParticipantConnectionWrapper that wraps the given 
	 * <var>target</var> ParticipantConnection and notifies the passed in
	 * <var>FailureHandler</var> about the failing connection.
	 * 
	 * @param target the target ParticipantConnection to be wrapped
	 * @param handler the FailureHandler to be notified about failures
	 */
	public ParticipantConnectionWrapper(ParticipantConnection target, FailureHandler handler) {
		ParameterValidator.notNull("target", target);
		this.target = target;
		this.handler = handler;
	}
	
	/**
	 * Gets the target ParticipantConnection. Initially this is the connection
	 * set in the constructor of this class. If the connection fails, it is
	 * replaced with a null object connection.
	 * 
	 * @return the target connection
	 */
	protected ParticipantConnection getTarget() {
		return target;
	}
	
	/**
	 * Gets the FailureHandler that is notified about a failing target
	 * connection.
	 * 
	 * @return the FailureHandler to be notified about a failure
	 */
	protected FailureHandler getFailureHandler() {
		return handler;
	}
	
	/**
	 * Handles a failure on the target connection.
	 * 
	 * <ol>
	 *   <li>notify the FailureHandler about the failing connection</li>
	 *   <li>replace the target with a null object connection</li>
	 * </ol>
	 */
	protected void failed(Exception e) {
		getFailureHandler().handleFailure(participantId, Participant.DISCONNECTED);
		target = createNullConnection();
	}
	
	/**
	 * Creates a null object connection.
	 * 
	 * @return the newly created null connection
	 */
	protected ParticipantConnection createNullConnection() {
		return new NullParticipantConnection();
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#setParticipantId(int)
	 */
	public void setParticipantId(int participantId) {
		this.participantId = participantId;
		getTarget().setParticipantId(participantId);
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#joinAccepted(ch.iserver.ace.net.ParticipantPort)
	 */
	public void joinAccepted(ParticipantPort port) {
		getTarget().joinAccepted(port);
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#joinRejected(int)
	 */
	public void joinRejected(int code) {
		getTarget().joinRejected(code);
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#getUser()
	 */
	public RemoteUserProxy getUser() {
		return getTarget().getUser();
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendDocument(ch.iserver.ace.net.PortableDocument)
	 */
	public void sendDocument(PortableDocument document) {
		try {
			getTarget().sendDocument(document);
		} catch (Exception e) {
			failed(e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendRequest(int, ch.iserver.ace.algorithm.Request)
	 */
	public void sendRequest(int participantId, Request request) {
		try {
			LOG.debug("sendRequest: " + participantId + " " + request);
			getTarget().sendRequest(participantId, request);
		} catch (Exception e) {
			failed(e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendCaretUpdateMessage(int, ch.iserver.ace.algorithm.CaretUpdateMessage)
	 */
	public void sendCaretUpdateMessage(int participantId, CaretUpdateMessage message) {
		try {
			LOG.debug("sendCaretUpdate: " + participantId + " " + message);
			getTarget().sendCaretUpdateMessage(participantId, message);
		} catch (Exception e) {
			failed(e);
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendAcknowledge(int, ch.iserver.ace.algorithm.Timestamp)
	 */
	public void sendAcknowledge(int siteId, Timestamp timestamp) {
		try {
			LOG.debug("sendRequest: ack(" + siteId + "," + timestamp + ")");
			getTarget().sendAcknowledge(siteId, timestamp);
		} catch (Exception e) {
			failed(e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendParticipantJoined(int, ch.iserver.ace.net.RemoteUserProxy)
	 */
	public void sendParticipantJoined(int participantId, RemoteUserProxy proxy) {
		try {
			getTarget().sendParticipantJoined(participantId, proxy);
		} catch (Exception e) {
			failed(e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendParticipantLeft(int, int)
	 */
	public void sendParticipantLeft(int participantId, int reason) {
		try {
			LOG.debug("sendParticipantLeft: " + participantId);
			getTarget().sendParticipantLeft(participantId, reason);
		} catch (Exception e) {
			failed(e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendKicked()
	 */
	public void sendKicked() {
		try {
			getTarget().sendKicked();
		} catch (Exception e) {
			failed(e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#close()
	 */
	public void close() {
		getTarget().close();
	}
	
}

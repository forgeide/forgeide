/*
 * $Id$
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;

import ch.iserver.ace.net.ParticipantConnection;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 * The default implementation of the ParticipantManager interface. This 
 * implementation keeps a number of collections to keep track of the state
 * of different users.
 * 
 * <h3>Join Set</h3>
 * Whenever a user sends a join requests, he is added to the join set of
 * this class. Users are removed from this set when either 
 * {@link #joinRequestRejected(String)} or
 * {@link #joinRequestAccepted(String)} is
 * called.
 * 
 * <h3>Blacklist</h3>
 * The blacklist keeps track of all the users that have been kicked from
 * the session. Blacklisted users cannot join the document again, unless
 * they are invited. To add a user to the blacklist, simply call
 * {@link #participantKicked(int)}.
 * 
 * <h3>Current Participants</h3>
 * There is a map that contains a mapping from participant id to user
 * id. Users are added to that map as soon as they join with
 * {@link #addParticipant(int, Forwarder, ParticipantConnection)}
 * and are removed from that map if they are either kicked or
 * leave the session otherwise
 * ({@link #participantLeft(int)}). 
 */
class ParticipantManagerImpl implements ParticipantManager {
	
	/**
	 * The logger of the participant manager.
	 */
	private static final Logger LOG = Logger.getLogger(ParticipantManager.class);
	
	/**
	 * The number of mappings from user id to participant id that are kept in
	 * the system.
	 */
	private static final int HISTORY_SIZE = 1000;
	
	/**
	 * The mapping from user id to participant id.
	 */
	private final Map userParticipantMapping = new HashMap();
	
	/**
	 * The current participant id. The next unknown user joining the
	 * session gets the id <code>currentParticipantId + 1</code>.
	 */
	private int currentParticipantId;
	
	/**
	 * The mapping from participant id to Forwarder object.
	 */
	private final Map forwarders = new HashMap();
	
	/**
	 * The mapping from participant id to ParticipantConnection.
	 */
	private final Map connections = new HashMap();
	
	/**
	 * The mapping from participant id to user id. 
	 */
	private final Map participants = new HashMap();
	
	/**
	 * The set of blacklisted users. Blacklisted users are no longer allowed
	 * to join the session.
	 */
	private final Set blacklist = new HashSet();
	
	/**
	 * The set of invited users. Invited users can join the session without
	 * prior callback to the application.
	 */
	private final Set invited = new HashSet();
	
	/**
	 * The set of currently joining users. If a join request is already in
	 * progress, a join should be rejected.
	 */
	private final Set joinSet = new HashSet();
	
	/**
	 * The CompositeForwarder which is used to manage forwarders.
	 */
	private CompositeForwarder compositeForwarder;
	
	/**
	 * Creates a new ParticipantManagerImpl instance that uses the given
	 * CompositeForwarder to manager forwarders.
	 * 
	 * @param forwarder the composite forwarder
	 */
	ParticipantManagerImpl(CompositeForwarder forwarder) {
		ParameterValidator.notNull("forwarder", forwarder);
		this.compositeForwarder = forwarder;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#getParticipantId(java.lang.String)
	 */
	public int getParticipantId(String userId) {
		Integer id = (Integer) userParticipantMapping.get(userId);
		if (id == null) {
			id = new Integer(nextParticipantId());
			userParticipantMapping.put(userId, id);
		}
		return id.intValue();
	}
	
	/**
	 * @return the next available participant id
	 */
	private synchronized int nextParticipantId() {
		return ++currentParticipantId;
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#isBlackListed(java.lang.String)
	 */
	public boolean isBlackListed(String userId) {
		return blacklist.contains(userId);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#isJoining(java.lang.String)
	 */
	public boolean isJoining(String userId) {
		return joinSet.contains(userId);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#isInvited(java.lang.String)
	 */
	public boolean isInvited(String userId) {
		return invited.contains(userId);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#isParticipant(java.lang.String)
	 */
	public boolean isParticipant(String userId) {
		return participants.containsValue(userId);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#addParticipant(int, ch.iserver.ace.collaboration.jupiter.server.Forwarder, ch.iserver.ace.net.ParticipantConnection)
	 */
	public void addParticipant(int participantId, Forwarder forwarder,
					ParticipantConnection connection) {
		LOG.info("addParticipant: " + participantId);
		Integer key = new Integer(participantId);
		RemoteUserProxy user = connection.getUser();
		if (user != null) {
			participants.put(new Integer(participantId), user.getId());
		} else {
			participants.put(new Integer(participantId), null);
		}
		forwarders.put(key, forwarder);
		connections.put(key, connection);
		compositeForwarder.addForwarder(forwarder);
	}

	/**
	 * Remove the participant with the given id from the manager.
	 * 
	 * @param participantId the participant to be removed
	 */
	protected void removeParticipant(int participantId) {
		LOG.info("removeParticipant: " + participantId);
		Integer key = new Integer(participantId);
		ParticipantConnection connection = getConnection(participantId);
		if (connection != null) {
			participants.remove(new Integer(participantId));
		}
		Forwarder removed = (Forwarder) forwarders.remove(key);
		compositeForwarder.removeForwarder(removed);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#participantKicked(int)
	 */
	public void participantKicked(int participantId) {
		LOG.info("participantKicked: " + participantId);
		String user = getUser(participantId);
		if (user != null) {
			blacklist.add(user);
		} else {
			LOG.warn("  user id is null");
		}
		removeParticipant(participantId);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#participantLeft(int)
	 */
	public void participantLeft(int participantId) {
		LOG.info("participantLeft: " + participantId);
		removeParticipant(participantId);
	}
	
	/**
	 * Gets the user id of the given participant. Note, if there is no
	 * participant with the given id in the session, null is returned.
	 * 
	 * @param participantId the participant id
	 * @return the user id of that particular participant
	 */
	private String getUser(int participantId) {
		return (String) participants.get(new Integer(participantId));
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#userInvited(String)
	 */
	public void userInvited(String userId) {
		ParameterValidator.notNull("userId", userId);
		blacklist.remove(userId);
		invited.add(userId);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#invitationAccepted(java.lang.String)
	 */
	public void invitationAccepted(String userId) {
		invited.remove(userId);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#invitationRejected(java.lang.String)
	 */
	public void invitationRejected(String userId) {
		invited.remove(userId);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#joinRequested(java.lang.String)
	 */
	public void joinRequested(String userId) {
		ParameterValidator.notNull("userId", userId);
		joinSet.add(userId);
	}

	public void joinRequestAccepted(String userId) {		
		LOG.info("joinRequestAccepted: " + userId);
		joinSet.remove(userId);
		invited.remove(userId);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#joinRequestRejected(String)
	 */
	public void joinRequestRejected(String userId) {
		ParameterValidator.notNull("userId", userId);
		joinSet.remove(userId);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ParticipantManager#getConnection(int)
	 */
	public ParticipantConnection getConnection(int participantId) {
		return (ParticipantConnection) connections.get(new Integer(participantId));
	}

}

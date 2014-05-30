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

import ch.iserver.ace.net.ParticipantConnection;

/**
 * Class that manages a set of participants.
 */
public interface ParticipantManager {
	
	/**
	 * The participant id for the given user. It is up to the implementation to
	 * determine which participant id should be given to the user. A possible
	 * implementation would give the same user always the same participant id.
	 * 
	 * @param userId the id of the user
	 * @return the participant id for that particular user
	 */
	int getParticipantId(String userId);
	
	/**
	 * Determines whether the given user is on the blacklist and should
	 * therefore not be allowed to join.
	 * 
	 * @param userId the id of the user
	 * @return true iff the user is on the blacklist
	 */
	boolean isBlackListed(String userId);
	
	/**
	 * Determines whether the given user has already a join request that is
	 * in progress.
	 * 
	 * @param userId the id of the user
	 * @return true iff the user is already joining
	 */
	boolean isJoining(String userId);
	
	/**
	 * Determines whether the given user is already a participant in the
	 * session.
	 * 
	 * @param userId the id of the user
	 * @return true iff the user is already in the session
	 */
	boolean isParticipant(String userId);
	
	/**
	 * Determines whether the given user was invited by the publisher.
	 * 
	 * @param userId the id of the user
	 * @return true iff the user was invited
	 */
	boolean isInvited(String userId);
	
	/**
	 * Adds a participant to the session.
	 * 
	 * @param participantId the id of the participant
	 * @param forwarder the forwarder for the participant
	 * @param connection the connection to the participant
	 */
	void addParticipant(int participantId, Forwarder forwarder, ParticipantConnection connection);
		
	/**
	 * Notifies the participant manager that the given participant has been kicked.
	 * 
	 * @param participantId the participant that has been kicked
	 */
	void participantKicked(int participantId);
	
	/**
	 * Notifies the participant manager that the given participant has left the
	 * session.
	 * 
	 * @param participantId the participant that left the session
	 */
	void participantLeft(int participantId);
	
	/**
	 * Notifies the participant manager that the given user has been invited
	 * to this session.
	 * 
	 * @param userId the user id of the invited user
	 */
	void userInvited(String userId);
	
	/**
	 * Notifies the participant manager that the given user has rejected an
	 * invitation.
	 * 
	 * @param userId the user id of the user that rejected an invitation
	 */
	void invitationRejected(String userId);
	
	/**
	 * Notifies the participant manager that the given user has accepted an
	 * invitation.
	 * 
	 * @param userId the user id of the user that accepted an invitation
	 */
	void invitationAccepted(String userId);
	
	/**
	 * Notifies the participant manager that the user wants to join.
	 * 
	 * @param userId the user id of the joining user
	 */
	void joinRequested(String userId);
	
	/**
	 * Notifies the participant manager that the user's join request was
	 * accepted.
	 * 
	 * @param userId the user whose join request was accepted
	 */
	void joinRequestAccepted(String userId);
	
	/**
	 * Notifies the participant manager that the user's join request was
	 * rejected.
	 * 
	 * @param userId the user id the joining user
	 */
	void joinRequestRejected(String userId);
	
	/**
	 * Gets the participant connection for the given participant.
	 * 
	 * @param participantId the participant's id
	 * @return the participant connection for that participant or null
	 */
	ParticipantConnection getConnection(int participantId);
	
}

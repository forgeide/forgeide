/*
 * $Id: ServerLogic.java 2839 2006-03-27 17:51:32Z sim $
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

import ch.iserver.ace.DocumentDetails;
import ch.iserver.ace.collaboration.InvitationCallback;
import ch.iserver.ace.collaboration.RemoteUser;
import ch.iserver.ace.net.DocumentServerLogic;
import ch.iserver.ace.net.ParticipantConnection;

/**
 * Extended interface for internal use of the server logic.
 */
public interface ServerLogic extends DocumentServerLogic {
	
	/**
	 * The participant id of the publisher.
	 */
	int PUBLISHER_ID = 0;
		
	/**
	 * Sets the document details object for the session.
	 * 
	 * @param details the document details object
	 */
	void setDocumentDetails(DocumentDetails details);
	
	/**
	 * Notifies the server that the specified user left the editing session.
	 *
	 * @param participantId the participant id of the leaving user
	 */
	void leave(int participantId);

	/**
	 * Kicks the participant with the given id from the session.
	 * 
	 * @param participant the participant to be kicked
	 */
	void kick(int participant);
	
	/**
	 * Notifies the server that the specified user's join request
	 * was rejected.
	 * 
	 * @param connection the participant connection
	 */
	void joinRejected(ParticipantConnection connection);
	
	/**
	 * Notifies the server that the specified user's join request
	 * was accepted.
	 * 
	 * @param connection the participant connection
	 */
	void joinAccepted(ParticipantConnection connection);
	
	/**
	 * Invites the given user to the session.
	 * 
	 * @param user the user to be invited
	 * @param callback TODO
	 */
	void invite(RemoteUser user, InvitationCallback callback);
	
	/**
	 * Shuts the server logic down. The server logic takes care that the
	 * associated document server is shut down too. 
	 */
	void shutdown();
			
}

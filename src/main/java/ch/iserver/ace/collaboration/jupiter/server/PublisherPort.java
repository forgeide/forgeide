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

import ch.iserver.ace.DocumentDetails;
import ch.iserver.ace.collaboration.InvitationCallback;
import ch.iserver.ace.collaboration.RemoteUser;
import ch.iserver.ace.net.ParticipantPort;

/**
 * A PublisherPort extends the ParticipantPort interface with methods available
 * to the publisher of a document. The publisher has additional rights, which
 * are not available to normal participants.
 */
public interface PublisherPort extends ParticipantPort {
	
	/**
	 * Allows the publisher part of the Collaboration Layer to kick a
	 * participant from the session. Kicked users are no longer
	 * part of the session and do not receive messages anymore.
	 * 
	 * @param participantId the participant to kick
	 */
	void kick(int participantId);
	
	/**
	 * Invites the given user to the session.
	 * 
	 * @param user the user to be invited
	 * @param callback the callback to be notified about the outcome
	 */
	void invite(RemoteUser user, InvitationCallback callback);
	
	/**
	 * Sets the new document details for the session.
	 * 
	 * @param details the new document details
	 */
	void setDocumentDetails(DocumentDetails details);
	
}

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
import ch.iserver.ace.collaboration.jupiter.AlgorithmWrapper;

/**
 * Default implementation of the PublisherPort interface.
 */
public class PublisherPortImpl extends ParticipantPortImpl implements PublisherPort {
	
	/**
	 * Creates a new PublisherPortImpl instance.
	 * 
	 * @param logic the server logic
	 * @param participantId the participant id of this participant
	 * @param algorithm the algorithm used by the port
	 * @param forwarder
	 */
	public PublisherPortImpl(ServerLogic logic, FailureHandler handler, int participantId, AlgorithmWrapper algorithm, Forwarder forwarder) {
		super(logic, handler, participantId, algorithm, forwarder);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.PublisherPort#kick(int)
	 */
	public void kick(int participantId) {
		if (participantId != getParticipantId()) {
			getLogic().kick(participantId);
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.PublisherPort#invite(ch.iserver.ace.collaboration.RemoteUser, InvitationCallback)
	 */
	public void invite(RemoteUser user, InvitationCallback callback) {
		getLogic().invite(user, callback);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.PublisherPort#setDocumentDetails(ch.iserver.ace.DocumentDetails)
	 */
	public void setDocumentDetails(DocumentDetails details) {
		getLogic().setDocumentDetails(details);
	}
	
	/**
	 * Overrides the leave method to shutdown the session if the
	 * publisher leaves the session.
	 * 
	 * @see ch.iserver.ace.net.ParticipantPort#leave()
	 */
	public void leave() {
		getLogic().shutdown();
	}
	
}

/*
 * $Id: SessionConnection.java 2840 2006-03-28 17:35:57Z sim $
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

package ch.iserver.ace.net;

import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;

/**
 * A SessionConnection is a network layer interface abstracting the concept of
 * a connection to the session represented by this object. SessionConnections
 * are only used by participants and not by the owner of the session (a 
 * different mechanism is used for published sessions).
 */
public interface SessionConnection {
	
	/**
	 * Gets the participant id of the local user.
	 * 
	 * @return the participant id of the local user
	 */
	int getParticipantId();
		
	/**
	 * Leave the session. After calling this method, the session becomes
	 * stale, i.e. it should not be used anymore.
	 */
	void leave();
	
	/**
	 * Sends the given request to the session.
	 *
	 * @param request the request to be sent
	 */
	void sendRequest(Request request);
	
	/**
	 * Sends the given caret update to the session.
	 *
	 * @param message the CaretUpdateMessage to be sent
	 */
	void sendCaretUpdateMessage(CaretUpdateMessage message);
	
	/**
	 * Sends an acknowledge message to the other site.
	 * 
	 * @param siteId the local site id
	 * @param timestamp the local timestamp
	 */
	void sendAcknowledge(int siteId, Timestamp timestamp);
			
}

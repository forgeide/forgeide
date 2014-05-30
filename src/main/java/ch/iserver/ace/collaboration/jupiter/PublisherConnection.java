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

package ch.iserver.ace.collaboration.jupiter;

import ch.iserver.ace.collaboration.JoinRequest;
import ch.iserver.ace.net.ParticipantConnection;

/**
 * A ParticipantConnection to the publisher of the document. Adds a single
 * method used for abnormal termination of a published session caused
 * by an fatal exception. If the sessionFailed method is called, it is 
 * likely an indication of a bug in the implementation.
 */
public interface PublisherConnection extends ParticipantConnection {
	
	/**
	 * Called by the ServerLogic on the publisher connection when there was a
	 * failure in the server part of the published session. The server logic
	 * shuts itself down after calling this method. Thus, the published session
	 * is unusable after this method is called.
	 * 
	 * @param reason the failure code
	 * @param e the exception causing the failure
	 */
	void sessionFailed(int reason, Exception e);
	
	/**
	 * Passes the given join request to the publisher of the document.
	 * 
	 * @param request the join request
	 */
	void sendJoinRequest(JoinRequest request);
	
}

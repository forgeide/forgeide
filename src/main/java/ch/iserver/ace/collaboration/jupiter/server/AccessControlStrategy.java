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

import ch.iserver.ace.collaboration.JoinRequest;
import ch.iserver.ace.collaboration.jupiter.PublisherConnection;

/**
 * An AccessControlStrategy determines how to handle join requests. The
 * normal implementation is to ask the publisher connection for join
 * permission, however other schemes can be implemented.
 */
public interface AccessControlStrategy {
	
	/**
	 * Requests the strategy for permission to join the session. The request
	 * can either be accepted or rejected.
	 * 
	 * @param connection the connection to the publisher of the session
	 * @param request the join request, containing the user that wants to join
	 */
	void joinRequest(PublisherConnection connection, JoinRequest request);
	
}

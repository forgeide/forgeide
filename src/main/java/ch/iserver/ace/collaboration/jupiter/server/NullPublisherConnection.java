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

import org.jboss.logging.Logger;

import ch.iserver.ace.collaboration.JoinRequest;
import ch.iserver.ace.collaboration.jupiter.PublisherConnection;

/**
 * Null object implementation of the PublisherConnection interface.
 */
class NullPublisherConnection extends NullParticipantConnection implements PublisherConnection {
	
	/**
	 * The logger used by instances.
	 */
	private static final Logger LOG = Logger.getLogger(NullPublisherConnection.class);
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.PublisherConnection#sendJoinRequest(ch.iserver.ace.collaboration.JoinRequest)
	 */
	public void sendJoinRequest(JoinRequest request) {
		LOG.debug("sendJoinRequest ignored");
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.PublisherConnection#sessionFailed(int, java.lang.Exception)
	 */
	public void sessionFailed(int reason, Exception e) {
		LOG.debug("sessionFailed ignored");
	}
	
}

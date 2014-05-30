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

import ch.iserver.ace.collaboration.ParticipantSessionCallback;
import ch.iserver.ace.collaboration.Session;
import ch.iserver.ace.net.SessionConnection;
import ch.iserver.ace.net.SessionConnectionCallback;

/**
 * ConfigurableSession objects are returned from the session factory. They can
 * be configured, most specifically the connection and session callback can
 * be set.
 */
public interface ConfigurableSession extends Session, SessionConnectionCallback {
	
	/**
	 * Sets the session callback of this session.
	 * 
	 * @param callback the session callback
	 */
	void setSessionCallback(ParticipantSessionCallback callback);
	
	/**
	 * Sets the session connection of this session.
	 * 
	 * @param connection the session connection
	 */
	void setConnection(SessionConnection connection);
	
}

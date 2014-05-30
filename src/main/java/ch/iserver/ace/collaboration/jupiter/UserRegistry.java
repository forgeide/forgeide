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

import ch.iserver.ace.net.RemoteUserProxy;

/**
 * Registry class and factory for RemoteUser objects.
 */
public interface UserRegistry {
	
	/**
	 * Creates a new RemoteUser, if there is not already a RemoteUser for the
	 * passed in proxy. The passed in RemoteUserProxy is
	 * used to create the RemoteUser. This method ensures that there is only
	 * one RemoteUser object for a unique user.
	 * 
	 * @param proxy the proxy object from the network layer
	 * @return the newly created MutableRemoteUser
	 */
	MutableRemoteUser getUser(RemoteUserProxy proxy);
	
	/**
	 * Gets the user with the given id from the registry. If there is no
	 * such user, null is returned.
	 * 
	 * @param id the id of the remote user
	 * @return the MutableRemoteUser with the given id or null if there is none
	 */
	MutableRemoteUser getUser(String id);
	
	/**
	 * Removes the remote user specified by the given RemoteUserProxy from
	 * the registry.
	 * 
	 * @param proxy the proxy of the user to be removed
	 * @return the removed MutableRemoteUser or null if there is no such user
	 */
	MutableRemoteUser removeUser(RemoteUserProxy proxy);
	
}

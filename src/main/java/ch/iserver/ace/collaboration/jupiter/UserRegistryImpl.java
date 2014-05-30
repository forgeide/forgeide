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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.iserver.ace.net.RemoteUserProxy;

/**
 * The default implementation of a UserRegistry.
 */
public class UserRegistryImpl implements UserRegistry {
	
	/**
	 * The map from user id to MutableRemoteUser object.
	 */
	private final Map users;
	
	/**
	 * Creates a new UserRegistryImpl object.
	 */
	public UserRegistryImpl() {
		this.users = Collections.synchronizedMap(new HashMap());
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.UserRegistry#getUser(ch.iserver.ace.net.RemoteUserProxy)
	 */
	public synchronized MutableRemoteUser getUser(RemoteUserProxy proxy) {
		MutableRemoteUser user = getUser(proxy.getId());
		if (user == null) {
			user = new RemoteUserImpl(proxy);
			users.put(user.getId(), user);
		}
		return user;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.UserRegistry#getUser(java.lang.String)
	 */
	public synchronized MutableRemoteUser getUser(String id) {
		return (MutableRemoteUser) users.get(id);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.UserRegistry#removeUser(ch.iserver.ace.net.RemoteUserProxy)
	 */
	public synchronized MutableRemoteUser removeUser(RemoteUserProxy proxy) {
		return (MutableRemoteUser) users.remove(proxy.getId());
	}

}

/*
 * $Id: Discovery.java 2424 2005-12-09 17:30:11Z zbinl $
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
package ch.iserver.ace.net.core;

import ch.iserver.ace.UserDetails;

/**
 * Interface for the service discovery, i.e. the discovery of up and running
 * users of ACE. Provides the necessary methods to properly initialize, execute
 * and stop the discovery process.
 *
 * @see ch.iserver.ace.net.core.DiscoveryFactory
 */
public interface Discovery {
	
	/**
	 * Sets the UUID for the local user.
	 * 
	 * @param uuid the unique id for the local user
	 */
	void setUserId(String uuid);
	
	/**
	 * Sets the user details for the local user. Currently this includes only the
	 * user name.
	 * 
	 * @param details the UserDetails to be set
	 */
	void setUserDetails(UserDetails details);
	
	/**
	 * Executes the Bonjour zeroconf discovery process.
	 * First, the user is registered. Afterwards, the network
	 * is searched for other users.
	 * 
	 * @see DiscoveryCallback
	 */
	void execute();
	
	/**
	 * Stops the discovery process. To be called only once in the
	 * applications lifetime.
	 */
	void abort();
	
}

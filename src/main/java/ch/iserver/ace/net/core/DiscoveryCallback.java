/*
 * $Id:DiscoveryCallback.java 1205 2005-11-14 07:57:10Z zbinl $
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


/**
 * Discovery callback interface used by the discovery implementation to 
 * notify about events received from the network. E.g. the discovery
 * of a new user or the loss of a user.
 */
public interface DiscoveryCallback {

	/**
	 * Called when a new user has been discovered. At the time
	 * of this call, it is possible that not all essential information for this
	 * user could be gathered. Wait until {@link #userDiscoveryCompleted(RemoteUserProxyExt)}
	 * is called.
	 * 
	 * @param proxy the RemoteUserProxy for the discovered user
	 */
	void userDiscovered(RemoteUserProxyExt proxy);
	
	/**
	 * Called when a user has discarded. 
	 *  
	 * @param proxy the RemoteUserProxy of the discarded user
	 */
	void userDiscarded(RemoteUserProxyExt proxy);
	
	/**
	 * Called when the user discovery completed, i.e.
	 * when all information for a user has been gathered, 
	 * including its host address.
	 * 
	 * @param proxy the RemoteUserProxy for the user which is completely discovered
	 */
	void userDiscoveryCompleted(RemoteUserProxyExt proxy); 
	
	/**
	 * Called when the details of a user have changed. This is currently
	 * only the user's name.
	 * 
	 * @param proxy the RemoteUserProxy of the user whose details have changed
	 */
	void userDetailsChanged(RemoteUserProxyExt proxy);
	
}

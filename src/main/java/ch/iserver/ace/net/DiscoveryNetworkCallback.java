/*
 * $Id: DiscoveryNetworkCallback.java 2252 2005-12-06 15:47:30Z sim $
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

/**
 * Callback interface for explicit user discovery. The result
 * of the discovery is communicated to that interface.
 */
public interface DiscoveryNetworkCallback {

	/**
	 * Notifies the callback that the discovery failed.
	 * The reason code specifies why the discovery failed, the message
	 * provides additional details.
	 * 
	 * @param reason the reason of the failure
	 * @param message the detail message
	 */
	void userDiscoveryFailed(int reason, String message);
	
	/**
	 * Notifies the callback that the discovery succeeded. The result is
	 * passed through the standard
	 * {@link NetworkServiceCallback#userDiscovered(RemoteUserProxy)}
	 * method.
	 */
	void userDiscoverySucceeded();
	
}

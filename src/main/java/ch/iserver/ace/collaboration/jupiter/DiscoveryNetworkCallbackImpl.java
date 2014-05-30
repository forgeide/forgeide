/*
 * $Id: DiscoveryNetworkCallbackImpl.java 2216 2005-12-06 09:04:12Z sim $
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

import ch.iserver.ace.collaboration.DiscoveryCallback;
import ch.iserver.ace.collaboration.DiscoveryResult;
import ch.iserver.ace.net.DiscoveryNetworkCallback;
import ch.iserver.ace.util.ParameterValidator;

/**
 * Default implementation of the DiscoveryNetworkCallback interface. This interface
 * is used in the case of explicit discovery requests.
 */
class DiscoveryNetworkCallbackImpl implements DiscoveryNetworkCallback {
	
	/**
	 * The callback to be notified.
	 */
	private final DiscoveryCallback callback;
	
	/**
	 * Creates a new DiscoveryNetworkCallbackImpl interface. The passed in
	 * callback is notified when this callback is notified.
	 * 
	 * @param callback the callback to be notified
	 */
	DiscoveryNetworkCallbackImpl(DiscoveryCallback callback) {
		ParameterValidator.notNull("callback", callback);
		this.callback = callback;
	}
	
	/**
	 * @return the discovery callback to be notified
	 */
	private DiscoveryCallback getCallback() {
		return callback;
	}
		
	/**
	 * @see ch.iserver.ace.net.DiscoveryNetworkCallback#userDiscoveryFailed(int, java.lang.String)
	 */
	public void userDiscoveryFailed(int status, String message) {
		getCallback().discovered(new DiscoveryResult(status, message));
	}

	/**
	 * @see ch.iserver.ace.net.DiscoveryNetworkCallback#userDiscoverySucceeded()
	 */
	public void userDiscoverySucceeded() {
		getCallback().discovered(new DiscoveryResult(DiscoveryResult.SUCCESS));
	}

}

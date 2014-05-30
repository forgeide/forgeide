/*
 * $Id: DiscoveryCallback.java 998 2005-11-07 14:41:55Z sim $
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

package ch.iserver.ace.collaboration;

import java.net.InetAddress;

/**
 * Callback interface for explicit discovery requests through
 * {@link ch.iserver.ace.collaboration.CollaborationService#discoverUser(DiscoveryCallback, InetAddress, int)}.
 */
public interface DiscoveryCallback {
	
	/**
	 * Notifies the callback that the discovery has completed. The passed
	 * in DiscoveryResult contains the result of the discovery.
	 * 
	 * @param result the DiscoveryResult containing the result of discovery
	 */
	void discovered(DiscoveryResult result);
	
}
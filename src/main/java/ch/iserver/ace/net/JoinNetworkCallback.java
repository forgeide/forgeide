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

package ch.iserver.ace.net;

/**
 * Callback interface for join requests on the network layer.
 */
public interface JoinNetworkCallback {
	
	/**
	 * Notifies the callback that the join request was rejected by the owner
	 * of the document. The corresponding codes are defined in the
	 * interface {@link ch.iserver.ace.collaboration.JoinRequest}.
	 * 
	 * @param code the reason of rejection
	 */
	void rejected(int code);
	
	/**
	 * Notifies the callback that the join request was accepted by the owner
	 * of the document. The passed in SessionConnection can be used to
	 * communicate with the joined shared document.
	 * 
	 * @param connection the SessionConnection
	 */
	SessionConnectionCallback accepted(SessionConnection connection);
	
}

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

package ch.iserver.ace;

import java.net.InetAddress;

/**
 * Object containing information about a server.
 */
public class ServerInfo {
	
	/**
	 * The InetAddress of the server.
	 */
	private InetAddress address;
	
	/**
	 * The port used by that particular server.
	 */
	private int port;
	
	/**
	 * Creates a new ServerInfo instance.
	 * 
	 * @param addr the InetAddress of the server
	 * @param port the port of the server
	 */
	public ServerInfo(InetAddress addr, int port) {
		this.address = addr;
		this.port = port;
	}

	/**
	 * Gets the InetAddress of the represented server.
	 * 
	 * @return the InetAddress
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Gets the port on which the represented server is listening.
	 * 
	 * @return the local port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + address + ":" + port + "]";
	}
	
}

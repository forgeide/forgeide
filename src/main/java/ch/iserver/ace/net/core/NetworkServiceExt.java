/*
 * $Id:NetworkServiceExt.java 1205 2005-11-14 07:57:10Z zbinl $
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

import ch.iserver.ace.ServerInfo;
import ch.iserver.ace.algorithm.TimestampFactory;
import ch.iserver.ace.net.NetworkService;
import ch.iserver.ace.util.ThreadDomain;

/**
 * This interface extends interface {@link ch.iserver.ace.net.NetworkService}
 * for the network layer.
 */
public interface NetworkServiceExt extends NetworkService {

	/**
	 * Sets the discovery object. 
	 * 
	 * @param discovery the discovery object
	 * @see Discovery
	 */
	public void setDiscovery(Discovery discovery);
	
	/**
	 * Sets the server info. The server info includes the address
	 * and port information for the local user.
	 * 
	 * @param info the server info for the local user
	 * @see ServerInfo
	 */
	public void setServerInfo(ServerInfo info);
	
	/**
	 * Conceals a document from the local user. This method
	 * does not cause any physical network traffic and is used 
	 * for management purpose.
	 * 
	 * @param docId the id of the document to conceal
	 */
	public void conceal(String docId);
	
	/**
	 * Returns true if the local user has one or more published documents.
	 * 
	 * @return true iff the local user has one or more published documents
	 */
	public boolean hasPublishedDocuments();
	
	/**
	 * Gets the TimestampFactory.
	 * 
	 * @return the TimestampFactory
	 * @see TimestampFactory
	 */
	public TimestampFactory getTimestampFactory();
	
	/**
	 * Returns whether the network layer is stopped.
	 * If true, no messages from the physical network shall be 
	 * received and processed anymore.
	 * 
	 * @return true iff the network layer is stopped
	 */
	public boolean isStopped();
	
	/**
	 * Returns the main thread domain to be used by the discovery
	 * component as well as the main request handler.
	 * 
	 * @return the ThreadDomain to be used
	 */
	public ThreadDomain getMainThreadDomain();
}

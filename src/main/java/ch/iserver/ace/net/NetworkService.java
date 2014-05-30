/*
 * $Id: NetworkService.java 2833 2006-03-22 22:09:37Z sim $
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

import java.net.InetAddress;

import ch.iserver.ace.DocumentDetails;
import ch.iserver.ace.ServerInfo;
import ch.iserver.ace.UserDetails;
import ch.iserver.ace.algorithm.TimestampFactory;


/**
 * The NetworkService provides access to the network layer functionality.
 * It allows to publish documents as well as to register various listeners
 * for events from the network.
 * <p>Note: this service should not be accessed directly by the application.
 * The logic layer provides enhanced functionality on top of the network
 * layer and the application should only access that layer.</p>
 */
public interface NetworkService {
	
	/**
	 * Gets the local server info object. It may take some time to initialize
	 * this property. So it is the responsibility of the clients of this
	 * service to query the network service periodically until the property
	 * is set.
	 * 
	 * @return the local server info
	 */
	ServerInfo getServerInfo();
	
	/**
	 * @return
	 */
	RemoteUserProxy getLocalUser();
	
	/**
	 * Called by the collaboration layer to start the network layer.
	 */
	void start();
	
	/**
	 * Called by the collaboration layer to stop the network layer.
	 */
	void stop();
	
	/**
	 * Sets the user id of the local user. This method has to be called before
	 * starting the service with the {@link #start()} method. Note, it is
	 * an error to set the user id twice!
	 * 
	 * @param id the id of the local user
	 */
	void setUserId(String id);
	
	/**
	 * Sets the UserDetails for the local user.
	 * 
	 * @param details the UserDetails for the local user
	 */
	void setUserDetails(UserDetails details);
	
	/**
	 * Sets the TimestampFactory used by the network layer to create Timestamp
	 * objects.
	 * 
	 * @param factory the TimestampFactory to create Timestamps
	 */
	void setTimestampFactory(TimestampFactory factory);
	
	/**
	 * Sets the <var>callback</var> for events from the network layer.
	 * It further initiates the discovery process for remote users.
	 * 
	 * @param callback the callback object for events from the network layer
	 */
	void setCallback(NetworkServiceCallback callback);
	
	/**
	 * Publishes a document in the network layer. A published document must
	 * be reachable from the network. The passed in <var>logic</var> object
	 * is used by the network layer to access the document server functionality
	 * in the logic layer. The returned DocumentServer object will be used
	 * by the caller to control the created network layer server functionality.
	 * 
	 * @param logic the DocumentServerLogic providing access to logic layer
	 *              functionality
	 * @param details the document details
	 * @return a DocumentServer instance that allows the caller to control
	 *         the network layer server functionality
	 */
	DocumentServer publish(DocumentServerLogic logic, DocumentDetails details);
	
	/**
	 * Initiates an explicit discovery of a user. The network layer tries to 
	 * contact the given host on the given port. The result is communicated 
	 * to the DiscoveryNetworkCallback objects.
	 * 
	 * @param callback the DiscoveryNetworkCallback to be notified
	 * @param addr the target address
	 * @param port the target port
	 */	
	void discoverUser(DiscoveryNetworkCallback callback, InetAddress addr, int port);
	
}

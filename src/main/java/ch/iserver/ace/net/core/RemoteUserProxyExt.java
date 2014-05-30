/*
 * $Id: RemoteUserProxyExt.java 2841 2006-03-29 18:25:31Z zbinl $
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

import java.util.Map;

import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.net.protocol.DiscoveryException;

/**
 * Interface extension of <code>RemoteUserProxy</code>
 * for the network layer. It adds user specific functionality 
 * for document management, explicit user discovery.
 *
 * @see ch.iserver.ace.net.RemoteUserProxy
 */
public interface RemoteUserProxyExt extends RemoteUserProxy {

	/**
	 * Sets the mutable user details.
	 * 
	 * @param details the MutableUserDetails object
	 */
	void setMutableUserDetails(MutableUserDetails details);
	
	/**
	 * Gets the mutable user details.
	 * 
	 * @return MutableUserDetails
	 */
	MutableUserDetails getMutableUserDetails();
	
	/**
	 * Returns a synchronized map upon which to synchronize properly.
	 * The map contains document id to <code>RemoteDocumentProxyExt</code>
	 * mappings.
	 * 
	 * @return a map with all documents
	 */
	Map getDocuments();
	
	/**
	 * Adds a shared document to this user.
	 * 
	 * @param doc the shared document
	 */
	void addSharedDocument(RemoteDocumentProxyExt doc);
	
	/**
	 * Removes a shared document from this user.
	 * 
	 * @param id		the id of the document to remove
	 * @return		the document that was removed or null if no document could be removed
	 */
	RemoteDocumentProxyExt removeSharedDocument(String id);
	
	/**
	 * Gets a shared document.
	 * 
	 * @param id		the id of the document
	 * @return the shared document or null if none could be found
	 */
	RemoteDocumentProxyExt getSharedDocument(String id);
	
	/**
	 * Checks whether the document with <code>id</code> belongs to this
	 * user.
	 * 
	 * @param id 	the document id 
	 * @return	true iff the document id belongs to this user
	 */
	boolean hasDocumentShared(String id);
	
	/**
	 * Sets the session established flag.
	 * 
	 * @param value the value to set the session established flag
	 */
	void setSessionEstablished(boolean value);
	
	/**
	 * Checks whether the RemoteUserSession object is established for this user,
	 * i.e. a physical connection to this user exists.
	 * 
	 * @return true iff the session with this user is established
	 */ 
	boolean isSessionEstablished();
	
	/**
	 * Checks whether this user was discoverd by means of the automatic 
	 * discovery mechanism.
	 * If false is returned, then the user was discovered explicitly,
	 * i.e. either by participating in another session or by explicitly
	 * connecting to that user.
	 * 
	 * @return true iff this user was discovered by the automatic discovery mechanism.
	 */
	boolean isDNSSDdiscovered();
	
	/**
	 * Sets the DNSSDdiscovered value.
	 * 
	 * @param value 	the value to set
	 */
	void setDNSSDdiscovered(boolean value);
	
	/**
	 * Explicit discovery of this user. As precondition, 
	 * the address and the port need to be set. Tries
	 * to establish a <code>RemoteUserSession</code> with
	 * the given information and requests the users coordinates,
	 *  e.g. the user id and the user name.
	 *
	 * @throws DiscoveryException if the discovery fails
	 */
	void discover() throws DiscoveryException;
	
	/**
	 * Sets the id of this RemoteUserProxy.
	 * 
	 * @param id		the id to set
	 */
	void setId(String id);
}

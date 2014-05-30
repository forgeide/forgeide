/*
 * $Id: CollaborationService.java 2833 2006-03-22 22:09:37Z sim $
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

import ch.iserver.ace.DocumentModel;
import ch.iserver.ace.ServerInfo;
import ch.iserver.ace.UserDetails;

/**
 * The CollaborationService is the entry point for all collaboration operations.
 * There are methods related to registering listeners for incoming events
 * such as discoveries and invitations, and there are methods for publishing
 * local documents.
 * 
 * <p>The CollaborationService has to be started before any events are
 * published by it. If the CollaborationService is no longer used, it should
 * be stopped.</p>
 * 
 * <h3>Discovery Listeners</h3>
 * First, you have to register the listener and start the service.
 * <pre>
 *  CollaborationService service = ...;
 *  service.addDocumentListener(myDocumentListener);
 *  service.addUserListener(myUserListener);
 *  service.start();
 * </pre>
 * Then, you will get discovery events on those two listeners you have 
 * registered.
 * 
 * <h3>Callbacks</h3>
 * The CollaborationService supports two different callback objects. An
 * InvitationCallback can be set with the
 * {@link #setInvitationHandler(InvitationHandler)} method. Incoming
 * invitations are passed to that callback. The other callback is
 * notified about service failures. It can be set with the
 * {@link #setFailureHandler(ServiceFailureHandler)} method.
 * 
 * <h3>Publishing Documents</h3>
 * A local document can be published with the
 * {@link #publish(PublishedSessionCallback, DocumentModel)} method.
 * Published documents can then be joined by other users.
 */
public interface CollaborationService {
	
	/**
	 * Retrieves the ServerInfo of the local server. This information is mainly
	 * useful as feedback to the local user. If may not be available right
	 * after starting. If getting the information fails, the client should
	 * periodically poll the CollaborationService for the ServerInfo object.
	 * 
	 * @return the server info object
	 */
	ServerInfo getServerInfo();
	
	/**
	 * Starts the CollaborationService. No events are fired before this
	 * method is called.
	 */
	void start();
	
	/**
	 * Stops the CollaborationService. This method destroys the collaboration
	 * service. Do not call any methods after this method call.
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
	 * @param details the new UserDetails
	 */
	void setUserDetails(UserDetails details);
	
	/**
	 * Adds a user discovery listener to the list of registered listeners.
	 *
	 * @param l the listener to add
	 */
	void addUserListener(UserListener l);
	
	/**
	 * Removes a user discovery listener from the list of registered listeners.
	 *
	 * @param l the listener to remove
	 */
	void removeUserListener(UserListener l);
	
	/**
	 * Adds a document listener to the list of registered listeners.
	 * 
	 * @param l the listener to add
	 */
	void addDocumentListener(DocumentListener l);
	
	/**
	 * Removes a document discovery listener from the list of registered 
	 * listeners.
	 * 
	 * @param l the listener to remove
	 */
	void removeDocumentListener(DocumentListener l);
	
	/**
	 * Sets the InvitationCallback for the service. The InvitationCallback
	 * is responsible to handle all invitations sent to this user.
	 * 
	 * @param processor the InvitationCallback used by the service
	 */
	void setInvitationHandler(InvitationHandler processor);
	
	/**
	 * Sets the ServiceFailureHandler for the service. This handler gets
	 * notified about failures in the collaboration and network layers.
	 * 
	 * @param handler the new failure handler
	 */
	void setFailureHandler(ServiceFailureHandler handler);
	
	/**
	 * Publishes a document so that other users can join editing it
	 * over the network.
	 *
	 * @param callback the SessionCallback used for callbacks
	 * @param document the document to publish
	 * @return a session for the publisher itself
	 */
	PublishedSession publish(PublishedSessionCallback callback, DocumentModel document);
	
	/**
	 * Initiates an explicit discovery of a user. The network layer tries to 
	 * contact the given host on the given port. The result is communicated 
	 * to the DiscoveryCallback objects.
	 * 
	 * @param callback the DiscoveryCallback to be notified
	 * @param addr the target address
	 * @param port the target port
	 */	
	void discoverUser(DiscoveryCallback callback, InetAddress addr, int port);
	
}

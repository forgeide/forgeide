/*
 * $Id: RemoteDocumentProxyExt.java 2700 2006-01-11 11:27:08Z zbinl $
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

import ch.iserver.ace.DocumentDetails;
import ch.iserver.ace.net.JoinNetworkCallback;
import ch.iserver.ace.net.RemoteDocumentProxy;
import ch.iserver.ace.net.SessionConnection;
import ch.iserver.ace.net.SessionConnectionCallback;

/**
 * Interface extension of {@link ch.iserver.ace.net.RemoteDocumentProxy} for 
 * the network layer.
 * It adds methods concerning the join and invitation process of other users.
 */
public interface RemoteDocumentProxyExt extends RemoteDocumentProxy {

	/**
	 * Sets the document details.
	 * 
	 * @param details	the document details to set
	 * @see DocumentDetails
	 */
	public void setDocumentDetails(DocumentDetails details);
	
	/**
	 * Notifies the RemoteDocumentProxy that the join request was 
	 * rejected by the publisher.
	 * 
	 * @param code	the reason for the rejection
	 */
	public void joinRejected(int code);
	
	/**
	 * Notifies the RemoteDocumentProxy that the join request was 
	 * accepted by the publisher.
	 * 
	 * @param connection 				the SessionConnection for the local user to the publisher
	 * @param participantId2User		a map with participantId to RemoteUserProxy pairs
	 * @return the SessionConnectionCallback
	 * @see SessionConnectionCallback
	 */
	public SessionConnectionCallback joinAccepted(SessionConnection connection, Map participantId2User);
	
	/**
	 * Gets the RemoteUserProxyExt for a <code>participantId</code>.
	 * 
	 * @param participantId			the participantId of the user to get
	 * @return	RemoteUserProxyExt	the user for the participantId or null if not found
	 */
	public RemoteUserProxyExt getSessionParticipant(int participantId);
	
	/**
	 * Removes a session participant. 
	 * This method is only of use during a joined session for the remote
	 * document this <code>RemoteDocumentProxyExt</code> embodies.
	 * 
	 * @param participantId	the id of the session participant (<code>RemoteUserProxyExt</code>) to remove
	 */
	public void removeSessionParticipant(int participantId);
	
	/**
	 * Adds a user to this RemoteDocumentProxyExt's session.
	 * 
	 * @param participantId	the participantId of the user
	 * @param user			the RemoteUserProxyExt embodying the user
	 */
	public void addSessionParticipant(int participantId, RemoteUserProxyExt user);
	
	/**
	 * Gets the SessionConnectionCallback object. Returns null as long as a join request
	 * has not been accepted.
	 * 
	 * @return the SessionConnectionCallback object or null if {@link #joinAccepted(SessionConnection)} 
	 * 				was not called prior
	 */
	public SessionConnectionCallback getSessionConnectionCallback();
	
	/**
	 * Called when the local user accepted an invitation to join this document.
	 * 
	 * @param callback the JoinNetworkCallback instance
	 * @see JoinNetworkCallback
	 */
	public void invitationAccepted(JoinNetworkCallback callback);
	
	/**
	 * Cleans up this object. To be called after the local user
	 * left the session of this document. Otherwise this call
	 * has no impact.
	 */
	public void cleanupAfterLeave();
	
	/**
	 * Returns true if this document has been joined, false otherwise
	 * 
	 * @return truee iff this document has been joined by the local user
	 */
	public boolean isJoined();
	
}

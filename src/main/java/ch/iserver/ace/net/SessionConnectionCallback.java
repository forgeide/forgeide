/*
 * $Id: SessionConnectionCallback.java 2840 2006-03-28 17:35:57Z sim $
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

import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;

/**
 * The SessionConnectionCallback interface is used by the network layer to communicate
 * with the logic layer in session scope. The session controller itself will
 * process the received information and forward the relevant (transformed)
 * parts to the SessionCallback for consumption.
 * <p>The SessionConnectionCallback is the place were the operational transformation
 * aspects fit in. It is used in case of a joined session. For published
 * sessions the mechanisms are totally different.</p>
 * 
 * <h3>Thread Safety</h3>
 * The SessionConnectionCallback is not thread safe. Use proper synchronization
 * when accessing the SessionConnectionCallback from multiple threads.
 */
public interface SessionConnectionCallback {
	
	/**
	 * Notifies the callback of the assigned participant id.
	 * 
	 * @param participantId the assigned local participant id
	 */
	void setParticipantId(int participantId);
	
	/**
	 * Sets the document content of the application document.
	 * 
	 * @param document the new document content
	 */
	void setDocument(PortableDocument document);
	
	/**
	 * Receives a request from the network layer and processes it.
	 * 
	 * @param participantId the participant id of the creator
	 * @param request the request to be received
	 */
	void receiveRequest(int participantId, Request request);
	
	/**
	 * Receives a caret update from the network and processes it.
	 * 
	 * @param participantId the participant id of the creator
	 * @param message the message to be received
	 */
	void receiveCaretUpdate(int participantId, CaretUpdateMessage message);
	
	/**
	 * Receives an acknowledge message from a given site.
	 * 
	 * @param siteId the site id of the other site
	 * @param timestamp the timestamp of the other site
	 */
	void receiveAcknowledge(int siteId, Timestamp timestamp);
	
	/**
	 * This method is called by the network layer to notify this participant
	 * that the session was terminated by the owner.
	 */
	void sessionTerminated();
	
	/**
	 * This method is called by the network layer to notify this participant
	 * has been kicked from the session.
	 */
	void kicked();
	
	/**
	 * This method is called by the network layer to notify that the specified
	 * participant has joined the session.
	 * 
	 * @param participantId the participant id of the joined participant
	 * @param proxy  the RemoteUserProxy for accesing the joined participant
	 */
	void participantJoined(int participantId, RemoteUserProxy proxy);
	
	/**
	 * This method is called by the network layer to notify that the specified
	 * participant has left the session.
	 * 
	 * @param participantId the participant id of the participant that left
	 * @param reason the reason why the participant left
	 */
	void participantLeft(int participantId, int reason);
	
}

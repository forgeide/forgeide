/*
 * $Id: ParticipantPort.java 2840 2006-03-28 17:35:57Z sim $
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
 * The ParticipantPort is the interface for the network layer to speak to
 * the logical server component in the logic layer of the application.
 */
public interface ParticipantPort {
	
	/**
	 * The participant id of the participant represented by this port.
	 * 
	 * @return the participant id of the represented participant
	 */
	int getParticipantId();

	/**
	 * Receives a request from the network layer.
	 * 
	 * @param request the request to receive
	 */
	void receiveRequest(Request request);
		
	/**
	 * Receives a caret update from the network layer.
	 * 
	 * @param message the CaretUpdateMessage to receive
	 */
	void receiveCaretUpdate(CaretUpdateMessage message);
	
	/**
	 * Receives an acknowledge message from the network layer.
	 * 
	 * @param siteId the site id of the sender
	 * @param timestamp the timstamp at the sender's site
	 */
	void receiveAcknowledge(int siteId, Timestamp timestamp);
	
	/**
	 * Notifies the implementation that the participant left the
	 * session.
	 */
	void leave();
	
}

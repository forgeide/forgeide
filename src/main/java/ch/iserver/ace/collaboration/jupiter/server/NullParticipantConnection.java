/*
 * $Id: NullParticipantConnection.java 2840 2006-03-28 17:35:57Z sim $
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

package ch.iserver.ace.collaboration.jupiter.server;

import org.jboss.logging.Logger;

import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.net.ParticipantConnection;
import ch.iserver.ace.net.ParticipantPort;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;

/**
 * Null-object implementation of a ParticipantConnection. Logs all method
 * calls with log level info.
 */
class NullParticipantConnection implements ParticipantConnection {
	
	/**
	 * The Logger used to log method calls.
	 */
	private static final Logger LOG = Logger.getLogger(NullParticipantConnection.class);
			
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#setParticipantId(int)
	 */
	public void setParticipantId(int participantId) {
		LOG.debug("setParticipantId ignored");
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#joinAccepted(ch.iserver.ace.net.ParticipantPort)
	 */
	public void joinAccepted(ParticipantPort port) {
		LOG.debug("joinAccepted ignored");
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#joinRejected(int)
	 */
	public void joinRejected(int code) {
		LOG.debug("joinRejected ignored");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#getUser()
	 */
	public RemoteUserProxy getUser() {
		LOG.debug("getUser ignored");
		return null;
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendDocument(ch.iserver.ace.net.PortableDocument)
	 */
	public void sendDocument(PortableDocument document) {
		LOG.debug("sendDocument ignored");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendRequest(int, ch.iserver.ace.algorithm.Request)
	 */
	public void sendRequest(int participantId, Request request) {
		LOG.debug("sendRequest ignored");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendCaretUpdateMessage(int, ch.iserver.ace.algorithm.CaretUpdateMessage)
	 */
	public void sendCaretUpdateMessage(int participantId, CaretUpdateMessage message) {
		LOG.debug("sendCaretUpdateMessage ignored");
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendAcknowledge(int, ch.iserver.ace.algorithm.Timestamp)
	 */
	public void sendAcknowledge(int siteId, Timestamp timestamp) {
		LOG.debug("sendAcknowledge ignored");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendParticipantJoined(int, ch.iserver.ace.net.RemoteUserProxy)
	 */
	public void sendParticipantJoined(int participantId, RemoteUserProxy proxy) {
		LOG.debug("sendParticipantJoined ignored");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendParticipantLeft(int, int)
	 */
	public void sendParticipantLeft(int participantId, int reason) {
		LOG.debug("sendParticipantLeft ignored");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendKicked()
	 */
	public void sendKicked() {
		LOG.debug("sendKicked ignored");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#close()
	 */
	public void close() {
		LOG.debug("close ignored");
	}

}

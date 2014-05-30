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

package ch.iserver.ace.collaboration.jupiter.server;

import org.jboss.logging.Logger;

import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.collaboration.JoinRequest;
import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.collaboration.jupiter.PublisherConnection;
import ch.iserver.ace.net.ParticipantPort;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 * Failsafe wrapper for a PublisherConnection.
 */
public class PublisherConnectionWrapper implements PublisherConnection {

	/**
	 * The logger instance used by this class.
	 */
	protected static final Logger LOG = Logger.getLogger(PublisherConnectionWrapper.class);

	/**
	 * The target PublisherConnection. Initially this is the real target
	 * connection. If the connection fails, a null object is set as
	 * target.
	 */
	private PublisherConnection target;
	
	/**
	 * The FailureHandler used by this class.
	 */
	private final FailureHandler failureHandler;
	
	/**
	 * Creates a new PublisherConnectionWrapper instance.
	 * 
	 * @param target the target connection
	 * @param failureHandler the FailureHandler
	 */
	public PublisherConnectionWrapper(PublisherConnection target, FailureHandler failureHandler) {
		ParameterValidator.notNull("target", target);
		ParameterValidator.notNull("failureHandler", failureHandler);
		this.target = target;
		this.failureHandler = failureHandler;
	}
	
	/**
	 * Gets the target PublisherConnection. Initially this is the connection
	 * set in the constructor of this class. If the connection fails, it is
	 * replaced with a null object connection.
	 * 
	 * @return the target connection
	 */
	protected PublisherConnection getTarget() {
		return target;
	}
	
	private PublisherConnection createNullConnection() {
		return new NullPublisherConnection();
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.PublisherConnection#sessionFailed(int, java.lang.Exception)
	 */
	public void sessionFailed(int reason, Exception e) {
		getTarget().sessionFailed(reason, e);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.PublisherConnection#sendJoinRequest(ch.iserver.ace.collaboration.JoinRequest)
	 */
	public void sendJoinRequest(JoinRequest request) {
		getTarget().sendJoinRequest(request);
	}
	
	/**
	 * Handles a failure on the target connection.
	 * 
	 * <ol>
	 *   <li>notify the target connection about the session failure</li>
	 *   <li>notify the FailureHandler about the failing connection</li>
	 *   <li>replace the target with a null object connection</li>
	 * </ol>
	 */
	protected void failed(int code, Exception e) {
		getTarget().sessionFailed(code, e);
		failureHandler.handleFailure(0, code);
		target = createNullConnection();
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#getUser()
	 */
	public RemoteUserProxy getUser() {
		return getTarget().getUser();
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#setParticipantId(int)
	 */
	public void setParticipantId(int participantId) {
		throw new UnsupportedOperationException("setParticipant not supported on publisher");
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#joinAccepted(ch.iserver.ace.net.ParticipantPort)
	 */
	public void joinAccepted(ParticipantPort port) {
		throw new UnsupportedOperationException("joinAccepted not supported on publisher");
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#joinRejected(int)
	 */
	public void joinRejected(int code) {
		throw new UnsupportedOperationException("joinRejected not supported on publisher");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendDocument(ch.iserver.ace.net.PortableDocument)
	 */
	public void sendDocument(PortableDocument document) {
		throw new UnsupportedOperationException("sendDocument not supported on publisher");
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendKicked()
	 */
	public void sendKicked() {
		throw new UnsupportedOperationException("sendKicked not supported on publisher");
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendRequest(int, ch.iserver.ace.algorithm.Request)
	 */
	public void sendRequest(int participantId, Request request) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("sendRequest: " + participantId + " " + request);
			}
			getTarget().sendRequest(participantId, request);
		} catch (Exception e) {
			failed(Participant.DISCONNECTED, e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendCaretUpdateMessage(int, ch.iserver.ace.algorithm.CaretUpdateMessage)
	 */
	public void sendCaretUpdateMessage(int participantId, CaretUpdateMessage message) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("sendCaretUpdate: " + participantId + " " + message);
			}
			getTarget().sendCaretUpdateMessage(participantId, message);
		} catch (Exception e) {
			failed(Participant.DISCONNECTED, e);
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendAcknowledge(int, ch.iserver.ace.algorithm.Timestamp)
	 */
	public void sendAcknowledge(int siteId, Timestamp timestamp) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("sendRequest: ack(" + siteId + "," + timestamp + ")");
			}
			getTarget().sendAcknowledge(siteId, timestamp);
		} catch (Exception e) {
			failed(Participant.DISCONNECTED, e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendParticipantJoined(int, ch.iserver.ace.net.RemoteUserProxy)
	 */
	public void sendParticipantJoined(int participantId, RemoteUserProxy proxy) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("sendParticipantJoined: " + participantId + " " + proxy);
			}
			getTarget().sendParticipantJoined(participantId, proxy);
		} catch (Exception e) {
			failed(Participant.DISCONNECTED, e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#sendParticipantLeft(int, int)
	 */
	public void sendParticipantLeft(int participantId, int reason) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("sendParticipantLeft: " + participantId);
			}
			getTarget().sendParticipantLeft(participantId, reason);
		} catch (Exception e) {
			failed(Participant.DISCONNECTED, e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantConnection#close()
	 */
	public void close() {
		getTarget().close();
	}

}

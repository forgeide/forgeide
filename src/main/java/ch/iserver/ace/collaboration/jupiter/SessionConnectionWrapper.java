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

package ch.iserver.ace.collaboration.jupiter;

import org.jboss.logging.Logger;

import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.collaboration.Session;
import ch.iserver.ace.net.SessionConnection;
import ch.iserver.ace.util.ParameterValidator;

/**
 * Fail safe wrapper class for SessionConnection objects.
 */
public class SessionConnectionWrapper implements SessionConnection {
	
	/**
	 * The logger instance used by this class.
	 */
	private static final Logger LOG = Logger.getLogger(SessionConnectionWrapper.class);
	
	/**
	 * The target SessionConnection.
	 */
	private final SessionConnection target;
	
	/**
	 * The failure handler for the target connection.
	 */
	private final FailureHandler handler;
	
	/**
	 * Creates a new SessionConnectionWrapper class.
	 * 
	 * @param target the target connection
	 * @param handler the handler for failures
	 */
	public SessionConnectionWrapper(SessionConnection target, FailureHandler handler) {
		ParameterValidator.notNull("target", target);
		this.target = target;
		this.handler = handler;
	}
	
	/**
	 * Gets the failure handler of this connection wrapper.
	 * 
	 * @return the failure handler
	 */
	protected FailureHandler getFailureHandler() {
		return handler;
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnection#getParticipantId()
	 */
	public int getParticipantId() {
		return target.getParticipantId();
	}

	/**
	 * @see ch.iserver.ace.net.SessionConnection#leave()
	 */
	public void leave() {
		try {
			target.leave();
		} catch (Exception e) {
			getFailureHandler().handleFailure(Session.LEAVE_FAILED, e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.SessionConnection#sendRequest(ch.iserver.ace.algorithm.Request)
	 */
	public void sendRequest(Request request) {
		try {
			LOG.debug("sending request: " + request);
			target.sendRequest(request);			
		} catch (Exception e) {
			getFailureHandler().handleFailure(Session.SEND_FAILED, e);
		}
	}

	/**
	 * @see ch.iserver.ace.net.SessionConnection#sendCaretUpdateMessage(ch.iserver.ace.algorithm.CaretUpdateMessage)
	 */
	public void sendCaretUpdateMessage(CaretUpdateMessage message) {
		try {
			LOG.debug("sending caret update: " + message);
			target.sendCaretUpdateMessage(message);
		} catch (Exception e) {
			getFailureHandler().handleFailure(Session.SEND_FAILED, e);
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnection#sendAcknowledge(int, ch.iserver.ace.algorithm.Timestamp)
	 */
	public void sendAcknowledge(int siteId, Timestamp timestamp) {
		try {
			LOG.debug("sending acknowledge: ack(" + siteId + "," + timestamp + ")");
			target.sendAcknowledge(siteId, timestamp);
		} catch (Exception e) {
			getFailureHandler().handleFailure(Session.SEND_FAILED, e);
		}
	}
	
	// --> FailureHandler interface <--
	
	/**
	 * FailureHandler for SessionConnectionWrapper objects.
	 */
	public static interface FailureHandler {
		
		/**
		 * Handle a failure of this connection.
		 * 
		 * @param reason the reason
		 * @param e the exception cause, may be null
		 */
		void handleFailure(int reason, Exception e);
		
	}

}

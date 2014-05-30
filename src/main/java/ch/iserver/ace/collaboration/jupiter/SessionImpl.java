/*
 * $Id: SessionImpl.java 2840 2006-03-28 17:35:57Z sim $
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


import java.util.HashMap;
import java.util.Map;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.algorithm.TransformationException;
import ch.iserver.ace.algorithm.jupiter.Jupiter;
import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.collaboration.ParticipantSessionCallback;
import ch.iserver.ace.collaboration.Session;
import ch.iserver.ace.collaboration.jupiter.SessionConnectionWrapper.FailureHandler;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.net.SessionConnection;
import ch.iserver.ace.util.ParameterValidator;
import ch.iserver.ace.util.ThreadDomain;

/**
 * Default implementation of the Session interface. This class further implements
 * the SessionConnectionCallback interface and can thus be set as callback
 * on SessionConnections.
 */
public class SessionImpl extends AbstractSession 
		implements ConfigurableSession, FailureHandler {
		
	/**
	 * The SessionCallback from the application layer.
	 */
	private ParticipantSessionCallback callback = NullSessionCallback.getInstance();
	
	/**
	 * The SessionConnection from the network layer.
	 */
	private SessionConnection connection;

	/**
	 * The ThreadDomain used to wrap the session connection.
	 */
	private ThreadDomain threadDomain;
	
	/**
	 * Creates a new SessionImpl.
	 */
	public SessionImpl() {
		this(new AlgorithmWrapperImpl(new Jupiter(true)));
	}
			
	/**
	 * Creates a new SessionImpl.
	 * 
	 * @param algorithm the algorithm for the session
	 */
	public SessionImpl(AlgorithmWrapper algorithm) {
		super(algorithm);
	}
		
	/**
	 * Sets the callback to be notified from the application layer.
	 * 
	 * @param callback the new callback
	 */
	public void setSessionCallback(ParticipantSessionCallback callback) {
		this.callback = callback == null ? NullSessionCallback.getInstance() : callback;
		if (callback != null) {
			setLock(callback.getLock());
		}
	}
	
	/**
	 * @return the callback to be notified from the application layer
	 */
	protected ParticipantSessionCallback getCallback() {
		return callback;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.ConfigurableSession#setConnection(ch.iserver.ace.net.SessionConnection)
	 */
	public void setConnection(SessionConnection connection) {
		ParameterValidator.notNull("connection", connection);
		this.connection = (SessionConnection) getThreadDomain().wrap(
				new SessionConnectionWrapper(connection, this), SessionConnection.class, true);
	}
	
	/**
	 * @return the connection to the network layer
	 */
	protected SessionConnection getConnection() {
		return connection;
	}
	
	/**
	 * @return the ThreadDomain used by the session to wrap the session 
	 *         connection
	 */
	public ThreadDomain getThreadDomain() {
		return threadDomain;
	}
	
	/**
	 * Sets the ThreadDomain used to wrap the SessionConnection.
	 * 
	 * @param threadDomain 
	 */
	public void setThreadDomain(ThreadDomain threadDomain) {
		this.threadDomain = threadDomain;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AbstractSession#createAcknowledgeAction()
	 */
	protected AcknowledgeAction createAcknowledgeAction() {
		return new AcknowledgeAction() {
			public void execute() {
				lock();
				try {
					Timestamp timestamp = getAlgorithm().getTimestamp();
					int siteId = getAlgorithm().getSiteId();
					getConnection().sendAcknowledge(siteId, timestamp);
				} finally {
					unlock();
				}
			}
		};
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.Session#getParticipantId()
	 */
	public int getParticipantId() {
		return connection.getParticipantId();
	}

	/**
	 * @see ch.iserver.ace.collaboration.Session#leave()
	 */
	public synchronized void leave() {
		checkSessionState();
		try {
			connection.leave();
			callback = NullSessionCallback.getInstance();
		} finally {
			destroy();
		}
	}

	/**
	 * @see ch.iserver.ace.collaboration.Session#sendOperation(ch.iserver.ace.algorithm.Operation)
	 */
	public void sendOperation(Operation operation) {
		checkSessionState();
		resetAcknowledgeStrategy();
		Request request = getAlgorithm().generateRequest(operation);
		getConnection().sendRequest(request);
	}

	/**
	 * @see ch.iserver.ace.collaboration.Session#sendCaretUpdate(ch.iserver.ace.CaretUpdate)
	 */
	public void sendCaretUpdate(CaretUpdate update) {
		checkSessionState();
		resetAcknowledgeStrategy();
		CaretUpdateMessage message = getAlgorithm().generateCaretUpdateMessage(update);
		getConnection().sendCaretUpdateMessage(message);
	}

	// --> SessionConnectionCallback methods <--
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#kicked()
	 */
	public synchronized void kicked() {
		getCallback().kicked();
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#sessionTerminated()
	 */
	public synchronized void sessionTerminated() {
		getCallback().sessionTerminated();
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#receiveCaretUpdate(int, ch.iserver.ace.algorithm.CaretUpdateMessage)
	 */
	public synchronized void receiveCaretUpdate(int participantId, CaretUpdateMessage message) {
		if (!isParticipant(participantId)) {
			throw new IllegalArgumentException("no participant with id " + participantId + " in session");
		}
		lock();
		try {
			CaretUpdate update = getAlgorithm().receiveCaretUpdateMessage(message);
			Participant participant = getParticipant(participantId);
			getCallback().receiveCaretUpdate(participant, update);
		} catch (TransformationException e) {
			getCallback().sessionFailed(Session.TRANSFORMATION_FAILED, e);
		} finally {
			unlock();
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#receiveRequest(int, ch.iserver.ace.algorithm.Request)
	 */
	public synchronized void receiveRequest(int participantId, Request request) {
		if (!isParticipant(participantId)) {
			throw new IllegalArgumentException("no participant with id " + participantId + " in session");
		}
		lock();
		try {
			Operation operation = getAlgorithm().receiveRequest(request);
			getCallback().receiveOperation(getParticipant(participantId), operation);
		} catch (TransformationException e) {
			getCallback().sessionFailed(Session.TRANSFORMATION_FAILED, e);
		} finally {
			messageReceived();
			unlock();
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#receiveAcknowledge(int, ch.iserver.ace.algorithm.Timestamp)
	 */
	public synchronized void receiveAcknowledge(int siteId, Timestamp timestamp) {
		lock();
		try {
			getAlgorithm().acknowledge(siteId, timestamp);
		} catch (TransformationException e) {
			getCallback().sessionFailed(Session.TRANSFORMATION_FAILED, e);
		} finally {
			unlock();
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#setParticipantId(int)
	 */
	public void setParticipantId(int participantId) {
		getCallback().setParticipantId(participantId);
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#setDocument(ch.iserver.ace.net.PortableDocument)
	 */
	public synchronized void setDocument(PortableDocument document) {
		Map participants = new HashMap();
		int[] ids = document.getParticipantIds();
		for (int i = 0; i < ids.length; i++) {
			int id = ids[i];
			Participant participant = createParticipant(id, document.getUserProxy(id));
			participants.put(new Integer(id), participant);
			addParticipant(participant);
		}
		PortableDocumentWrapper wrapper = new PortableDocumentWrapper(document, participants);
		getCallback().setDocument(wrapper);
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#participantJoined(int, ch.iserver.ace.net.RemoteUserProxy)
	 */
	public synchronized void participantJoined(int participantId, RemoteUserProxy proxy) {
		Participant participant = createParticipant(participantId, proxy);
		addParticipant(participant);
		getCallback().participantJoined(participant);
	}
	
	/**
	 * @see ch.iserver.ace.net.SessionConnectionCallback#participantLeft(int, int)
	 */
	public synchronized void participantLeft(int participantId, int reason) {
		Participant participant = getParticipant(participantId);
		removeParticipant(participant);
		getCallback().participantLeft(participant, reason);
	}
	
	// --> FailureHandler implementation <--
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.SessionConnectionWrapper.FailureHandler#handleFailure(int, java.lang.Exception)
	 */
	public synchronized void handleFailure(int reason, Exception e) {
		getCallback().sessionFailed(reason, e);
	}
	
}

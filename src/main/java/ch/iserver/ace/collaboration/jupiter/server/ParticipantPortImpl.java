/*
 * $Id: ParticipantPortImpl.java 2840 2006-03-28 17:35:57Z sim $
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

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.algorithm.TransformationException;
import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.collaboration.jupiter.AlgorithmWrapper;
import ch.iserver.ace.net.ParticipantPort;
import ch.iserver.ace.util.ParameterValidator;

/**
 * Default implementation of the ParticipantPort interface. 
 */
public class ParticipantPortImpl implements ParticipantPort {
		
	/**
	 * The server logic to which this port belongs.
	 */
	private final ServerLogic logic;
	
	/**
	 * The forwarder to forward events.
	 */
	private final Forwarder forwarder;
	
	/**
	 * The participant id of the participant.
	 */
	private final int participantId;
	
	/**
	 * The algorithm used to transform requests.
	 */
	private final AlgorithmWrapper algorithm;
	
	/**
	 * The failure handler used by the port.
	 */
	private final FailureHandler failureHandler;
		
	/**
	 * Creates a new ParticipantPortImpl using the passed in server logic
	 * and algorithm.
	 * 
	 * @param logic the server logic used by this port
	 * @param participantId the participant id of the participant
	 * @param algorithm the algorithm used to transform requests
	 * @param forwarder
	 */
	public ParticipantPortImpl(ServerLogic logic, FailureHandler handler, int participantId, AlgorithmWrapper algorithm, Forwarder forwarder) {
		ParameterValidator.notNull("algorithm", algorithm);
		this.logic = logic;
		this.participantId = participantId;
		this.algorithm = algorithm;
		this.forwarder = forwarder;
		this.failureHandler = handler;
	}

	/**
	 * @return the server logic used by this port
	 */
	protected ServerLogic getLogic() {
		return logic;
	}
		
	/**
	 * @return the algorithm used to transform requests
	 */
	public AlgorithmWrapper getAlgorithm() {
		return algorithm;
	}
	
	/** 
	 * @see ch.iserver.ace.net.ParticipantPort#getParticipantId()
	 */
	public int getParticipantId() {
		return participantId;
	}
	
	/**
	 * Retrieves the forwarder which is responsible to forward transformed
	 * operations and caret updates to the other participants.
	 * 
	 * @return the forwarder instance
	 */
	protected Forwarder getForwarder() {
		return forwarder;
	}
	
	/**
	 * Retrieves the FailureHandler which is responsible to handle any
	 * failures in this port.
	 * 
	 * @return the FailureHandler instance
	 */
	protected FailureHandler getFailureHandler() {
		return failureHandler;
	}

	/**
	 * @see ch.iserver.ace.net.ParticipantPort#receiveCaretUpdate(ch.iserver.ace.algorithm.CaretUpdateMessage)
	 */
	public void receiveCaretUpdate(CaretUpdateMessage message) {
		try {
			CaretUpdate update = getAlgorithm().receiveCaretUpdateMessage(message);
			getForwarder().sendCaretUpdate(getParticipantId(), update);
		} catch (Exception e) {
			getFailureHandler().handleFailure(getParticipantId(), Participant.RECEPTION_FAILED);
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantPort#receiveRequest(ch.iserver.ace.algorithm.Request)
	 */
	public void receiveRequest(Request request) {
		try {
			Operation op = getAlgorithm().receiveRequest(request);
			getForwarder().sendOperation(getParticipantId(), op);
		} catch (Exception e) {
			getFailureHandler().handleFailure(getParticipantId(), Participant.RECEPTION_FAILED);
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantPort#receiveAcknowledge(int, ch.iserver.ace.algorithm.Timestamp)
	 */
	public void receiveAcknowledge(int siteId, Timestamp timestamp) {
		try {
			getAlgorithm().acknowledge(siteId, timestamp);
		} catch (TransformationException e) {
			getFailureHandler().handleFailure(getParticipantId(), Participant.RECEPTION_FAILED);
		}
	}
	
	/**
	 * @see ch.iserver.ace.net.ParticipantPort#leave()
	 */
	public void leave() {
		getLogic().leave(getParticipantId());
	}
	
}

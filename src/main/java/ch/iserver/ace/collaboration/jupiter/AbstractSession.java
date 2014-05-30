/*
 * $Id: AbstractSession.java 2679 2006-01-09 21:34:48Z sim $
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.collaboration.Session;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.Lock;
import ch.iserver.ace.util.ParameterValidator;
import ch.iserver.ace.util.UUID;

/**
 * Abstract base class for Session implementations. Implements the common methods
 * of both PublishedSessionImpl and SessionImpl.
 */
abstract class AbstractSession implements Session {
	
	/**
	 * Unique session id useful for debugging.
	 */
	private final String id;
	
	/**
	 * The Lock used to guard the access to the Algorithm.
	 */
	private Lock lock;
	
	/**
	 * The AlgorithmWrapper wrapping the Algorithm.
	 */
	private final AlgorithmWrapper algorithm;
	
	/**
	 * The current Set of Participant objects.
	 */
	private final Set participants = new HashSet();
	
	/**
	 * A mapping from participant id to Participant objects.
	 */
	private final Map participantMap = new HashMap();
	
	/**
	 * The user registry containing the known users.
	 */
	private UserRegistry userRegistry;

	/**
	 * The AcknowledgeStrategy used by this class.
	 */
	private AcknowledgeStrategy acknowledgeStrategy = new NullAcknowledgeStrategy();
	
	/**
	 * Flag used to determine when the session has been destroyed.
	 */
	private boolean destroyed;
	
	/**
	 * Creates a new AbstractSession that uses the given Algorithm.
	 * 
	 * @param algorithm the Algorithm used by the Session
	 */
	protected AbstractSession(AlgorithmWrapper algorithm) {
		ParameterValidator.notNull("algorithm", algorithm);
		this.algorithm = algorithm;
		this.id = UUID.nextUUID();
	}
	
	/**
	 * Gets the unique id of this session.
	 * 
	 * @return the unique session id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return true iff the session has been destroyed
	 */
	public boolean isDestroyed() {
		return destroyed;
	}
	
	/**
	 * Sets the UserRegistry to be used by the session.
	 * 
	 * @param registry the user registry
	 */
	public void setUserRegistry(UserRegistry registry) {
		this.userRegistry = registry;
	}
	
	/**
	 * @return gets the UserRegistry in use
	 */
	protected UserRegistry getUserRegistry() {
		return userRegistry;
	}
	
	/**
	 * @return the AlgorithmWrapper
	 */
	protected AlgorithmWrapper getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * Gets the Lock used to guard the access to the transformation engine.
	 * 
	 * @return the Lock
	 */
	protected Lock getLock() {
		return lock;
	}
	
	/**
	 * Sets the Lock used to guard the access to the transformation engine.
	 * 
	 * @param lock the Lock
	 */
	protected void setLock(Lock lock) {
		this.lock = lock;
	}
		
	/**
	 * Checks the session's state. If the session is destroyed, an 
	 * IllegalStateException is thrown.
	 * 
	 * @throws IllegalStateException if the session has been destroyed
	 */
	protected final void checkSessionState() {
		if (isDestroyed()) {
			throw new IllegalStateException("session is disposed");
		}
	}

	/**
	 * @see ch.iserver.ace.collaboration.Session#lock()
	 */
	public void lock() {
		lock.lock();
	}

	/**
	 * @see ch.iserver.ace.collaboration.Session#unlock()
	 */
	public void unlock() {
		lock.unlock();
	}

	/**
	 * @see ch.iserver.ace.collaboration.Session#getParticipants()
	 */
	public Set getParticipants() {
		checkSessionState();
		return Collections.unmodifiableSet(participants);
	}
	
	/**
	 * Determines whether the given participant id corresponds to an active
	 * participant.
	 * 
	 * @param participantId the id of the participant
	 * @return true iff that particular participant is in the session
	 */
	protected boolean isParticipant(int participantId) {
		return participantMap.containsKey(new Integer(participantId));
	}

	/**
	 * @see ch.iserver.ace.collaboration.Session#getParticipant(int)
	 */
	public synchronized Participant getParticipant(int participantId) {
		checkSessionState();
		Participant participant = (Participant) participantMap.get(new Integer(participantId));
		if (participant == null) {
			throw new IllegalArgumentException("no participant with id " + participantId + " found");
		}
		return participant;
	}

	/**
	 * Adds a Participant to the current set of participants in this Session.
	 * 
	 * @param participant the Participant to be added
	 */
	protected synchronized void addParticipant(Participant participant) {
		participants.add(participant);
		participantMap.put(new Integer(participant.getParticipantId()), participant);
	}

	/**
	 * Removes a Participant from the current set of participants in the
	 * Session.
	 * 
	 * @param participant the Participant to be removed
	 */
	protected synchronized void removeParticipant(Participant participant) {
		participants.remove(participant);
		participantMap.remove(new Integer(participant.getParticipantId()));
	}

	/**
	 * Factory method to create Participant objects.
	 * 
	 * @param participantId the id of the participant
	 * @param proxy the RemoteUserProxy for the user
	 * @return a Participant instance
	 */
	protected Participant createParticipant(int participantId, RemoteUserProxy proxy) {
		return new ParticipantImpl(participantId, getUserRegistry().getUser(proxy));
	}

	/**
	 * Sets the AcknowledgeStrategy to be used. Do not call this method more
	 * than once.
	 * 
	 * @param acknowledgeStrategy the new AcknowledgeStrategy
	 */
	public void setAcknowledgeStrategy(AcknowledgeStrategy acknowledgeStrategy) {
		this.acknowledgeStrategy = acknowledgeStrategy;
		this.acknowledgeStrategy.init(createAcknowledgeAction());
	}
	
	/**
	 * @return the AcknowledgeStrategy used by the session
	 */
	public AcknowledgeStrategy getAcknowledgeStrategy() {
		return acknowledgeStrategy;
	}

	/**
	 * Resets the acknowledge strategy.
	 */
	protected void resetAcknowledgeStrategy() {
		acknowledgeStrategy.reset();
	}
	
	/**
	 * Notifies the acknowledge strategy that an (unacknowledged) message
	 * has been received.
	 */
	protected void messageReceived() {
		acknowledgeStrategy.messageReceived();
	}

	/**
	 * Creates a new AcknowledgeAction to be executed each time the 
	 * AcknowledgeStrategy decides it's time for an acknowledge. The action
	 * defines what happens at that moment.
	 * 
	 * @return the action to be executed
	 */
	protected abstract AcknowledgeAction createAcknowledgeAction();
		
	/**
	 * Destroys the session.
	 */
	protected void destroy() {
		destroyed = true;
		acknowledgeStrategy.destroy();
	}
	
	// --> java.lang.Object methods <--
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getName() + "[id=" + getId() + "]";
	}
	
}

/*
 * $Id: Session.java 2430 2005-12-11 15:17:11Z sim $
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

import java.util.Set;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.util.InterruptedRuntimeException;

/**
 * A session represents one particular editing session of a shared document.
 * An implementation of this class allows communicating with the other
 * participants in the session. This includes sending/receiving of requests
 * as well as sending/receiving of awareness information.
 *
 * <p>A session is obtained either by joining a published document or
 * by accepting an invitation of another user.</p>
 */
public interface Session {
	
	/**
	 * Constant used to specify that the session failed because
	 * of a transformation error.
	 */
	int TRANSFORMATION_FAILED = 1;
	
	/**
	 * Constant used to specify that the session failed because
	 * of a failure while leaving the session.
	 */
	int LEAVE_FAILED = 2;
	
	/**
	 * Constant used to specify that the session failed because
	 * an operation could not be sent.
	 */
	int SEND_FAILED = 3;
	
	/**
	 * Locks the session's logic so that only the calling thread can access
	 * the concurrency sensitive parts of the logic. This method must be
	 * called before any send call is invoked.
	 * 
	 * @throws InterruptedRuntimeException
	 */
	void lock();
	
	/**
	 * Unlocks the session's logic so that other threads may gain access
	 * to the session's logic (the operational transformation engine). Take
	 * care that every call to {@link #lock()} is accompanied by a exactly
	 * one call to this method.
	 */
	void unlock();
	
	/**
	 * Gets the participant id of the local user. This id uniquely identifies 
	 * the user within an editing session. The participant id is available to 
	 * the application mainly for informational purposes. The Session 
	 * implementation will know when and how to use this id appropriately,
	 * without intervention of the application layer.
	 *
	 * @return the participant id of the local user
	 */
	int getParticipantId();
	
	/**
	 * Leaves the session. After calling this method, the session becomes
	 * stale, i.e. it should not be used anymore.
	 */
	void leave();
	
	/**
	 * Sends the given operation to the session.
	 *
	 * @param operation the operation to be sent
	 */
	void sendOperation(Operation operation);
	
	/**
	 * Sends the given caret update to the session.
	 *
	 * @param update the caret update of this user
	 */
	void sendCaretUpdate(CaretUpdate update);
		
	/**
	 * Gets a read-only set of users that are participating in this session.
	 *
	 * @return a read-only set of participants
	 */
	Set getParticipants();
	
	/**
	 * Gets the participant with the given id. If there is no participant
	 * with the given id in the session, null is returned.
	 * 
	 * @param participantId the participant id of the participant to retrieve
	 * @return the Participant with the given id or null if there is none
	 */
	Participant getParticipant(int participantId);
		
}

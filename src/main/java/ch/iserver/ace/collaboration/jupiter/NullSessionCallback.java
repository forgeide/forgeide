/*
 * $Id: NullSessionCallback.java 2430 2005-12-11 15:17:11Z sim $
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

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.collaboration.ParticipantSessionCallback;
import ch.iserver.ace.collaboration.PortableDocument;
import ch.iserver.ace.util.Lock;

/**
 * Null object of a SessionCallback. Logs a warning if methods are called on
 * this object.
 */
final class NullSessionCallback implements ParticipantSessionCallback {
	
	/**
	 * Logger used by the singleton.
	 */
	private static final Logger LOG = Logger.getLogger(NullSessionCallback.class);
	
	/**
	 * The singleton instance.
	 */
	private static ParticipantSessionCallback instance;
	
	/**
	 * Private hidden constructor.
	 */
	private NullSessionCallback() {
		// hidden constructor
	}
	
	/**
	 * Retrieves the single instance of this class.
	 * 
	 * @return the singleton instance
	 */
	public static final synchronized ParticipantSessionCallback getInstance() {
		if (instance == null) {
			instance = new NullSessionCallback();
		}
		return instance;
	}
	
	public Lock getLock() {
		return null;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.SessionCallback#setParticipantId(int)
	 */
	public void setParticipantId(int participantId) {
		// ignore
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.SessionCallback#participantJoined(ch.iserver.ace.collaboration.Participant)
	 */
	public void participantJoined(Participant participant) {
		LOG.warn("SessionCallback not set on Session (participantJoined called)");
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.SessionCallback#participantLeft(ch.iserver.ace.collaboration.Participant, int)
	 */
	public void participantLeft(Participant participant, int code) {
		LOG.warn("SessionCallback not set on Session (participantLeft called)");
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.ParticipantSessionCallback#setDocument(PortableDocument)
	 */
	public void setDocument(PortableDocument doc) {
		LOG.warn("SessionCallback not set on Session (setDocument called)");
	}

	/**
	 * @see ch.iserver.ace.collaboration.ParticipantSessionCallback#sessionTerminated()
	 */
	public void sessionTerminated() {
		LOG.warn("SessionCallback not set on Session (sessionTerminated called)");
	}

	/**
	 * @see ch.iserver.ace.collaboration.ParticipantSessionCallback#kicked()
	 */
	public void kicked() {
		LOG.warn("SessionCallback not set on Session (kicked called)");
	}

	/**
	 * @see ch.iserver.ace.collaboration.SessionCallback#receiveOperation(ch.iserver.ace.collaboration.Participant, ch.iserver.ace.algorithm.Operation)
	 */
	public void receiveOperation(Participant participant, Operation operation) {
		LOG.warn("SessionCallback not set on Session (receiveOperation called)");
	}

	/**
	 * @see ch.iserver.ace.collaboration.SessionCallback#receiveCaretUpdate(ch.iserver.ace.collaboration.Participant, ch.iserver.ace.CaretUpdate)
	 */
	public void receiveCaretUpdate(Participant participant, CaretUpdate update) {
		LOG.warn("SessionCallback not set on Session (receiveCaretUpdate called)");
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.SessionCallback#sessionFailed(int, java.lang.Exception)
	 */
	public void sessionFailed(int reason, Exception e) {
		LOG.warn("SessionCallback not set on Session (sessionFailed called)");
	}

}

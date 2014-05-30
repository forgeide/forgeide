/*
 * $Id: ParticipantImpl.java 1805 2005-11-25 18:02:41Z sim $
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

import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.collaboration.RemoteUser;
import ch.iserver.ace.util.ParameterValidator;

/**
 * The default implementation of the Participant interface. It is a simple
 * class holding both the participant id and the RemoteUser for the
 * participant.
 */
public class ParticipantImpl implements Participant {

	/**
	 * The RemoteUser of this participant.
	 */
	private final RemoteUser user;
	
	/**
	 * The participant id of the participant.
	 */
	private final int participantId;
	
	/**
	 * Creates a new ParticipantImpl instance with the given participant id
	 * and remote user.
	 * 
	 * @param participantId the participant id
	 * @param user the remote user
	 */
	public ParticipantImpl(int participantId, RemoteUser user) {
		ParameterValidator.notNull("user", user);
		this.user = user;
		this.participantId = participantId;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.Participant#getUser()
	 */
	public RemoteUser getUser() {
		return user;
	}

	/**
	 * @see ch.iserver.ace.collaboration.Participant#getParticipantId()
	 */
	public int getParticipantId() {
		return participantId;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + participantId
		        + ",user=" + user + "]";
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Participant) {
			Participant p = (Participant) obj;
			return getParticipantId() == p.getParticipantId()
					&& getUser().equals(p.getUser());
		} else {
			return false;
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashCode = getParticipantId();
		hashCode += 17 * getUser().hashCode();
		return hashCode;
	}

}

/*
 * $Id: PortableDocument.java 2068 2005-12-02 10:53:04Z sim $
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

import java.util.Collection;
import java.util.Iterator;

import ch.iserver.ace.CaretUpdate;

/**
 * Document passed to the 
 * {@link ch.iserver.ace.collaboration.ParticipantSessionCallback#setDocument(PortableDocument)}
 * method. This is the initial document of the session when a user joins.
 */
public interface PortableDocument {

	/**
	 * Gets the participants of the session. The element type of this
	 * collection is {@link Participant}.
	 * 
	 * @return the participants
	 */
	Collection getParticipants();
	
	/**
	 * Gets the participant with the given id.
	 * 
	 * @param participantId the participant
	 * @return the Participant object
	 */
	Participant getParticipant(int participantId);
	
	/**
	 * Gets the selection of the given participant.
	 * 
	 * @param participantId the participant's id
	 * @return the selection of that participant
	 */
	CaretUpdate getSelection(int participantId);
	
	/**
	 * Gets the fragments of the document.
	 * 
	 * @return the fragments of the document
	 */
	Iterator getFragments();
	
}

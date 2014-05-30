/*
 * $Id: PortableDocument.java 825 2005-10-31 12:20:09Z sim $
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

package ch.iserver.ace.net;

import java.util.Iterator;

import ch.iserver.ace.CaretUpdate;

/**
 * A PortableDocument is a representation of a document in a portable way.
 * PortableDocuments can be serialized in an implementation independent
 * way and thus facilitate collaboration across application boundaries.
 */
public interface PortableDocument {
	
	/**
	 * Gets the set of participant ids.
	 * 
	 * @return the set of participant ids
	 */
	int[] getParticipantIds();
	
	/**
	 * Gets the RemoteUserProxy for the given <var>participantId</var>.
	 * 
	 * @param participantId the participant id
	 * @return the RemoteUserProxy for the participantId
	 */
	RemoteUserProxy getUserProxy(int participantId);
	
	/**
	 * Gets the caret of the specified <var>participant</var>.
	 * 
	 * @param participantId the participant for which to retrieve the caret
	 * @return the CaretUpdate of the given Participant
	 */
	CaretUpdate getSelection(int participantId);
	
	/**
	 * Gets an iterator over all the fragments of the document.
	 * 
	 * @return an iterator over all the fragments
	 */
	Iterator getFragments();
		
}

/*
 * $Id: PortableDocumentWrapper.java 911 2005-11-03 07:40:49Z sim $
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.collaboration.Participant;
import ch.iserver.ace.collaboration.PortableDocument;
import ch.iserver.ace.util.ParameterValidator;

/**
 * ch.iserver.ace.collaboration.PortableDocument implementation that wraps a 
 * ch.iserver.ace.net.PortableDocument.
 */
class PortableDocumentWrapper implements PortableDocument {
	
	/**
	 * The wrapped PortableDocument object from the network layer.
	 */
	private final ch.iserver.ace.net.PortableDocument document;
	
	/**
	 * The mapping from participant id to Participant objects.
	 */
	private final Map participants;
	
	/**
	 * Creates a new PortableDocumentWrapper instance wrapping the given
	 * document from the network layer.
	 * 
	 * @param doc the PortableDocument from the network layer to be wrapped.
	 * @param participants mapping of participants
	 */
	PortableDocumentWrapper(ch.iserver.ace.net.PortableDocument doc, Map participants) {
		ParameterValidator.notNull("doc", doc);
		ParameterValidator.notNull("participants", participants);
		this.document = doc;
		this.participants = new HashMap(participants);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.PortableDocument#getParticipant(int)
	 */
	public Participant getParticipant(int participantId) {
		return (Participant) participants.get(new Integer(participantId));
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.PortableDocument#getParticipants()
	 */
	public Collection getParticipants() {
		return Collections.unmodifiableCollection(participants.values());
	}

	/**
	 * @see ch.iserver.ace.collaboration.PortableDocument#getSelection(int)
	 */
	public CaretUpdate getSelection(int participantId) {
		return document.getSelection(participantId);
	}

	/**
	 * @see ch.iserver.ace.collaboration.PortableDocument#getFragments()
	 */
	public Iterator getFragments() {
		return document.getFragments();
	}

}

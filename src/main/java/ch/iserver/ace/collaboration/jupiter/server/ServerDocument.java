/*
 * $Id: ServerDocument.java 1984 2005-11-30 16:39:04Z sim $
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

import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;

/**
 * A ServerDocument is used by the ServerLogic and the DocumentUpdateProxy
 * to keep an up-to-date copy of the document content on the server-side.
 */
public interface ServerDocument {
	
	/**
	 * Notifies the document that a participant joined the document.
	 * 
	 * @param participantId the id of the participant
	 * @param proxy the user proxy of the user
	 */
	void participantJoined(int participantId, RemoteUserProxy proxy);
	
	/**
	 * Notifies the document that a participant left the document.
	 * 
	 * @param participantId the id of the participant
	 */
	void participantLeft(int participantId);
	
	/**
	 * Updates the caret position of the specified participant.
	 * 
	 * @param participantId the id of the participant
	 * @param dot the dot position
	 * @param mark the mark position
	 */
	void updateCaret(int participantId, int dot, int mark);
	
	/**
	 * Inserts <var>text</var> from the given participant into the document
	 * at the given <var>offset</var>.
	 * 
	 * @param participantId the id of the participant
	 * @param offset the insertion index
	 * @param text the text to insert
	 * @throws ServerDocumentException if the insertion fails
	 */
	void insertString(int participantId, int offset, String text);
	
	/**
	 * Removes <var>length</var> characters starting at <var>offset</var>.
	 * 
	 * @param offset the start offset
	 * @param length the number of characters to delete
	 * @throws ServerDocumentException if the removal fails
	 */
	void removeString(int offset, int length);
	
	/**
	 * Gets the complete text of the document as String.
	 * 
	 * @return the complete text
	 */
	String getText();
	
	/**
	 * Adapts this server document to a portable document suitable to
	 * be transmitted over the network.
	 * 
	 * @return the adapted document
	 */
	PortableDocument toPortableDocument();
	
}

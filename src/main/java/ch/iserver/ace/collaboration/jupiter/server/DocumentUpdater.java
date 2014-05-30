/*
 * $Id: DocumentUpdater.java 2430 2005-12-11 15:17:11Z sim $
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

import org.jboss.logging.Logger;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.algorithm.text.DeleteOperation;
import ch.iserver.ace.algorithm.text.InsertOperation;
import ch.iserver.ace.algorithm.text.NoOperation;
import ch.iserver.ace.algorithm.text.SplitOperation;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 * A Forwarder that updates the server copy of the document. An instance
 * of this class is added to the list of forwarders that receive transformed
 * server-side operations and caret updates. The transformed operations and
 * caret updates are applied to a 
 * {@link ch.iserver.ace.collaboration.jupiter.server.ServerDocument} instance.
 */
class DocumentUpdater implements Forwarder {
	
	/**
	 * The static logger used by classes of this instance.
	 */
	private static final Logger LOG = Logger.getLogger(DocumentUpdater.class);
	
	/**
	 * The target ServerDocument to which all operations and caret updates
	 * are applied to.
	 */
	private final ServerDocument document;
		
	/**
	 * Creates a new DocumentUpdateProxy using the given ServerDocument as target
	 * for applying operations and caret updates.
	 *  
	 * @param document the target ServerDocument
	 */
	DocumentUpdater(final ServerDocument document) {
		ParameterValidator.notNull("document", document);
		this.document = document;
	}
	
	/**
	 * Gets the target ServerDocument to which all operations and caret updates
	 * are applied to.
	 * 
	 * @return the target ServerDocument
	 */
	protected ServerDocument getDocument() {
		return document;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#sendOperation(int, ch.iserver.ace.algorithm.Operation)
	 */
	public void sendOperation(int participantId, Operation operation) {
		ParameterValidator.notNull("operation", operation);
		if (operation instanceof InsertOperation) {
			InsertOperation op = (InsertOperation) operation;
			getDocument().insertString(participantId, op.getPosition(), op.getText());
		} else if (operation instanceof DeleteOperation) {
			DeleteOperation op = (DeleteOperation) operation;
			getDocument().removeString(op.getPosition(), op.getTextLength());
		} else if (operation instanceof SplitOperation) {
			SplitOperation op = (SplitOperation) operation;
			sendOperation(participantId, op.getSecond());
			sendOperation(participantId, op.getFirst());
		} else if (operation instanceof NoOperation) {
			// ignore
		} else {
			LOG.warn("unkown operation class: " + operation.getClass());
		}
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#sendCaretUpdate(int, ch.iserver.ace.CaretUpdate)
	 */
	public void sendCaretUpdate(int participantId, CaretUpdate update) {
		ParameterValidator.notNull("update", update);
		getDocument().updateCaret(participantId, update.getDot(), update.getMark());
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#sendParticipantLeft(int, int)
	 */
	public void sendParticipantLeft(int participantId, int reason) {
		getDocument().participantLeft(participantId);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#sendParticipantJoined(int, ch.iserver.ace.net.RemoteUserProxy)
	 */
	public void sendParticipantJoined(int participantId, RemoteUserProxy proxy) {
		getDocument().participantJoined(participantId, proxy);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.Forwarder#close()
	 */
	public void close() {
		// ignore
	}

}

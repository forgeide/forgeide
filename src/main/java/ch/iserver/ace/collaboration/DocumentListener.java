/*
 * $Id: DocumentListener.java 2043 2005-12-01 14:38:27Z sim $
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

import java.util.EventListener;

/**
 * Discovery listener interface for documents. A registered DocumentListener
 * is notified about document related discoveries. To receive notifications,
 * the DocumentListener must be registered with the
 * {@link ch.iserver.ace.collaboration.CollaborationService#addDocumentListener(DocumentListener)}
 * method.
 */
public interface DocumentListener extends EventListener {
	
	/**
	 * Notifies the listener that new RemoteDocuments were discovered.
	 * 
	 * @param documents the discovered documents
	 */
	void documentsDiscovered(RemoteDocument[] documents);
		
	/**
	 * Notifies the listener that RemoteDocuments were discarded, that is it is
	 * no longer available.
 	 * Do not call any methods on this object that make network calls (see
	 * documentation of RemoteDocument for information, which methods make network
	 * calls).
	 * 
	 * @param documents the discarded documents
	 */
	void documentsDiscarded(RemoteDocument[] documents);
	
}

/*
 * $Id$
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

import ch.iserver.ace.net.RemoteDocumentProxy;

/**
 * A registry and factory for MutableRemoteDocument objects.
 */
public interface DocumentRegistry {
		
	/**
	 * Adds a new remote document to the registry.
	 * 
	 * @param proxy the proxy of the document
	 * @return a new MutableRemoteDocument wrapping the proxy
	 */
	MutableRemoteDocument getDocument(RemoteDocumentProxy proxy);
	
	/**
	 * Gets the document with the given id.
	 * 
	 * @param id the id of the document to remove
	 * @return the document or null if there is none with this id
	 */
	MutableRemoteDocument getDocument(String id);
	
	/**
	 * Removes a document from the registry.
	 * 
	 * @param proxy the proxy of the document to be removed
	 * @return the removed document or null if there is none
	 */
	MutableRemoteDocument removeDocument(RemoteDocumentProxy proxy);
	
}

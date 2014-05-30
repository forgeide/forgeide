/*
 * $Id: PortableDocumentExt.java 2700 2006-01-11 11:27:08Z zbinl $
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

package ch.iserver.ace.net.core;

import java.util.List;
import java.util.Map;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.Fragment;
import ch.iserver.ace.net.PortableDocument;

/**
 * Interface extension of {@link ch.iserver.ace.net.PortableDocument} for the 
 * network layer. 
 * The added functionality concerns the creation of a portable document 
 * when parsing a document request.
 * 
 * @see ch.iserver.ace.net.PortableDocument
 */
public interface PortableDocumentExt extends PortableDocument {

	/**
	 * Gets the participant id for the receiving user of this document.
	 * 
	 * @return the participant id
	 */
	public int getParticipantId();
	
	/**
	 * Sets the participant id of the receiving user of this document.
	 * 
	 * @param id		the participant id for the receiving user
	 */
	public void setParticpantId(int id);
	
	/**
	 * Adds a fragment to this document.
	 * 
	 * @param fragment the fragment to be added
	 */
	public void addFragment(Fragment fragment);
	
	/**
	 * Adds a new particpant to this document given his id and
	 * RemoteUserProxy object.
	 * 
	 * @param id		the id of the participant to add
	 * @param proxy	the RemoteUserProxy of the participant to add
	 */
	public void addParticipant(int id, RemoteUserProxyExt proxy);
	
	/**
	 * Sets a selection for a given participant.
	 * 
	 * @param participantId 	the particpant id
	 * @param selection		the selection to be added
	 */
	public void setSelection(int participantId, CaretUpdate selection);
	
	/**
	 * Sets the document id.
	 * 
	 * @param docId the document id
	 */
	public void setDocumentId(String docId);
	
	/**
	 * Gets the document id.
	 * 
	 * @return the document id
	 */
	public String getDocumentId();
	
	/**
	 * Sets the user id of the publisher of this document.
	 * 
	 * @param publisherId		the user id of the publisher
	 */
	public void setPublisherId(String publisherId);
	
	/**
	 * Gets the publisher id.
	 * 
	 * @return the publisher id
	 */
	public String getPublisherId();
	
	/**
	 * Gets a list with all users participating in this document
	 * session.
	 * 
	 * @return a list with the RemoteUserProxy's of all particpants
	 */
	public List getUsers();
	
	/**
	 * Returns a map with a participantId to RemoteUserProxyExt mapping.
	 * 
	 * @return Map 	a participantId to RemoteUserProxyExt mapping
	 */
	public Map getParticipantIdUserMapping();
	
	
}

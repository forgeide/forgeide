/*
 * $Id: PublishedDocument.java 2833 2006-03-22 22:09:37Z sim $
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

package ch.iserver.ace.net.simulator;

import javax.swing.event.EventListenerList;

import ch.iserver.ace.DocumentDetails;
import ch.iserver.ace.net.DocumentServer;
import ch.iserver.ace.net.DocumentServerLogic;
import ch.iserver.ace.net.InvitationPort;
import ch.iserver.ace.net.ParticipantConnection;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.UUID;

/**
 *
 */
public class PublishedDocument implements DocumentServer {

	private String id = UUID.nextUUID();
	
	private RemoteUserProxy localUser;
	
	private DocumentDetails details;
	
	private DocumentServerLogic logic;
	
	private EventListenerList listeners = new EventListenerList();
	
	public PublishedDocument(RemoteUserProxy localUser, DocumentServerLogic logic, DocumentDetails details) {
		this.localUser = localUser;
		this.details = details;
		this.logic = logic;
	}
	
	public void addPublishedDocumentListener(PublishedDocumentListener l) {
		listeners.add(PublishedDocumentListener.class, l);
	}
	
	public void removePublishedDocumentListener(PublishedDocumentListener l) {
		listeners.remove(PublishedDocumentListener.class, l);
	}
	
	public String getId() {
		return id;
	}
	
	public void join(ParticipantConnection connection) {
		logic.join(connection);
	}
	
	public DocumentDetails getDocumentDetails() {
		return details;
	}
	
	public RemoteUserProxy getPublisher() {
		return localUser;
	}
	
	/**
	 * @see ch.iserver.ace.net.DocumentServer#setDocumentDetails(ch.iserver.ace.DocumentDetails)
	 */
	public void setDocumentDetails(DocumentDetails details) {
		this.details = details;
		PublishedDocumentListener[] listeners = (PublishedDocumentListener[]) this.listeners.getListeners(PublishedDocumentListener.class);
		for (int i = 0; i < listeners.length; i++) {
			PublishedDocumentListener listener = listeners[i];
			listener.documentChanged(id, details);
		}
	}

	/**
	 * @see ch.iserver.ace.net.DocumentServer#invite(ch.iserver.ace.net.InvitationPort)
	 */
	public void invite(InvitationPort port) {
		User user = (User) port.getUser();
		user.invite(new Invitation(new InvitationChannel(port), user, new Document(this, localUser)));
	}

	/**
	 * @see ch.iserver.ace.net.DocumentServer#shutdown()
	 */
	public void shutdown() {
		PublishedDocumentListener[] listeners = (PublishedDocumentListener[]) this.listeners.getListeners(PublishedDocumentListener.class);
		for (int i = 0; i < listeners.length; i++) {
			PublishedDocumentListener listener = listeners[i];
			listener.documentConcealed(id);
		}
	}
	
}

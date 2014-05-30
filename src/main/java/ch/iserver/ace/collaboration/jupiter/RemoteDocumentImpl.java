/*
 * $Id: RemoteDocumentImpl.java 1876 2005-11-28 14:00:18Z sim $
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.iserver.ace.collaboration.JoinCallback;
import ch.iserver.ace.collaboration.RemoteDocument;
import ch.iserver.ace.collaboration.RemoteUser;
import ch.iserver.ace.net.RemoteDocumentProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 * Default implementation of the RemoteDocument interface.
 */
class RemoteDocumentImpl implements MutableRemoteDocument {
		
	/**
	 * PropertyChangeSupport used to manage PropertyChangeListeners
	 */
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	/**
	 * The wrapped RemoteDocumentProxy instance.
	 */
	private final RemoteDocumentProxy proxy;
	
	/**
	 * The publisher of the document.
	 */
	private final RemoteUser publisher;
	
	/**
	 * The SessionFactory used to create sessions.
	 */
	private final SessionFactory sessionFactory;
	
	/**
	 * The title of the document.
	 */
	private String title;
	
	/**
	 * Creates a new RemoteDocumentImpl passing most requests directly to
	 * the passed in RemoteDocumentProxy.
	 * 
	 * @param proxy the document proxy
	 * @param sessionFactory the session factory
	 * @param publisher the publisher of the document
	 */
	RemoteDocumentImpl(RemoteDocumentProxy proxy, SessionFactory sessionFactory, RemoteUser publisher) {
		ParameterValidator.notNull("proxy", proxy);
		ParameterValidator.notNull("sessionFactory", sessionFactory);
		ParameterValidator.notNull("publisher", publisher);
		this.proxy = proxy;
		this.sessionFactory = sessionFactory;
		this.publisher = publisher;
		this.title = proxy.getDocumentDetails().getTitle();
		this.title = this.title == null ? "" : title;
	}
	
	/**
	 * @return gets the session factory
	 */
	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.RemoteDocument#getId()
	 */
	public String getId() {
		return proxy.getId();
	}

	/**
	 * @see ch.iserver.ace.collaboration.RemoteDocument#getTitle()
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.MutableRemoteDocument#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		String old = this.title;
		if (!old.equals(title)) {
			this.title = title;
			support.firePropertyChange(TITLE_PROPERTY, old, title);
		}
	}

	/**
	 * @see ch.iserver.ace.collaboration.RemoteDocument#getPublisher()
	 */
	public RemoteUser getPublisher() {
		return publisher;
	}

	/**
	 * @see ch.iserver.ace.collaboration.RemoteDocument#join(ch.iserver.ace.collaboration.JoinCallback)
	 */
	public void join(final JoinCallback callback) {
		proxy.join(new JoinNetworkCallbackImpl(callback, getSessionFactory()));
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.RemoteDocument#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.RemoteDocument#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof RemoteDocument) {
			RemoteDocument document = (RemoteDocument) obj;
			return getId().equals(document.getId());
		} else {
			return false;
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getId().hashCode();
	}

}

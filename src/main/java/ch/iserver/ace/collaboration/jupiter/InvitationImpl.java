/*
 * $Id: InvitationImpl.java 2079 2005-12-02 12:41:42Z sim $
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

import ch.iserver.ace.collaboration.Invitation;
import ch.iserver.ace.collaboration.JoinCallback;
import ch.iserver.ace.collaboration.RemoteDocument;
import ch.iserver.ace.collaboration.RemoteUser;
import ch.iserver.ace.net.InvitationProxy;
import ch.iserver.ace.net.JoinNetworkCallback;
import ch.iserver.ace.util.ParameterValidator;

/**
 * Default implementation of the Invitation interface. This class wraps a
 * InvitationProxy from the network layer and delegates most method calls
 * to it.
 */
class InvitationImpl implements Invitation {
	
	/**
	 * The wrapped InvitationProxy from the network layer.
	 */
	private final InvitationProxy proxy;
		
	/**
	 * The RemoteUser that invited the local user.
	 */
	private final RemoteUser inviter;
	
	/**
	 * The RemoteDocument for which the user is invited.
	 */
	private final RemoteDocument document;
	
	/**
	 * The session factory used to create the sessions.
	 */
	private final SessionFactory sessionFactory;
	
	/**
	 * Creates a new InvitationImpl object delegating most of the work
	 * to the passed in InvitationProxy.
	 * 
	 * @param proxy the InvitationProxy wrapped by this instance
	 * @param document the RemoteDocument to which the user is invited
	 * @param factory the session factory used to create sessions
	 */
	InvitationImpl(InvitationProxy proxy, 
					RemoteDocument document,
					SessionFactory factory) {
		ParameterValidator.notNull("proxy", proxy);
		ParameterValidator.notNull("document", document);
		ParameterValidator.notNull("factory", factory);
		this.proxy = proxy;
		this.document = document;
		this.inviter = document.getPublisher();
		this.sessionFactory = factory;
	}
	
	/**
	 * @return the wrapped InvitationProxy instance
	 */
	private InvitationProxy getProxy() {
		return proxy;
	}
		
	/**
	 * @see ch.iserver.ace.collaboration.Invitation#getInviter()
	 */
	public RemoteUser getInviter() {
		return inviter;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.Invitation#getDocument()
	 */
	public RemoteDocument getDocument() {
		return document;
	}

	/**
	 * @return the session factory used by this invitation to create sessions
	 */
	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * Create a new JoinNetworkCallback object.
	 * 
	 * @param callback the callback to the application layer
	 * @param factory the session factory
	 * @return the callback for the network layer
	 */
	protected JoinNetworkCallback createJoinNetworkCallback(JoinCallback callback, SessionFactory factory) {
		ParameterValidator.notNull("callback", callback);
		ParameterValidator.notNull("factory", factory);
		return new JoinNetworkCallbackImpl(callback, sessionFactory);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.Invitation#accept(ch.iserver.ace.collaboration.JoinCallback)
	 */
	public void accept(JoinCallback callback) {
		ParameterValidator.notNull("callback", callback);
		getProxy().accept(createJoinNetworkCallback(callback, getSessionFactory()));
	}

	/**
	 * @see ch.iserver.ace.collaboration.Invitation#reject()
	 */
	public void reject() {
		getProxy().reject();
	}

}

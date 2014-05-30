/*
 * $Id: Invitation.java 2833 2006-03-22 22:09:37Z sim $
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

import ch.iserver.ace.net.InvitationProxy;
import ch.iserver.ace.net.JoinNetworkCallback;
import ch.iserver.ace.net.RemoteDocumentProxy;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 *
 */
public class Invitation implements InvitationProxy {
	
	private InvitationChannel invitation;
	
	private RemoteUserProxy localUser;
	
	private RemoteDocumentProxy document;
	
	public Invitation(InvitationChannel invitation, RemoteUserProxy user, RemoteDocumentProxy document) {
		ParameterValidator.notNull("invitation", invitation);
		ParameterValidator.notNull("user", user);
		ParameterValidator.notNull("document", document);
		this.invitation = invitation;
		this.localUser = user;
		this.document = document;
	}
	
	/**
	 * @see ch.iserver.ace.net.InvitationProxy#getDocument()
	 */
	public RemoteDocumentProxy getDocument() {
		return document;
	}

	/**
	 * @see ch.iserver.ace.net.InvitationProxy#accept(ch.iserver.ace.net.JoinNetworkCallback)
	 */
	public void accept(JoinNetworkCallback callback) {
		invitation.accept(new ParticipantChannel(callback, localUser));
	}

	/**
	 * @see ch.iserver.ace.net.InvitationProxy#reject()
	 */
	public void reject() {
		invitation.reject();
	}

}

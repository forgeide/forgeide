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

import ch.iserver.ace.collaboration.JoinRequest;
import ch.iserver.ace.collaboration.RemoteUser;
import ch.iserver.ace.collaboration.jupiter.server.ServerLogic;
import ch.iserver.ace.net.ParticipantConnection;
import ch.iserver.ace.util.ParameterValidator;

/**
 * The default implementation of the JoinRequest interface.
 */
public class JoinRequestImpl implements JoinRequest {

	/**
	 * The user that wants to join.
	 */
	private RemoteUser user;
	
	/**
	 * The ParticipantConnection to the joining user.
	 */
	private ParticipantConnection connection;
	
	/**
	 * The ServerLogic of the session to which the participant wants to join.
	 */
	private ServerLogic logic;
	
	/**
	 * Creates a new JoinRequestImpl instance.
	 * 
	 * @param logic the ServerLogic of the session
	 * @param user the user that wants to join
	 * @param connection the connection to the joining user
	 */
	public JoinRequestImpl(ServerLogic logic, RemoteUser user, ParticipantConnection connection) {
		ParameterValidator.notNull("user", user);
		ParameterValidator.notNull("connection", connection);
		this.logic = logic;
		this.user = user;
		this.connection = connection;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.JoinRequest#getUser()
	 */
	public RemoteUser getUser() {
		return user;
	}

	/**
	 * @see ch.iserver.ace.collaboration.JoinRequest#accept()
	 */
	public void accept() {
		logic.joinAccepted(connection);
	}

	/**
	 * @see ch.iserver.ace.collaboration.JoinRequest#reject()
	 */
	public void reject() {
		logic.joinRejected(connection);
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof JoinRequest) {
			JoinRequest request = (JoinRequest) obj;
			return getUser().equals(request.getUser());
		} else {
			return false;
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return 7 * getUser().hashCode();
	}

}

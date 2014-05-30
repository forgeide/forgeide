/*
 * $Id: Invitation.java 2075 2005-12-02 12:29:32Z sim $
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


/**
 * This interface represents an invitation from another user to an editing
 * session. The invitation can be accepted or rejected. Further it is possible
 * to access the RemoteUser interface of the inviter.
 */
public interface Invitation {

	/**
	 * Retrieves the remote user that invited this user to a given
	 * session.
	 *
	 * @return the inviter of this user
	 */
	RemoteUser getInviter();
	
	/**
	 * Retrieves the remote document for which the local user is 
	 * invited.
	 * 
	 * @return the remote document to which the local user is invited
	 */
	RemoteDocument getDocument();
	
	/**
	 * Accepts the invitation and gets the corresponding session info object.
	 *
	 * @param callback the callback for the join event
	 */
	void accept(JoinCallback callback);
	
	/**
	 * Rejects the invitation.
	 */
	void reject();

}

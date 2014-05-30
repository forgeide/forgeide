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

package ch.iserver.ace.net;

/**
 * An InvitationPort is passed from the collaboration layer to the network
 * layer whenever the publisher invites a user. The port allows the network
 * layer to return the response of the invitation request to the
 * collaboration layer of the publisher.
 */
public interface InvitationPort {
	
	/**
	 * The user that is to be invited.
	 * 
	 * @return the invited user
	 */
	RemoteUserProxy getUser();
	
	/**
	 * Notifies the collaboration layer that the invitation was accepted
	 * by the invited user.
	 * 
	 * @param connection the connection to the invited participant
	 */
	void accept(ParticipantConnection connection);
	
	/**
	 * Notifies the collaboration layer that the invitation has been
	 * rejected by the invited user.
	 */
	void reject();
	
}

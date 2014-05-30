/*
 * $Id: InvitationHandler.java 2833 2006-03-22 22:09:37Z sim $
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
 * Callback interface for invitations sent to the local user. The callback
 * interface must be set on the CollaborationService with the 
 * {@link ch.iserver.ace.collaboration.CollaborationService#setInvitationHandler(InvitationHandler)}
 * method. The callback is invoked whenever there is an invitation is
 * received.
 */
public interface InvitationHandler {
	
	/**
	 * Notifies the callback that an invitation is received. The callback is
	 * responsible to handle the invitation, that is to either accept
	 * or reject it.
	 * 
	 * @param event the Invitation containing all the necessary 
	 *              information
	 */
	void handleInvitation(Invitation event);
	
}

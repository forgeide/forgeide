/*
 * $Id: JoinCallback.java 1607 2005-11-22 07:40:02Z sim $
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
 * Callback interface for join requests. The callback is notified
 * about the outcome of the join request. A join request can either
 * be rejected or accepted. Do not expect that a join request
 * results in a response. The owner of the document might never respond
 * to the request.
 */
public interface JoinCallback {
	
	/**
	 * Notifies the callback that the request was rejected by the owner of 
	 * the document.
	 * 
	 * @param code the rejection code
	 */
	void rejected(int code);
	
	/**
	 * Notifies the callback that the request was accepted by the owner of
	 * the document. The session to communicate with the joined shared
	 * document.
	 * 
	 * @param session the Session object
	 */
	ParticipantSessionCallback accepted(Session session);
	
}

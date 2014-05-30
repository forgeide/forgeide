/*
 * $Id: DocumentServerLogic.java 1575 2005-11-21 13:34:35Z sim $
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
 * This interface represents the view of the network layer on the object that
 * implements the logic functionality of a document server.
 */
public interface DocumentServerLogic {
		
	/**
	 * Join this editing session. The passed in proxy is used by the server
	 * to communicate with the joining user.
	 *
	 * @param connection a connection for communicating with the joining user
	 */
	void join(ParticipantConnection connection);
			
}

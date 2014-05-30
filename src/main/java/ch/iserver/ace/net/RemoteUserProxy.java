/*
 * $Id: RemoteUserProxy.java 2833 2006-03-22 22:09:37Z sim $
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

import ch.iserver.ace.UserDetails;


/**
 * A RemoteUserProxy is a network layer representation of a remote user. It
 * provides a set of actions available on the user level.
 */
public interface RemoteUserProxy {
	
	/**
	 * Gets the unique identifier of the user.
	 * 
	 * @return the unique identifier
	 */
	String getId();
	
	/**
	 * Gets the display information about the user.
	 * 
	 * @return the UserDetails information
	 */
	UserDetails getUserDetails();
	
}

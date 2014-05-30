/*
 * $Id: RemoteUser.java 2216 2005-12-06 09:04:12Z sim $
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

import java.beans.PropertyChangeListener;


/**
 * A RemoteUser is a local representation of a remote user. It is a proxy
 * providing all the user related operations in one convenient place.
 * The Collaboration Layer makes sure that there is only one RemoteUser
 * object per unique user id. A PropertyChangeListener can be added
 * that gets notified whenever a property of this object changes.
 */
public interface RemoteUser {
	
	/**
	 * The name of the name property.
	 */
	String NAME_PROPERTY = "name";
	
	/**
	 * Gets the unique identifier of the user.
	 * 
	 * @return the unique identifier of the user
	 */
	String getId();
		
	/**
	 * Gets the name of the user.
	 * 
	 * @return the name of the user
	 */
	String getName();
	
	/**
	 * Registers a property change listener on this object.
	 * 
	 * @param listener the property change listener
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * Removes a property change listener from this object.
	 * 
	 * @param listener the listener
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);
	
}

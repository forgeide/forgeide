/*
 * $Id: RemoteUserImpl.java 2271 2005-12-07 11:57:29Z sim $
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.iserver.ace.collaboration.RemoteUser;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.ParameterValidator;

/**
 * Default implementation of the RemoteUser interface. The RemoteUserImpl
 * passes most requests directly to a wrapped RemoteUserProxy instance.
 */
public class RemoteUserImpl implements MutableRemoteUser {
	
	/**
	 * PropertyChangeSupport used for PropertyChangeListener management.
	 */
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	/**
	 * The wrapped target RemoteUserProxy instance.
	 */
	private final RemoteUserProxy proxy;
	
	/**
	 * The nick name of the user.
	 */
	private String name;
	
	/**
	 * Creates a new RemoteUserImpl object.
	 * 
	 * @param proxy the wrapped RemoteUserProxy instance
	 */
	public RemoteUserImpl(RemoteUserProxy proxy) {
		ParameterValidator.notNull("proxy", proxy);
		this.proxy = proxy;
		this.name = proxy.getUserDetails().getUsername();
	}
	
	/**
	 * @return the wrapped RemoteUserProxy instance
	 */
	public RemoteUserProxy getProxy() {
		return proxy;
	}
		
	/**
	 * @see ch.iserver.ace.collaboration.RemoteUser#getId()
	 */
	public String getId() {
		return getProxy().getId();
	}

	/**
	 * @see ch.iserver.ace.collaboration.RemoteUser#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.MutableRemoteUser#setName(java.lang.String)
	 */
	public void setName(String userName) {
		String old = this.name;
		if (!old.equals(userName)) {
			this.name = userName;
			support.firePropertyChange(NAME_PROPERTY, old, userName);
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.RemoteUser#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.RemoteUser#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	/**
	 * Gets an array of all registered property change listeners.
	 * 
	 * @return the array of property change listeners
	 */
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return support.getPropertyChangeListeners();
	}
	
	// --> java.lang.Object methods <--

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof RemoteUser) {
			RemoteUser user = (RemoteUser) obj;
			return getId().equals(user.getId());
		} else {
			return false;
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getId().hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "RemoteUser[id='" + getId() + "',name='" + getName() + "']";
	}
	
}

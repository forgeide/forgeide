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

package ch.iserver.ace.collaboration.jupiter.server;

/**
 * Forwarder built on the Composite design pattern. Child forwarders can
 * be added and removed from this forwarder. A composite forwarder
 * typically just forwards all method invocations to its children, but
 * is free to implement some more complex behavior. For instance a
 * composite forwarder could filter out certain events based on the
 * passed in parameters.
 */
public interface CompositeForwarder extends Forwarder {
	
	/**
	 * Adds a child forwarder to this CompositeForwarder.
	 * 
	 * @param forwarder the child forwarder to be added
	 */
	void addForwarder(Forwarder forwarder);
	
	/**
	 * Removes a child forwarder from this CompositeForwarder.
	 * 
	 * @param forwarder the child forwarder to be removed
	 */
	void removeForwarder(Forwarder forwarder);
	
}

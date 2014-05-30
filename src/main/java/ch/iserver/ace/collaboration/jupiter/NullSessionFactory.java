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

/**
 * Null object for SessionFactory interface.
 */
final class NullSessionFactory implements SessionFactory {
	
	/**
	 * The singleton instance. 
	 */
	private static SessionFactory instance;
	
	/**
	 * Private hidden constructor.
	 */
	private NullSessionFactory() {
		// hidden
	}
	
	/**
	 * Retrieves the singleton instance of this class.
	 * 
	 * @return the singleton instance
	 */
	public static final synchronized SessionFactory getInstance() {
		if (instance == null) {
			instance = new NullSessionFactory();
		}
		return instance;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.SessionFactory#createSession()
	 */
	public ConfigurableSession createSession() {
		return null;
	}

}

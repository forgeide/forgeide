/*
 * $Id: SessionTemplateCallback.java 788 2005-10-28 09:29:49Z sim $
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

package ch.iserver.ace.collaboration.util;

import ch.iserver.ace.collaboration.Session;

/**
 * The SessionTemplateCallback is used in combination with the SessionTemplate.
 * The SessionTemplate takes care of the proper locking/unlocking of the
 * Session. The SessionTemplateCallback implementation can concentrate on the
 * real work that has to be done with sessions.
 * 
 * @see ch.iserver.ace.collaboration.Session
 * @see ch.iserver.ace.collaboration.util.SessionTemplate
 */
public interface SessionTemplateCallback {
	
	/**
	 * Executes this callback's code passing in the session.
	 * 
	 * @param session the passed in Session
	 */
	void execute(Session session);
	
}

/*
 * $Id: SessionTemplate.java 1003 2005-11-07 14:50:52Z sim $
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
 * The SessionTemplate provides a save way to use sessions. It takes care of
 * properly locking/unlocking the Session. The code that needs access to
 * a session creates a SessionTemplateCallback and calls execute on this
 * class. The execute method ensures proper locking/unlocking of the session
 * (even if the session throws an exception) and calls the callback's
 * execute method between lock and unlock.
 * 
 * <p>The following example shows a typical usage of the SessionTemplate
 * class:</p>
 * 
 * <pre>
 *   Session session = ...;
 *   SessionTemplate template = new SessionTemplate(session);
 *   template.execute(new SessionTemplateCallback() {
 *     public void execute(Session session) {
 *       int idx = ...;
 *       String txt = ...;
 *       Operation op = new InsertOperation(idx, txt);
 *       document.insertString(idx, txt);
 *       session.sendOperation(op);
 *     }
 *   });
 * </pre>
 * 
 * @see ch.iserver.ace.collaboration.Session
 * @see ch.iserver.ace.collaboration.util.SessionTemplateCallback
 */
public final class SessionTemplate {
	
	/** 
	 * The Session instance used by this SessionTemplate.
	 */
	private final Session session;
	
	/**
	 * Creates a new SessionTemplate that uses the passed in Session object.
	 * 
	 * @param session the Session object passed to the SessionTemplateCallback
	 */
	public SessionTemplate(Session session) {
		if (session == null) {
			throw new IllegalArgumentException("session cannot be null");
		}
		this.session = session;
	}
	
	/**
	 * Executes the passed in SessionTemplateCallback ensuring proper
	 * locking/unlocking of the Session, even if the template callback
	 * results in an exception.
	 * 
	 * @param callback the SessionTemplateCallback to execute
	 */
	public void execute(SessionTemplateCallback callback) {
		session.lock();
		try {
			callback.execute(session);
		} finally {
			session.unlock();
		}
	}
	
}

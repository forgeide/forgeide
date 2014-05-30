/*
 * $Id: CallerThreadDomain.java 1905 2005-11-29 08:04:40Z sim $
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

package ch.iserver.ace.util;

/**
 * CallerThreadDomain does not create worker threads at all. It does not wrap
 * the target instances either. It simply returns the target from the
 * {@link #wrap(Object, Class)} method and thus method calls on the target
 * are processed on the caller thread.
 * 
 * <p>Note, this might especially be useful in unit testing scenarios where
 * threading introduces a degree of non-deterministic behavior.</p>
 */
public class CallerThreadDomain implements ThreadDomain {
	
	/**
	 * @see ch.iserver.ace.util.ThreadDomain#getName()
	 */
	public String getName() {
		return "caller-thread-domain";
	}
	
	/**
	 * @see ch.iserver.ace.util.ThreadDomain#setName(java.lang.String)
	 */
	public void setName(String name) {
		// ignored
	}
	
	/**
	 * @see ch.iserver.ace.util.ThreadDomain#wrap(java.lang.Object, java.lang.Class)
	 */
	public Object wrap(Object target, Class clazz) {
		return target;
	}
	
	/**
	 * @see ch.iserver.ace.util.ThreadDomain#wrap(java.lang.Object, java.lang.Class, boolean)
	 */
	public Object wrap(Object target, Class clazz, boolean ignoreVoidMethods) {
		return target;
	}
	
	/**
	 * A CallerThreadDomain does not have to be disposed as there are no
	 * worker threads.
	 * 
	 * @see ch.iserver.ace.util.ThreadDomain#dispose()
	 */
	public void dispose() {
		// nothing to dispose
	}

}

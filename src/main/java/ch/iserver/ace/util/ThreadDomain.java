/*
 * $Id: ThreadDomain.java 2043 2005-12-01 14:38:27Z sim $
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
 * ThreadDomain allow to wrap objects so that they are executed on different
 * threads. By using thread domains, it is possible to easily determine
 * the number of threads in a system. A thread domain could create a worker
 * thread for each call to {@link #wrap(Object, Class)} or could use
 * a more conservative approach. It is even possible to choose an 
 * implementation that returns the target object unmodified, thus executing
 * the methods in the thread of the caller.
 */
public interface ThreadDomain {
	
	/**
	 * Sets the name of this ThreadDomain. Names are mainly useful in debug
	 * mode.
	 * 
	 * @param name the name of the thread domain
	 */
	void setName(String name);
	
	/**
	 * Gets the name of this ThreadDomain. The name is mainly useful for
	 * debugging and logging output.
	 * 
	 * @return the name of the domain
	 */
	String getName();
	
	/**
	 * Wraps the <var>target</var> object so that calls to it are executed
	 * in this ThreadDomain.
	 * 
	 * @param target the target object to be wrapped
	 * @param clazz the interface implemented by the target object
	 * @return the proxied target object
	 */
	Object wrap(Object target, Class clazz);
	
	/**
	 * Wraps the <var>target</var> object so that calls to it are executed
	 * in this ThreadDomain. The <var>ignoreNonVoidMethods</var> parameter
	 * determines whether the async interceptor is applied to methods
	 * having a void return type.
	 * 
	 * @param target the target object
	 * @param clazz the interface implemented by the target object
	 * @param ignoreNonVoidMethods do not invoke non-void return typed methods
	 *        asynchronously
	 * @return the wrapped object
	 */
	Object wrap(Object target, Class clazz, boolean ignoreNonVoidMethods);
	
	/**
	 * Disposes this thread domain.
	 */
	void dispose();
	
}

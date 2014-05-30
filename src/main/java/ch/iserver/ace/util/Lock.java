/*
 * $Id: Lock.java 2043 2005-12-01 14:38:27Z sim $
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
 * Simple interface for locks. The interface hides the actual implementation
 * of the lock from the application. A possible implementation could use
 * the ReentrantLocks from the JDK or another
 * lock implementation. Further, the actual implementation can be switched
 * easily without affecting the client code.
 */
public interface Lock {
	
	/**
	 * Determines whether the given thread is holding the lock at the
	 * moment.
	 * 
	 * @param thread the thread
	 * @return true iff the thread is the holder of the lock
	 */
	boolean isOwner(Thread thread);
	
	/**
	 * Grabs the lock.
	 * 
	 * @throws InterruptedRuntimeException if the operation was interrupted
	 */
	void lock() throws InterruptedRuntimeException;
	
	/**
	 * Releases the lock.
	 */
	void unlock();
	
}

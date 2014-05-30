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

package ch.iserver.ace.util;

/**
 *
 */
public class ReentrantLock implements Lock {

	private edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock lock;
	
	public ReentrantLock() {
		this.lock = new edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock();
	}
	
	/**
	 * @see ch.iserver.ace.util.Lock#isOwner(java.lang.Thread)
	 */
	public boolean isOwner(Thread thread) {
		return lock.isHeldByCurrentThread();
	}

	/**
	 * @see ch.iserver.ace.util.Lock#lock()
	 */
	public void lock() {
		lock.lock();
	}

	/**
	 * @see ch.iserver.ace.util.Lock#unlock()
	 */
	public void unlock() {
		lock.unlock();
	}

}

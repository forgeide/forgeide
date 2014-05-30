/*
 * $Id: Worker.java 2371 2005-12-09 08:17:19Z sim $
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

import org.jboss.logging.Logger;

/**
 *
 */
public abstract class Worker extends Thread {
	
	private static final Logger LOG = Logger.getLogger(Worker.class);
	
	private boolean stop;
	
	protected Worker(String name) {
		super(name);
	}
	
	public void kill() {
		stop = true;
		interrupt();
	}
		
	public void run() {
		LOG.info(getName() + " started");
		try {
			while (!stop) {
				try {
					doWork();
				} catch (InterruptedException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			LOG.info("worker " + getName() + " has been interrupted");
		} finally {
			LOG.info(getName() + " terminated");
		}
	}
	
	protected abstract void doWork() throws InterruptedException;
	
}

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

import org.jboss.logging.Logger;

import ch.iserver.ace.util.ParameterValidator;
import edu.emory.mathcs.backport.java.util.concurrent.Future;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * Default implementation of the AcknowledgeStrategy interface.
 * Uses a scheduled executor service to periodically schedule
 * the acknowledge action's execution. Calling the resetTimer method results
 * in a cancelling of the currently scheduled runnable and reschedules
 * it again.
 */
public class AcknowledgeStrategyImpl implements AcknowledgeStrategy, Runnable {

	/**
	 * Logger used by this class.
	 */
	private static final Logger LOG = Logger.getLogger(AcknowledgeStrategyImpl.class);
	
	/**
	 * The executor service used to schedule the execution of the action.
	 */
	private final ScheduledExecutorService executorService;
	
	/**
	 * The delay after which to fire the action.
	 */
	private final int delay;
	
	/**
	 * The number of unacknowledged messages before an acknowledge is sent.
	 */
	private final int threshold;
	
	/**
	 * The number of received messages since the last reset.
	 */
	private int messages = 0;
	
	/**
	 * The action to be executed whenever an acknowledge should take place.
	 */
	private AcknowledgeAction action;
		
	/**
	 * Future object of the currently scheduled execution. This is uesd to
	 * cancel an execution.
	 */
	private Future future;
	
	/**
	 * Creates a new AcknowledgeStrategyImpl class.
	 * 
	 * @param executorService the executor service used to schedule objects
	 * @param delay the delay in seconds from the last reset to the firing of the action
	 */
	public AcknowledgeStrategyImpl(ScheduledExecutorService executorService, int delay, int threshold) {
		ParameterValidator.notNull("executorService", executorService);
		this.executorService = executorService;
		this.delay = delay;
		this.threshold = threshold;
	}
	
	/**
	 * Gets the threshold of unacknowledged messages before this strategy fires
	 * the action.
	 * 
	 * @return the threshold
	 */
	public int getThreshold() {
		return threshold;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AcknowledgeStrategy#messageReceived()
	 */
	public synchronized void messageReceived() {
		messages++;
		if (messages == 1) {
			schedule();
		}
		if (messages == getThreshold()) {
			action.execute();
			reset();
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AcknowledgeStrategy#reset()
	 */
	public synchronized void reset() {
		if (future == null) {
			throw new IllegalStateException("cannot reset unscheduled AcknowledgeManager");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("reset timer of AcknowledgeStrategy " + this);
		}
		messages = 0;
		future.cancel(false);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AcknowledgeStrategy#init(ch.iserver.ace.collaboration.jupiter.AcknowledgeAction)
	 */
	public void init(final AcknowledgeAction action) {
		ParameterValidator.notNull("action", action);
		if (LOG.isDebugEnabled()) {
			LOG.debug("initialize AcknowledgeStrategy " + this);
		}
		this.action = action;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AcknowledgeStrategy#destroy()
	 */
	public synchronized void destroy() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("destroy AcknowledgeStrategy " + this);
		}
		future.cancel(false);
	}
	
	/**
	 * Schedules the execution of the action with the executor service.
	 */
	protected void schedule() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("reschedule execution of AcknowledgeStrategy " + this);
		}
		this.future = executorService.scheduleWithFixedDelay(this, delay, delay, TimeUnit.SECONDS);
	}
	
	/**
	 * Executes the AcknowledgeAction and resets the timer.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("executing acknowledge action: " + this);
		}
		action.execute();
		reset();
	}
	
}

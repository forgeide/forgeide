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
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledExecutorService;

/**
 * Default implementation of the AcknowledgeStrategyFactory interface.
 */
public class AcknowledgeStrategyFactoryImpl implements AcknowledgeStrategyFactory {

	/**
	 * Logger used to create debug output.
	 */
	private static final Logger LOG = Logger.getLogger(AcknowledgeStrategyFactoryImpl.class);
	
	/**
	 * The executor service passed as argument to the AcknowledgeStrategyImpl 
	 * class.
	 */
	private final ScheduledExecutorService executorService;
	
	/**
	 * The delay after which the AcknowledgeAction is executed in seconds. 
	 * Defaults to 60 seconds.
	 */
	private int delay = 60;
	
	/**
	 * The number of unacknowledged messages before an acknowledge action
	 * is executed.
	 */
	private int threshold = 5;
	
	/**
	 * Creates a new AcknowledgeStrategyFactoryImpl instance.
	 * 
	 * @param executorService the executor service used by the strategies to
	 *        schedule executions
	 */
	public AcknowledgeStrategyFactoryImpl(ScheduledExecutorService executorService) {
		ParameterValidator.notNull("executorService", executorService);
		this.executorService = executorService;
	}
	
	/**
	 * Sets the delay in seconds for the strategies.
	 * 
	 * @param delay the new delay
	 */
	public void setDelay(int delay) {
		ParameterValidator.notNegative("delay", delay);
		this.delay = delay;
	}
	
	/**
	 * Gets the delay in seconds for the created strategies.
	 * 
	 * @return the currently set delay
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Sets the threshold of unacknowledged messages before the action is
	 * executed.
	 * 
	 * @param threshold the threshold
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * Gets the threshold of unacknowledged messages before the action is
	 * executed.
	 * 
	 * @return the threshold
	 */
	public int getThreshold() {
		return threshold;
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AcknowledgeStrategyFactory#createStrategy()
	 */
	public AcknowledgeStrategy createStrategy() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("creating AcknowledgeStrategy using delay " + delay + " and threshold " + threshold);
		}
		return new AcknowledgeStrategyImpl(executorService, getDelay(), getThreshold());
	}

}

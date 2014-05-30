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
 * An AcknowledgeStrategy determines how and when an AcknowledgeAction should
 * be executed. An AcknowledgeAction sends an acknowledge message to the
 * other side of a connection. This ensures that outgoing queues in the
 * algorithm implementation do not grow indefinitely when one participant
 * is idle.
 */
public interface AcknowledgeStrategy {
	
	/**
	 * Initializes the acknowledge strategy with an action to be executed
	 * whenever an acknowledge should happen according to the acknowledge
	 * strategy.
	 * 
	 * @param action the action to be executed
	 */
	void init(AcknowledgeAction action);

	/**
	 * Called whenever the other side sent a message. A strategy can use
	 * a threshold of unacknowledged messages to decide to send back an
	 * acknowledge.
	 */
	void messageReceived();
	
	/**
	 * Resets the AcknowledgeStrategy. This method should be called whenever
	 * a message has been sent that makes sending acknowledge messages
	 * pointless.
	 */
	void reset();
	
	/**
	 * Destroys the acknowledge strategy.
	 */
	void destroy();
		
}

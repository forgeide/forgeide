/*
 * $Id:DiscoveryException.java 2413 2005-12-09 13:20:12Z zbinl $
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

package ch.iserver.ace.net.protocol;

/**
 * Exception thrown on discovery error.
 */
public class DiscoveryException extends Exception {

	/**
	 * the exception that caused this exception to be created
	 */
	private Exception reason;
	
	/**
	 *
	 * Creates a new DiscoveryException with the causing
	 * reason exception.
	 *
	 * @param reason		the causing exception
	 */
	public DiscoveryException(Exception reason) {
		this.reason = reason;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message an error message
	 */
	public DiscoveryException(String message) {
		super(message);
	}
	
	/**
	 * Gets the reason.
	 * @return	the reason exception
	 */
	public Exception getReason() {
		return reason;
	}
	
	
}

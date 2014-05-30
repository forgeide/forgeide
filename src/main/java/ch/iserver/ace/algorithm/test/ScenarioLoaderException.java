/*
 * $Id: ScenarioLoaderException.java 2430 2005-12-11 15:17:11Z sim $
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

package ch.iserver.ace.algorithm.test;

/**
 * Exception thrown by scenario loader.
 */
public class ScenarioLoaderException extends ScenarioException {

	/**
	 * 
	 */
	public ScenarioLoaderException() {
		super();
	}

	/**
	 * @param message the exception message
	 */
	public ScenarioLoaderException(String message) {
		super(message);
	}

	/**
	 * @param message the exception message
	 * @param cause the exception cause
	 */
	public ScenarioLoaderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause the exception cause
	 */
	public ScenarioLoaderException(Throwable cause) {
		super(cause);
	}

}

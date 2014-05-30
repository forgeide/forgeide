/*
 * $Id: ApplicationError.java 2019 2005-12-01 10:00:48Z sim $
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

package ch.iserver.ace;

/**
 * Error to be thrown in cases where it does not make sense to let the
 * application running. Typical reasons to throw this application are
 * missing configuration files that must be present in each application.
 */
public class ApplicationError extends Error {

	/**
	 * Creates a new ApplicationError instance.
	 */
	public ApplicationError() {
		super();
	}

	/**
	 * Creates a new ApplicationError instance.
	 * 
	 * @param message the detail message
	 */
	public ApplicationError(String message) {
		super(message);
	}

	/**
	 * Creates a new ApplicationError instance.
	 * 
	 * @param cause the cause of the ApplicationError
	 */
	public ApplicationError(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new ApplicationError instance.
	 * 
	 * @param message the detail message
	 * @param cause the cause of the ApplicationError
	 */
	public ApplicationError(String message, Throwable cause) {
		super(message, cause);
	}

}

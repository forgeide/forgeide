/*
 * $Id: ParameterValidator.java 2825 2006-03-20 19:01:36Z sim $
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
 * Utility class for validation of method parameters.
 */
public final class ParameterValidator {
	
	private ParameterValidator() {
		// do nothing
	}
	
	/**
	 * Validates that the parameter value with the given <var>name</var> is not
	 * null. 
	 * 
	 * @param name the name of the parameter
	 * @param value the value of the parameter
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public static void notNull(String name, Object value) {
		if (value == null) {
			throw new IllegalArgumentException(name + " cannot be null");
		}
	}
	
	/**
	 * Validates that the parameter value is not negative.
	 * 
	 * @param name the name of the parameter
	 * @param value the value of the parameter
	 * @throws IllegalArgumentException if the value is negative
	 */
	public static void notNegative(String name, int value) {
		if (value < 0) {
			throw new IllegalArgumentException(name + " cannot be negative");
		}
	}
	
	/**
	 * Validates that the parameter is in the given range.
	 * 
	 * @param name the name of the parameter
	 * @param value the value of the parameter
	 * @param min the minimum value (inclusive)
	 * @param max the maximum value (inclusive)
	 */
	public static void inRange(String name, int value, int min, int max) {
		if (!(min <= value && value <= max)) {
			throw new IllegalArgumentException(name + " is not in range.");
		}
	}
	
	public static void isTrue(String name, boolean value) {
		if (!value) {
			throw new IllegalArgumentException(name + " not valid");
		}
	}
	
}

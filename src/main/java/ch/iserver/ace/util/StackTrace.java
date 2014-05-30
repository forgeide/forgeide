/*
 * $Id: StackTrace.java 2716 2006-02-11 18:24:25Z zbinl $
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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import ch.iserver.ace.net.core.NetworkProperties;

/**
 * Helper class to get the stack trace from various sources.
 */
public class StackTrace {

	/**
	 * Returns the stack trace of Exception <code>e</code> as a String.
	 * 
	 * @param e	the exception to get the stack trace
	 * @return	String	the stack trace
	 */
	public static String get(Exception e) {
		ByteArrayOutputStream trace = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(trace);
		e.printStackTrace(pw);
		pw.close();
		String str = ""; 
		try {
			str = new String(trace.toByteArray(), NetworkProperties.get(NetworkProperties.KEY_DEFAULT_ENCODING));
		} catch (Exception ex) {}
		return str;
	}
	
}

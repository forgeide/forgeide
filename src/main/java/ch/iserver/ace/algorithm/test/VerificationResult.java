/*
 * $Id: VerificationResult.java 2430 2005-12-11 15:17:11Z sim $
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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Result holder class for verification results. Call 
 * {@link #verify()} if you want the old style behavior of throwing
 * exceptions if there is at least one failure.
 */
public class VerificationResult {

	/** list of successful verifications. */
	private List success;
	
	/** list of failed verifications */
	private List failure;
	
	/**
	 * Creates a new VerificationResult.
	 */
	public VerificationResult() { 
		this.success = new LinkedList();
		this.failure = new LinkedList();
	}
	
	/**
	 * Adds a successful verification to the result.
	 * 
	 * @param siteId the site id
	 * @param state the verified state
	 */
	public void addSuccess(String siteId, String state) {
		success.add(new Success(siteId, state));
	}
	
	/**
	 * Adds a failed verification to the result.
	 * 
	 * @param siteId the site id
	 * @param expected the expected document state
	 * @param actual the actual document state
	 */
	public void addFailure(String siteId, String expected, String actual) {
		failure.add(new Failure(siteId, expected, actual));
	}
	
	/**
	 * @return the list of successful verifications
	 */
	public List getSuccesses() {
		return Collections.unmodifiableList(success);
	}
	
	/**
	 * @return the list of failed verifications
	 */
	public List getFailures() {
		return Collections.unmodifiableList(failure);
	}
	
	/**
	 * Verifies the result set. If there is at least one failure,
	 * this method throws a VerificationException.
	 *
	 * @throws VerificationException if there is at least one failure
	 */
	public void verify() throws VerificationException {
		if (failure.size() > 0) {
			throw new VerificationException(this);
		}
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(failure.size());
		buf.append(" of ");
		buf.append(success.size() + failure.size());
		buf.append(" verifications failed:\n");
		Iterator it = failure.iterator();
		while (it.hasNext()) {
			Failure next = (Failure) it.next();
			buf.append("- ");
			buf.append(next);
			if (it.hasNext()) {
				buf.append("\n");
			}
		}
		return buf.toString();
	}
	
	/**
	 * Represents a successful verification.
	 */
	public static final class Success {
		private final String siteId;
		private final String state;
		public Success(String siteId, String state) {
			this.siteId = siteId;
			this.state = state;
		}
		public String getSiteId() {
			return siteId;
		}
		public String getState() {
			return state;
		}
		public String toString() {
			return "success @ site " + siteId + ": state='" + state + "'";
		}
	}

	/**
	 * Represents a failed verification.
	 */
	public static final class Failure {
		private final String siteId;
		private final String expected;
		private final String actual;
		public Failure(String siteId, String expected, String actual) {
			this.siteId = siteId;
			this.expected = expected;
			this.actual = actual;
		}
		public String getActual() {
			return actual;
		}
		public String getExpected() {
			return expected;
		}
		public String getSiteId() {
			return siteId;
		}
		public String toString() {
			return "failure @ site " + siteId + ": expected '" + expected
					+ "' but was '" + actual + "'";
		}
	}
}

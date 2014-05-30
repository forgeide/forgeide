/*
 * $Id: DiscoveryResult.java 2217 2005-12-06 09:11:03Z sim $
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

package ch.iserver.ace.collaboration;

import java.net.InetAddress;

import ch.iserver.ace.util.CompareUtil;

/**
 * A DiscoveryResult object contains the result of an explicit
 * discovery request through the method
 * {@link ch.iserver.ace.collaboration.CollaborationService#discoverUser(DiscoveryCallback, InetAddress, int)}.
 * The status specifies whether the discovery was successful or not.
 */
public final class DiscoveryResult {
	
	/**
	 * The status code that signifies a successful discovery. 
	 */
	public static final int SUCCESS = 0;
		
	/**
	 * The status code of the discovery.
	 */
	private int status = SUCCESS;
	
	/**
	 * The status message of the discovery (especially interesting if the
	 * discovery failed).
	 */
	private String statusMessage = "OK";
	
	/**
	 * Creates a new DiscoveryResult for a successful discovery.
	 * 
	 * @param status the discovery status
	 */
	public DiscoveryResult(int status) {
		this.status = status;
	}
	
	/**
	 * Creates a new DiscoveryResult for a failed discovery.
	 * 
	 * @param status the status code
	 * @param message the status message
	 */
	public DiscoveryResult(int status, String message) {
		this.status = status;
		this.statusMessage = message;
	}
	
	/**
	 * Determines whether the discovery succeeded.
	 * 
	 * @return true iff the discovery was successful
	 */
	public boolean isSuccess() {
		return status == SUCCESS;
	}
		
	/**
	 * The status code of the discovery. Unless it is equal to
	 * <code>DiscoveryResult.SUCCESS</code> the discovery failed.
	 * 
	 * @return the status code
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * An optional status message describing the cause of the failure.
	 * 
	 * @return the status message
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass().equals(obj.getClass())) {
			DiscoveryResult result = (DiscoveryResult) obj;
			return status == result.status 
					&& CompareUtil.nullSafeEquals(statusMessage, result.statusMessage);
		} else {
			return false;
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashCode = status;
		hashCode += statusMessage == null ? 0 : 13 * statusMessage.hashCode();
		return hashCode;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getName() + "[status=" + status + ",message='" + statusMessage + "']";
	}
	
}

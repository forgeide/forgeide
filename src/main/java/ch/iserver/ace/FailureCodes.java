/*
 * $Id: FailureCodes.java 2399 2005-12-09 12:04:17Z zbinl $
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
 * Interface that contains constants that define failure codes passed to
 * the {@link ch.iserver.ace.collaboration.ServiceFailureHandler}.
 */
public interface FailureCodes {
	
	/*********************************************************/
	/** Failure codes for network layer between 100 and 199 **/
	/*********************************************************/
	public static final int CONNECTION_REFUSED = 100;
	public static final int CHANNEL_FAILURE = 101;
	//if a BindException occurs (address already in use)
	public static final int ADDRESS_ALREADY_USED = 102;
	public static final int SESSION_FAILURE = 103;
	
	public static final int DISCOVERY_FAILED = 104;
	
	public static final int DNSSD_NOT_AVAILABLE = 105;
	public static final int DNSSD_FAILURE = 106;
	
	public static final int REMOTE_USER_FAILURE = 107;
	
	
}

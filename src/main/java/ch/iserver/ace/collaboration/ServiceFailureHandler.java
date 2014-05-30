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

package ch.iserver.ace.collaboration;

/**
 * A ServiceFailureHandler can be registered with the collaboration layer
 * in order to receive service failure events. Beside of potential
 * service failure events from the collaboration layer, events from
 * the network layer are also passed to the ServiceFailureHandler.
 */
public interface ServiceFailureHandler {
	
	/**
	 * Notifies the failure handler that a service failure occured. The 
	 * error codes are defined in the {@link ch.iserver.ace.FailureCodes}
	 * interface.
	 * 
	 * @param code the error code
	 * @param msg an optional message
	 * @param e an exception
	 */
	void serviceFailed(int code, String msg, Exception e);
	
}

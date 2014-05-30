/*
 * $Id: ReceptionNode.java 2430 2005-12-11 15:17:11Z sim $
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

import ch.iserver.ace.algorithm.Request;

/**
 * This interface represents a node that receives requests. The
 * requests are received by a call to {@link #setRequest(String, Request)}.
 */
public interface ReceptionNode extends Node {

	/**
	 * Sets the request to be received by this node.
	 * 
	 * @param participantId the id of the participant
	 * @param request the request to be received
	 */
	public void setRequest(String participantId, Request request);

	/**
	 * Gets the request to be received by this node.
	 * 
	 * @return the request to be received
	 */
	public Request getRequest();
	
	/**
	 * Gets the referenced operation id of this node.
	 * 
	 * @return the operation id
	 */
	public String getReference();

}

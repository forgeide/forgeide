/*
 * $Id: SimpleReceptionNode.java 2430 2005-12-11 15:17:11Z sim $
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

import java.util.ArrayList;
import java.util.List;

import ch.iserver.ace.algorithm.Request;

/**
 * A reception node represents the reception of a remote request. Such a node
 * must have the following properties.
 * 
 * <ul>
 * <li>at most one local successor</li>
 * <li>exactly two predecessor, one local the other remote</li>
 * </ul>
 */
public class SimpleReceptionNode extends AbstractNode implements ReceptionNode {
	
	/** the request's participant. */
	private String senderParticipantId;
	
	/** the request to process. */
	private Request request;

	/** the reference to the operation. */
	private final String ref;

	/**
	 * Creates a new reception node belonging to the given site and referencing
	 * the given operation.
	 * 
	 * @param participantId
	 *            the id of the site this node belongs to
	 * @param ref
	 *            the reference to the received operation
	 */
	public SimpleReceptionNode(String participantId, String ref) {
		super(participantId);
		this.ref = ref;
	}
	
	public String getSenderParticipantId() {
		return senderParticipantId;
	}
	
	/**
	 * Gets the reference to the operation that is to be received by this node.
	 * 
	 * @return the reference to the operation
	 */
	public String getReference() {
		return ref;
	}

	/**
	 * @see ch.iserver.ace.algorithm.test.ReceptionNode#setRequest(String, Request)
	 */
	public void setRequest(String participantId, Request request) {
		this.senderParticipantId = participantId;
		this.request = request;
	}

	/**
	 * @see ch.iserver.ace.algorithm.test.ReceptionNode#getRequest()
	 */
	public Request getRequest() {
		return request;
	}

	public List getSuccessors() {
		List result = new ArrayList();
		if (getLocalSuccessor() != null) {
			result.add(getLocalSuccessor());
		}
		return result;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return getClass().getName() + "[site=" + getParticipantId() + ",ref=" + ref
				+ "]";
	}

}

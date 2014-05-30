/*
 * $Id: RelayNode.java 2430 2005-12-11 15:17:11Z sim $
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.iserver.ace.algorithm.Request;

/**
 * Node implementation that represents a relay event on the server. The
 * server receives operations from the clients and then relays it to the
 * clients (after appropriate transformations have been done). This node
 * exactly represents that event.
 */
public class RelayNode extends AbstractNode implements ReceptionNode, GenerationNode {
	/** the incoming request */
	private Request request;
	/** the incoming request's participant */
	private String senderParticipantId;
	/** the referenced operation */
	private String reference;
	/** the id of this generation event */
	private String eventId;
	/** set of remote successors */
	private Set remoteSuccessors;
	/** predecessor of this node */
	private Node localPredecessor;
	
	/**
	 * Creates a new relay node residing at the given site, referencing the 
	 * given event (receiving side) and having the given event id.
	 * 
	 * @param siteId the site id of the site this node resides on
	 * @param reference the referenced event
	 * @param id the event id
	 */
	public RelayNode(String siteId, String reference, String id) {
		super(siteId);
		this.reference = reference;
		this.eventId = id;
		this.remoteSuccessors = new HashSet();
	}
	
	public String getSenderParticipantId() {
		return senderParticipantId;
	}
	
	public String getEventId() {
		return eventId;
	}
	
	public String getReference() {
		return reference;
	}
	
	public Request getRequest() {
		return request;
	}
	
	public void setRequest(String participantId, Request request) {
		this.senderParticipantId = participantId;
		this.request = request;
	}
	
	/**
	 * Gets the predecessor of this node or null if there is none.
	 * 
	 * @return the predecessor of this node
	 */
	public Node getLocalPredecessor() {
		return localPredecessor;
	}

	/**
	 * Sets the predecessor of this node to the given object.
	 * 
	 * @param predecessor the new predecessor of this node
	 */
	public void setLocalPredecessor(Node predecessor) {
		this.localPredecessor = predecessor;
	}

	public void addRemoteSuccessor(ReceptionNode successor) {
		remoteSuccessors.add(successor);
	}
	
	public Set getRemoteSuccessors() {
		return remoteSuccessors;
	}
	
	public List getSuccessors() {
		List result = new ArrayList();
		if (getLocalSuccessor() != null) {
			result.add(getLocalSuccessor());
		}
		result.addAll(remoteSuccessors);
		return result;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * @return a string representation of this node
	 */
	public String toString() {
		return getClass().getName() + "["
				+ "siteId=" + getParticipantId()
				+ ",eventId=" + getEventId()
				+ ",ref=" + getReference()
				+ "]";
	}

}

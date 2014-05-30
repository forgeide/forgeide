/*
 * $Id: AbstractGenerationNode.java 2430 2005-12-11 15:17:11Z sim $
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

/**
 * An abstract base class for all classes that generate an
 * operation. Subclasses include nodes for doing, undoing and redoing
 * operations.
 */
public abstract class AbstractGenerationNode extends AbstractNode 
		implements GenerationNode {
	
	/** list of remote successors (nodes that receive from this node). */
	private Set remoteSuccessors = new HashSet();
	
	/** the event id. */
	private final String eventId;

	/**
	 * Creates a new node.
	 * 
	 * @param participantId the site id
	 * @param eventId the generation event id
	 */
	protected AbstractGenerationNode(String participantId, String eventId) {
		super(participantId);
		this.eventId = eventId;
	}
	
	/**
	 * @return the event id
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * Adds a node as remote successor of this node.
	 * 
	 * @param successor
	 *            the remote successor (a reception node)
	 */
	public void addRemoteSuccessor(ReceptionNode successor) {
		remoteSuccessors.add(successor);
	}

	/**
	 * Gets the list of remote successors from this node.
	 * 
	 * @return the list of remote successors
	 */
	public Set getRemoteSuccessors() {
		return remoteSuccessors;
	}

	public List getSuccessors() {
		List result = new ArrayList();
		if (getLocalSuccessor() != null) {
			result.add(getLocalSuccessor());
		}
		result.addAll(getRemoteSuccessors());
		return result;
	}

}

/*
 * $Id: Node.java 2430 2005-12-11 15:17:11Z sim $
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

import java.util.List;


/**
 * The node interface represents a node in a scenario graph. Nodes
 * represent characteristic events in such a graph. So there are
 * nodes for reception/generation of messages as well as nodes
 * that represent the start and end of a site lifecycle.
 */
public interface Node {
	
	/**
	 * Gets the id of the participant this node belongs to.
	 * 
	 * @return the participant id
	 */
	public String getParticipantId();
	
	/**
	 * Gets a list of successors of this node. This includes local
	 * successors (i.e. from the same site) as well as in some cases
	 * remote successors. 
	 * <p>Note that the return value is never null. If there are no
	 * successors, implementors should return an empty list.</p>
	 * 
	 * @return the list of successors
	 */
	public List getSuccessors();
	
	/**
	 * Gets the local successor of this node. There is never more than
	 * one local successors.
	 * 
	 * @return the local successor
	 */
	public Node getLocalSuccessor();
	
	/**
	 * Sets the local successor of this node. The old successor is
	 * replaced by the new one.
	 * 
	 * @param successor the new local successor
	 */
	public void setLocalSuccessor(Node successor);
	
	/**
	 * Accepts the given node visitor. This is used for double dispatching.
	 * 
	 * @param visitor the visitor that wishes to visit this node
	 */
	public void accept(NodeVisitor visitor);
	
}

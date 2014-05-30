/*
 * $Id: StartNode.java 2430 2005-12-11 15:17:11Z sim $
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


/**
 * Node implementation that represents the start of the site lifecycle
 * in a scenario. A start node should never have any predecessors
 * although that condition is not ensured by the implementation.
 */
public class StartNode extends AbstractNode {
	/** how many sites where generated before this one. */
	private final int sites;
	/** the initial state of the document at the site. */
	private final String state;
	
	/**
	 * Creates a new start node belonging to the given site whose initial
	 * state is given by <var>initialState</var>.
	 * 
	 * @param siteId  the id of the site this node belongs to
	 * @param initialState  the initial state at the site
	 * @param sites  the number of sites generated prior to this site
	 */
	public StartNode(String siteId, String initialState, int sites) {
		super(siteId);
		this.state = initialState;
		this.sites = sites;
	}
	
	/**
	 * Gets the initial state at the local site.
	 * 
	 * @return the initial state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Gets the number of sites generated before this one.
	 * 
	 * @return the number of sites generated before this one
	 */
	public int getSites() {
		return sites;
	}
	
	/**
	 * Gets a list of successors. A start node has zero or one
	 * successor. If there is a local successor, it is the only
	 * successor of this node.
	 * 
	 * @return the list of successors (at most one)
	 */
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
		return getClass().getName() + "[site=" + getParticipantId()
				+ ",state=" + state 
				+ "]";
	}
	
}

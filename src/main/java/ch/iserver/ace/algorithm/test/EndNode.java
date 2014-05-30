/*
 * $Id: EndNode.java 2430 2005-12-11 15:17:11Z sim $
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
 * Node implementation that represents the end of a site lifecycle in
 * a scenario. An end node does not have any successors. That is 
 * {@link #setLocalSuccessor(Node)} is unsupported and 
 * {@link ch.iserver.ace.algorithm.test.Node#getLocalSuccessor()} returns always null.
 */
public class EndNode extends AbstractNode {
	/** the expected final state at the site. */
	private String state;
	
	/**
	 * Creates a new end node belonging to the given site and expecting the
	 * given finalt <var>state</var>.
	 * 
	 * @param siteId the site this operation belongs to
	 * @param state the expected final state
	 */
	public EndNode(String siteId, String state) {
		super(siteId);
		this.state = state;
	}
	
	/**
	 * Gets the expected final state at the site.
	 * 
	 * @return the expected final state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Prohibits setting the local successor by throwing a
	 * <code>UnsupportedOperationException</code>.
	 * 
	 * @param successor the succesor node
	 */
	public void setLocalSuccessor(Node successor) {
		throw new UnsupportedOperationException("EndNode has no successors");
	}
	
	/**
	 * Returns an empty list as an end node never has any successor.
	 * 
	 * @return an empty list
	 */
	public List getSuccessors() {
		return new ArrayList();
	}
	
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
	
	public String toString() {
		return getClass().getName() + "[siteId=" + getParticipantId()
				+ ",state='" + state
				+ "']";
	}

}

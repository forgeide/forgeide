/*
 * $Id: VerificationNode.java 2430 2005-12-11 15:17:11Z sim $
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
 * A VerificationNode can be used to verify the document state of
 * a site at an arbitrary time in the lifecycle of a site. 
 */
public class VerificationNode extends AbstractNode {
	/** the expected state at the site. */
	private final String state;

	/**
	 * Creates a new VerificatioNode for the given site that expects
	 * the given state.
	 * 
	 * @param siteId the site of this node
	 * @param state the expected state
	 */
	public VerificationNode(String siteId, String state) {
		super(siteId);
		this.state = state;
	}
	
	/**
	 * Gets the expected state at the place where this node is.
	 * 
	 * @return the expected state
	 */
	public String getState() {
		return state;
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
		return getClass().getName() + "["
		        + "site=" + getParticipantId()
		        + "state='" + state + "'"
		        + "]";
	}

}

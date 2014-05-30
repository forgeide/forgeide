/*
 * $Id: RedoNode.java 2430 2005-12-11 15:17:11Z sim $
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


/**
 * Node implementation that represents the generation of a redo operation.
 * This is a generation node. The only difference to a DoNode is that
 * this node does not need any operation. The operation to be redone
 * is explicitely given (the algorithm implementation knows about it).
 */
public class RedoNode extends AbstractGenerationNode {
	
	/**
	 * Creates a new redo node for the given site. The id is used to
	 * link this event to its successors (i.e. the receivers).
	 * 
	 * @param siteId the site id of the site where this node resides
	 * @param id the id of the vent
	 */
	public RedoNode(String siteId, String id) {
		super(siteId, id);
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
				+ "]";
	}

}

/*
 * $Id: Scenario.java 2430 2005-12-11 15:17:11Z sim $
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

import java.util.Collection;
import java.util.Iterator;

/**
 * This class represents a scenerio. It contains the scenario graph.
 */
public class Scenario {
	/** the initial state of the scenario. */
	private String initialState;
	/** the expected final state of the scenario. */
	private String finalState;
	/** the collection of nodes. */
	private Collection nodes;
	
	/**
	 * Creates a new scenario with the given initial state, final state
	 * and graph of nodes.
	 * 
	 * @param initialState the initial state
	 * @param finalState the expected final state
	 * @param nodes a collection of nodes
	 */
	public Scenario(String initialState, String finalState, Collection nodes) { 
		this.initialState = initialState;
		this.finalState = finalState;
		this.nodes = nodes;
	}
	
	/**
	 * Gets the initial state of the scenario.
	 *
	 * @return the initial state
	 */
	public String getInitialState() {
		return initialState;
	}
	
	/**
	 * Gets the final state of the scenario.
	 *
	 * @return the final state
	 */
	public String getFinalState() {
		return finalState;
	}
	
	/**
	 * Accepts the given node visitor that gets called back
	 * for each node in the graph. The callbacks occur in 
	 * topological order.
	 *
	 * @param visitor the node visitor that visits the nodes
	 */
	public void accept(NodeVisitor visitor) {
		Iterator it = nodes.iterator();
		while (it.hasNext()) {
			Node node = (Node) it.next();
			node.accept(visitor);
		}
	}
	
}

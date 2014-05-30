/*
 * $Id: DefaultScenarioBuilder.java 2430 2005-12-11 15:17:11Z sim $
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ch.iserver.ace.algorithm.Operation;

/**
 * The default scenario builder implementation creates a scenario object.
 */
public class DefaultScenarioBuilder implements ScenarioBuilder {

	/** the initial state of the scenario. */
	private String initialState;

	/** the final state of the scenario. */
	private String finalState;

	/** the current site id. */
	private String currentSiteId;

	/** 
	 * map of local predecessor (maps site ids to the last node at the site). 
	 */
	private Map localPredecessors;

	/** map of site helper objects (maps site ids to SiteHelper objects). */
	private Map siteHelpers;

	/** site helper object for server. */
	private SiteHelper serverHelper;

	/** map of generation nodes (maps operation ids to generation nodes). */
	private Map generationNodes;

	/** list of reception nodes. */
	private List receptionNodes;

	/** set of all nodes. */
	private Set nodes;

	/** the number of sites generated so far. */
	private int siteCount;

	public void init(String initialState, String finalState) {
		this.initialState = initialState;
		this.finalState = finalState;
		this.localPredecessors = new HashMap();
		this.siteHelpers = new HashMap();
		this.serverHelper = new SiteHelper();
		this.generationNodes = new HashMap();
		this.receptionNodes = new ArrayList();
		this.nodes = new HashSet();
		this.siteCount = 0;
	}

	/**
	 * @param node add node to set of nodes
	 */
	protected void addNode(Node node) {
		nodes.add(node);
	}

	/**
	 * @return the set of all nodes
	 */
	protected Set getNodes() {
		return nodes;
	}

	/**
	 * @param node adds the generation node
	 */
	protected void addGenerationNode(GenerationNode node) {
		addNode(node);
		Node pred = getPredecessor(node.getParticipantId());
		pred.setLocalSuccessor(node);
		addGeneratedOperation(node.getEventId(), node);
		setPredecessor(node.getParticipantId(), node);
		addToSiteGeneration(node.getParticipantId(), node.getEventId());
	}

	/**
	 * @param node adds the reception node
	 */
	protected void addReceptionNode(ReceptionNode node) {
		addNode(node);
		Node pred = getPredecessor(node.getParticipantId());
		pred.setLocalSuccessor(node);
		receptionNodes.add(node);
		setPredecessor(node.getParticipantId(), node);
		addToSiteReception(node.getParticipantId(), node.getReference());
	}

	public void startSite(String siteId) {
		if (this.currentSiteId != null) {
			throw new ScenarioException("sites cannot nest");
		}
		this.currentSiteId = siteId;
		StartNode node = new StartNode(siteId, initialState, siteCount++);
		addNode(node);
		setPredecessor(siteId, node);
		siteHelpers.put(siteId, new SiteHelper());
	}

	public void addDoGeneration(final String id, final Operation op) {
		checkCurrentSite();
		addGenerationNode(new DoNode(currentSiteId, id, op));
	}

	public void addUndoGeneration(String id) {
		checkCurrentSite();
		addGenerationNode(new UndoNode(currentSiteId, id));
	}

	public void addRedoGeneration(String id) {
		checkCurrentSite();
		addGenerationNode(new RedoNode(currentSiteId, id));
	}

	public void addReception(String ref) {
		checkCurrentSite();
		addReceptionNode(new SimpleReceptionNode(currentSiteId, ref));
	}

	public void addVerification(String expect) {
		checkCurrentSite();
		Node node = new VerificationNode(currentSiteId, expect);
		addNode(node);
		Node pred = getPredecessor(currentSiteId);
		pred.setLocalSuccessor(node);
		setPredecessor(currentSiteId, node);
	}

	public void endSite() {
		checkCurrentSite();
		Node node = new EndNode(currentSiteId, finalState);
		addNode(node);
		Node pred = getPredecessor(currentSiteId);
		pred.setLocalSuccessor(node);
		setPredecessor(currentSiteId, null);
		currentSiteId = null;
	}

	private void checkCurrentSite() {
		if (currentSiteId == null) {
			throw new ScenarioException("no previous startSite call");
		}
	}

	public void addRelay(String ref, String id) {
		if (ref == null) {
			throw new IllegalArgumentException(
					"operation reference cannot be null");
		}
		if (id == null) {
			throw new IllegalArgumentException("id cannot be null");
		}
		RelayNode node = new RelayNode("server", ref, id);
		addNode(node);
		// 1) reception
		receptionNodes.add(node);
		serverHelper.addReception(node.getReference());
		// 2) local successors
		Node pred = getPredecessor(node.getParticipantId());
		if (pred != null) {
			pred.setLocalSuccessor(node);
			node.setLocalPredecessor(pred);
		}
		setPredecessor(node.getParticipantId(), node);
		// 3) generation
		addGeneratedOperation(id, node);
		serverHelper.addGeneration(node.getEventId());
	}

	/**
	 * Finishes the build process and returns the scenario object.
	 * 
	 * @return the built scenario object
	 */
	public Scenario getScenario() {
		// 1) create 'cross-site' edges
		Iterator it = getReceptionNodes().iterator();
		while (it.hasNext()) {
			ReceptionNode target = (ReceptionNode) it.next();
			String ref = target.getReference();
			GenerationNode source = getGenerationNode(ref);
			source.addRemoteSuccessor(target);
		}

		// 2) validate graph
		List nodes = validateDAG();

		// 3) create result
		return new Scenario(initialState, finalState, nodes);
	}

	/**
	 * Validates that all the operations form a directed acyclic graph and
	 * returns a topologically ordered list of nodes.
	 * 
	 * @return a topologically ordered list of nodes
	 * @throws ScenarioException
	 *             if the graph is not a DAG
	 */
	protected List validateDAG() {
		return GraphUtil.topologicalSort(getNodes());
	}

	// --> internal helper methods <--

	private Node getPredecessor(String siteId) {
		return (Node) localPredecessors.get(siteId);
	}

	private void setPredecessor(String siteId, Node node) {
		localPredecessors.put(siteId, node);
	}

	private Collection getReceptionNodes() {
		return receptionNodes;
	}

	private GenerationNode getGenerationNode(String id) {
		return (GenerationNode) generationNodes.get(id);
	}

	private void addGeneratedOperation(String ref, Node node) {
		if (isGenerated(ref)) {
			throw new ScenarioException("operation " + ref
					+ " already generated");
		}
		generationNodes.put(ref, node);
	}

	private boolean isGenerated(String id) {
		return generationNodes.containsKey(id);
	}

	private void addToSiteGeneration(String siteId, String ref) {
		SiteHelper helper = getSiteHelper(siteId);
		helper.addGeneration(ref);
	}

	private void addToSiteReception(String siteId, String ref) {
		SiteHelper helper = getSiteHelper(siteId);
		helper.addReception(ref);
	}

	private SiteHelper getSiteHelper(String siteId) {
		return (SiteHelper) siteHelpers.get(siteId);
	}

	// --> internal helper classes <--

	/**
	 * Basic helper object for site related checks.
	 */
	private static class SiteHelper {
		private Set generated = new TreeSet();

		private Set received = new TreeSet();

		private void addGeneration(String id) {
			generated.add(id);
		}

		private void addReception(String ref) {
			if (generated.contains(ref)) {
				throw new ScenarioException("cannot receive local operation");
			}
			if (received.contains(ref)) {
				throw new ScenarioException(
						"cannot receive the same operation twice");
			}
			received.add(ref);
		}
	}

}

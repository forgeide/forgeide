/*
 * $Id: ExecuteVisitor.java 2430 2005-12-11 15:17:11Z sim $
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jboss.logging.Logger;

import ch.iserver.ace.algorithm.Algorithm;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.TransformationException;

/**
 * An ExecuteVisitor is a special node visitor implementation that executes a
 * scenario. The ExecuteVistor makes use of a
 * {@link ch.iserver.ace.algorithm.test.AlgorithmTestFactory} to create algorithms,
 * timestamps and documents. ExecuteVisitor should only be used on a sequence of
 * nodes that is correctly ordered (e.g. operations are generated before they
 * are received).
 * <p>
 * A {@link ch.iserver.ace.algorithm.test.Scenario} contains such an ordered sequence of
 * nodes. Use {@link ch.iserver.ace.algorithm.test.Scenario#accept(NodeVisitor)} to visit
 * this sequence of nodes.
 * </p>
 */
public class ExecuteVisitor implements NodeVisitor {
	/** logger class. */
	private static final Logger LOG = Logger.getLogger(ExecuteVisitor.class);
	
	/** the factory used to create the needed objects. */
	private AlgorithmTestFactory factory;

	/** map from site ids to algorithms. */
	private Map algorithms;
	
	/** map from site ids to documents. */
	private Map documents;
	
	/** verification results */
	private VerificationResult verificationResult;

	/**
	 * Creates a new execute visitor using the given <var>factory</var>.
	 * 
	 * @param factory
	 *            the factory to create needed components
	 */
	public ExecuteVisitor(AlgorithmTestFactory factory) {
		this.factory = factory;
		this.algorithms = new HashMap();
		this.documents = new HashMap();
		this.verificationResult = new VerificationResult();
	}
	
	/**
	 * Gets the verification result at the end of the execution.
	 * 
	 * @return the verification result
	 */
	public VerificationResult getVerificationResult() {
		return verificationResult;
	}
	
	/**
	 * Adds a failure to the verification result.
	 * 
	 * @param siteId the site id of the failing verification
	 * @param expected the expected document state
	 * @param actual the actual document state
	 */
	protected void addFailure(String siteId, String expected, String actual) {
		verificationResult.addFailure(siteId, expected, actual);
	}
	
	/**
	 * Adds a success to the verification result.
	 * 
	 * @param siteId the site id of the successful verification
	 * @param state the state at the site
	 */
	protected void addSuccess(String siteId, String state) {
		verificationResult.addSuccess(siteId, state);
	}
	
	protected void apply(String siteId, Operation op) {
		Document doc = getDocument(siteId);
		doc.apply(op);
	}

	/**
	 * Gets the algorithm test factory, which is needed to create algorithms,
	 * timestamps and documents.
	 * 
	 * @return the algorithm test factory
	 */
	public AlgorithmTestFactory getFactory() {
		return factory;
	}
	
	/**
	 * Visits a start node. It initializes the algorithm at the site represented
	 * by this node.
	 * 
	 * @param node
	 *            the node to visit
	 */
	public void visit(StartNode node) {
		LOG.info("visit: " + node);
		String state = node.getState();
		setDocument(node.getParticipantId(), new DefaultDocument(state));
		Algorithm algorithm = getFactory().createAlgorithm(
				Integer.parseInt(node.getParticipantId()), null);
		setAlgorithm(node.getParticipantId(), algorithm);
	}
	
	/**
	 * Visits a generation node. It passes the stored operation to the algorithm
	 * to create a request. The request is then set on all the remote successor
	 * nodes (i.e. reception nodes).
	 * 
	 * @param node
	 *            the node to visit
	 */
	public void visit(DoNode node) {
		LOG.info("visit: " + node);
		Operation op = node.getOperation();		
		apply(node.getParticipantId(), op);
		Algorithm algo = getAlgorithm(node.getParticipantId());
		Request request = algo.generateRequest(op);
		Iterator it = node.getRemoteSuccessors().iterator();
		while (it.hasNext()) {
			ReceptionNode remote = (ReceptionNode) it.next();
			remote.setRequest(node.getParticipantId(), request);
		}
	}

	/**
	 * Visits an undo node. Calls undo on the local algorithm to get a request
	 * that is then sent to all remote successors.
	 * 
	 * @param node the node to visit
	 */
	public void visit(UndoNode node) {
		LOG.info("visit: " + node);
		Algorithm algo = getAlgorithm(node.getParticipantId());
		Request request = algo.undo();
		apply(node.getParticipantId(), request.getOperation());
		Iterator it = node.getRemoteSuccessors().iterator();
		while (it.hasNext()) {
			ReceptionNode remote = (ReceptionNode) it.next();
			remote.setRequest(node.getParticipantId(), request);
		}
	}

	/**
	 * Visits a redo node. Calls redo on the local algorithm to get a request
	 * that is then sent to all remote successors.
	 * 
	 * @param node the node to visit
	 */
	public void visit(RedoNode node) {
		LOG.info("visit: " + node);
		Algorithm algo = getAlgorithm(node.getParticipantId());
		Request request = algo.redo();
		apply(node.getParticipantId(), request.getOperation());
		Iterator it = node.getRemoteSuccessors().iterator();
		while (it.hasNext()) {
			ReceptionNode remote = (ReceptionNode) it.next();
			remote.setRequest(node.getParticipantId(), request);
		}
	}

	/**
	 * Visits a reception node. The stored request is retrieved from the node
	 * and passed to the correct algorithm.
	 * 
	 * @param node
	 *            the node to visit
	 */
	public void visit(ReceptionNode node) {
		LOG.info("visit: " + node);
		Request request = node.getRequest();
		Algorithm algo = getAlgorithm(node.getParticipantId());
		try {
			Operation op = algo.receiveRequest(request);
			apply(node.getParticipantId(), op);
		} catch (TransformationException e) {
			throw new RuntimeException("algorithm threw unexected runtime exception", e);
		}
	}

	/**
	 * Visits a relay node. Here the server side processing must be handled.
	 */
	public void visit(RelayNode node) {
		// this type of visitor does not handle relay nodes
	}

	/**
	 * Visits a verification node. This node type is used to verify the document
	 * content at an arbitrary point in the sites lifecycle.
	 * 
	 * @param node
	 *            the node to visit
	 * @throws VerificationException
	 *             if the document state does not match the expected state
	 */
	public void visit(VerificationNode node) {
		LOG.info("visit: " + node);
		verify(node.getParticipantId(), node.getState());
	}

	/**
	 * Visits an end node. This is the place where actual verification takes
	 * place. The end node stores the expected content. This content is compared
	 * to the actual document at the site. If they do not match a
	 * {@link VerificationException} is thrown.
	 * 
	 * @param node
	 *            the node to visit
	 * @throws VerificationException
	 *             if the document state does not match the expected state
	 */
	public void visit(EndNode node) {
		LOG.info("visit: " + node);
		verify(node.getParticipantId(), node.getState());
	}

	/**
	 * Verifies that the current state at the given site corresponds to the
	 * given state.
	 * 
	 * @param siteId
	 *            the site to verify
	 * @param expected
	 *            the expected state
	 * @throws VerificationException
	 *             if the document state does not match the expected state
	 */
	protected void verify(final String siteId, final String expected) {
		Document doc = getDocument(siteId);
		String actual = doc.getContent();
		if (!expected.equals(actual)) {
			addFailure(siteId, expected, nullSafeToString(actual));
		} else {
			addSuccess(siteId, expected);
		}
	}
	
	private String nullSafeToString(Object o) {
		return o == null ? "" : o.toString();
	}

	public Document getDocument(String siteId) {
		Document doc = (Document) documents.get(siteId);
		if (doc == null) {
			throw new ScenarioException("unknown site: " + siteId);
		}
		return doc;
	}
	
	public void setDocument(String siteId, Document doc) {
		if (documents.containsKey(siteId)) {
			throw new IllegalStateException("document for site " + siteId
							+ " already set");
		}
		documents.put(siteId, doc);
	}
	
	/**
	 * Gets the algorithm for the given site. Throws a ScenarioException if an
	 * algorithm for the given site does not exist.
	 * 
	 * @param siteId
	 *            the site for which the algorithm is requested
	 * @return the algorithm for the site
	 * @throws ScenarioException
	 *             if there is no algorithm for the site
	 */
	public Algorithm getAlgorithm(String siteId) {
		Algorithm algo = (Algorithm) algorithms.get(siteId);
		if (algo == null) {
			throw new ScenarioException("unknown site: " + siteId);
		}
		return algo;
	}
	
	/**
	 * Sets the algorithm for the given site.
	 * 
	 * @param siteId the site id of the site
	 * @param algorithm the algorithm
	 */
	protected void setAlgorithm(String siteId, Algorithm algorithm) {
		if (algorithms.containsKey(siteId)) {
			throw new IllegalStateException("algorithm for site " + siteId
							+ " already set");
		}
		algorithms.put(siteId, algorithm);
	}

}

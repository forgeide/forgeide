/*
 * $Id: ScenarioBuilder.java 2430 2005-12-11 15:17:11Z sim $
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

import ch.iserver.ace.algorithm.Operation;

/**
 * This interface contains the elementary operations needed to construct a
 * scenario. It is based on the builder pattern (see GOF). 
 */
public interface ScenarioBuilder {

	/**
	 * Initializes the builder and passes in the initial state and the
	 * expected final state.
	 * 
	 * @param initialState the initial state at all sites
	 * @param finalState the expected final state
	 */
	public void init(String initialState, String finalState);
	
	/**
	 * Notifies the builder of the start of a new site to process.
	 * Calls to startSite and endSite must be executed in proper
	 * sequence. Most notably, after calling startSite this method
	 * cannot be executed again until endSite is called.
	 * 
	 * @param siteId the identifier of the site
	 * @throws ScenarioException in case of errors
	 */
	public void startSite(String siteId);
	
	/**
	 * Adds the reception of an operation to the current site. The
	 * current site is the last site for which startSite was called.
	 * This method must be called within startSite/endSite.
	 * 
	 * @param opRef the operation to be received
	 * @throws ScenarioException in case of errors
	 */
	public void addReception(String opRef);
	
	/**
	 * Adds the generation of an operation to the current site. The
	 * current site is the last site for which startSite was called.
	 * 
	 * @param id the id of this event
	 * @param operation the generated operation
	 * @throws ScenarioException in case of errors
	 */
	public void addDoGeneration(String id, Operation operation);
	
	/**
	 * Adds the generation of an undo to the current site. The current
	 * site is the last site for which startSite was called.
	 *
	 * @param id the undo reference
	 * @throws ScenarioException in case of errors
	 */
	public void addUndoGeneration(String id);
	
	/**
	 * Adds the generation of a redo to the current site. The current
	 * site is the last site for which startSite was called.
	 * 
	 * @param id the redo reference
	 * @throws ScenarioException in case of errors
	 */
	public void addRedoGeneration(String id);
	
	/**
	 * Adds a verification point to the current site. The current
	 * site is the last site for which startSite was called.
	 * 
	 * @param expect the expected document content
	 * @throws ScenarioException in case of errors
	 */
	public void addVerification(String expect);
	
	/**
	 * Adds a relay event to the list of relay events.
	 * 
	 * @param ref the referenced operation
	 * @param id  the id of this generation event
	 */
	public void addRelay(String ref, String id);
	
	/**
	 * Notifies the builder of the end of a site. A call to startSite
	 * must preceed this call.
	 * 
	 * @throws ScenarioException in case of errors
	 */
	public void endSite();
	
}

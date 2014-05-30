/*
 * $$Id: Algorithm.java 2430 2005-12-11 15:17:11Z sim $$
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

package ch.iserver.ace.algorithm;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


/**
 * This interface is the basic interface every OT algorithm implementation must
 * implement. It contains methods for generating requests for an operation,
 * receiving requests and for site management.
 */
public interface Algorithm {

	/**
	 * Gets the site id of this algorithm.
	 * 
	 * @return the site id
	 */
	int getSiteId();
	
	/**
	 * Gets the current timestamp at the local site.
	 * 
	 * @return the current timestamp
	 */
	Timestamp getTimestamp();
	
	/**
	 * Checks whether an undo is possible at the moment. If this method returns
	 * true, a subsequent call to undo must succeed.
	 * 
	 * @return true iff an undo is possible
	 */
	boolean canUndo();

	/**
	 * Checks whether a redo is possible at the moment. If this method returns
	 * true, a subsequent call to redo must succeed.
	 * 
	 * @return true iff a redo is possible
	 */
	boolean canRedo();

	/**
	 * Undo the last local operation.
	 * 
	 * @return the request to be sent to other sites
	 * @throws javax.swing.undo.CannotUndoException
	 *             iff an undo is impossible
	 */
	Request undo() throws CannotUndoException;

	/**
	 * Redo the last undone local operation.
	 * 
	 * @return the request to be sent to other sites
	 * @throws javax.swing.undo.CannotRedoException
	 *             iff a redo is impossible
	 */
	Request redo() throws CannotRedoException;

	/**
	 * Generates a request for the given operation. The operation is a locally
	 * generated operation. The returned request must be sent to the other
	 * sites.
	 * 
	 * @param op the operation for which a request should be generated
	 * @return the generated request
	 * @see Request
	 */
	Request generateRequest(Operation op);
		
	/**
	 * Receives a request from a remote site. The request must be transformed
	 * and the resulting operation is returned.
	 * 
	 * @param req the request to transform and apply
	 * @return the transformed Operation
	 */
	Operation receiveRequest(Request req) throws TransformationException;
	
	/**
	 * Notifies the algorithm that the site specified by the site id has 
	 * processed the number of messages in the timestamp.
	 * 
	 * @param siteId the site id of the sending site
	 * @param timestamp the timestamp at the other site
	 * @throws TransformationException
	 */
	void acknowledge(int siteId, Timestamp timestamp) throws TransformationException;
	
	/**
	 * Transform the array of indices from the state indicated by the timestamp
	 * to the current timestamp at the local site. The transformed indices
	 * are returned to the client.
	 * 
	 * @param timestamp the timestamp at which the indices are valid
	 * @param indices the array of integer indices
	 * @return the transformed array of indices
	 */
	int[] transformIndices(Timestamp timestamp, int[] indices) throws TransformationException;
	
}

/*
 * $Id: AlgorithmWrapperImpl.java 2840 2006-03-28 17:35:57Z sim $
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

package ch.iserver.ace.collaboration.jupiter;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Algorithm;
import ch.iserver.ace.algorithm.Operation;
import ch.iserver.ace.algorithm.Request;
import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.algorithm.TransformationException;
import ch.iserver.ace.util.ParameterValidator;

/**
 * AlgorithmWrapper wraps an Algorithm and adds certain useful methods.
 * The Algorithm class itself stays agnostic of CaretUpdate objects. All it
 * does is transform indices and operations. 
 */
public class AlgorithmWrapperImpl implements AlgorithmWrapper {
	
	/**
	 * The wrapped Algorithm instance.
	 */
	private final Algorithm algorithm;
	
	/**
	 * Creates a new AlgorithmWrapperImpl object wrapping the given Algorithm
	 * object.
	 * 
	 * @param algorithm the wrapped Algorithm instance
	 */
	public AlgorithmWrapperImpl(Algorithm algorithm) {
		ParameterValidator.notNull("algorithm", algorithm);
		this.algorithm = algorithm;
	}
	
	/**
	 * Gets the algorithm wrapped by this wrapper.
	 * 
	 * @return the wrapped algorithm
	 */
	protected Algorithm getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.Algorithm#getSiteId()
	 */
	public int getSiteId() {
		return getAlgorithm().getSiteId();
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.Algorithm#getTimestamp()
	 */
	public Timestamp getTimestamp() {
		return getAlgorithm().getTimestamp();
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.Algorithm#canRedo()
	 */
	public boolean canRedo() {
		return getAlgorithm().canRedo();
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.Algorithm#canUndo()
	 */
	public boolean canUndo() {
		return getAlgorithm().canUndo();
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.Algorithm#redo()
	 */
	public Request redo() throws CannotRedoException {
		return getAlgorithm().redo();
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.Algorithm#undo()
	 */
	public Request undo() throws CannotUndoException {
		return getAlgorithm().undo();
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AlgorithmWrapper#receiveRequest(ch.iserver.ace.algorithm.Request)
	 */
	public Operation receiveRequest(Request request) throws TransformationException {
		return getAlgorithm().receiveRequest(request);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AlgorithmWrapper#receiveCaretUpdateMessage(ch.iserver.ace.algorithm.CaretUpdateMessage)
	 */
	public CaretUpdate receiveCaretUpdateMessage(CaretUpdateMessage message) throws TransformationException {
		Algorithm algorithm = getAlgorithm();
		int[] indices = message.getUpdate().getIndices();
		indices = algorithm.transformIndices(message.getTimestamp(), indices);
		return new CaretUpdate(indices[CaretUpdate.DOT], indices[CaretUpdate.MARK]);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AlgorithmWrapper#acknowledge(int, ch.iserver.ace.algorithm.Timestamp)
	 */
	public void acknowledge(int siteId, Timestamp timestamp) throws TransformationException {
		getAlgorithm().acknowledge(siteId, timestamp);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AlgorithmWrapper#generateRequest(ch.iserver.ace.algorithm.Operation)
	 */
	public Request generateRequest(Operation operation) {
		return getAlgorithm().generateRequest(operation);
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.AlgorithmWrapper#generateCaretUpdateMessage(ch.iserver.ace.CaretUpdate)
	 */
	public CaretUpdateMessage generateCaretUpdateMessage(CaretUpdate update) {
		CaretUpdateMessage msg = new CaretUpdateMessage(
						getAlgorithm().getSiteId(),
						getAlgorithm().getTimestamp(),
						update);
		return msg;
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.Algorithm#transformIndices(ch.iserver.ace.algorithm.Timestamp, int[])
	 */
	public int[] transformIndices(Timestamp timestamp, int[] indices) throws TransformationException {
		return getAlgorithm().transformIndices(timestamp, indices);
	}

}

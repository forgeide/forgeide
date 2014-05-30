/*
 * $Id: AlgorithmWrapper.java 2840 2006-03-28 17:35:57Z sim $
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

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.CaretUpdateMessage;
import ch.iserver.ace.algorithm.Algorithm;
import ch.iserver.ace.algorithm.TransformationException;

/**
 * Wrapper interface for algorithm instances. Adds methods for receiving
 * and generating CaretUpdate and the corresponding CaretUpdateMessage.
 */
public interface AlgorithmWrapper extends Algorithm {
			
	/**
	 * Receives a CaretUpdateMessage and returns the transformed CaretUpdate.
	 * 
	 * @param message the CaretUpdateMessage to receive
	 * @return the transformed CaretUpdate
	 * @throws TransformationException
	 */
	CaretUpdate receiveCaretUpdateMessage(CaretUpdateMessage message) throws TransformationException;
			
	/**
	 * Generates a CaretUpdateMessage for a locally generated CaretUpdate.
	 * 
	 * @param update the locally generated CaretUpdate
	 * @return the CaretUpdateMessage to be sent
	 */
	CaretUpdateMessage generateCaretUpdateMessage(CaretUpdate update);
	
}

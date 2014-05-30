/*
 * $Id: TwoWayExecuteVisitor.java 2430 2005-12-11 15:17:11Z sim $
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

package ch.iserver.ace.algorithm.test.jupiter;

import ch.iserver.ace.algorithm.Algorithm;
import ch.iserver.ace.algorithm.test.AlgorithmTestFactory;
import ch.iserver.ace.algorithm.test.DefaultDocument;
import ch.iserver.ace.algorithm.test.ExecuteVisitor;
import ch.iserver.ace.algorithm.test.StartNode;

/**
 * A two way execute visitor is to be used with standard scenario files
 * and jupiter. It determines which site is to be a server and which
 * to be a client in a deterministic way.
 */
public class TwoWayExecuteVisitor extends ExecuteVisitor {

	/**
	 * Creates a new two way execute visitor that uses the given algorithm
	 * factory.
	 * 
	 * @param factory the factory to use
	 */
	public TwoWayExecuteVisitor(AlgorithmTestFactory factory) {
		super(factory);
	}
	
	public void visit(StartNode node) {
		setDocument(node.getParticipantId(), new DefaultDocument(node.getState()));
		Boolean isClient = Boolean.valueOf(node.getSites() % 2 == 0);
		Algorithm algorithm = getFactory().createAlgorithm(
				Integer.parseInt(node.getParticipantId()), isClient);
		setAlgorithm(node.getParticipantId(), algorithm);
	}
	
}

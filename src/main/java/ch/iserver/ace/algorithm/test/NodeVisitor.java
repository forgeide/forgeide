/*
 * $Id: NodeVisitor.java 2430 2005-12-11 15:17:11Z sim $
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
 * NodeVisitor is an interface that allows to visit nodes. The idea behind
 * this interface is based on the double dispatch principle.
 * 
 * <pre>
 *   Node n = ...;         // got from somewhere else
 *   NodeVisitor v = ...;  // your node visitor
 *   n.accept(v);
 * </pre>
 * 
 * The node will call the correct visit method on the visitor. Using
 * this simple scheme, instanceof checks can be avoided.
 */
public interface NodeVisitor {

	/**
	 * Visit a node of type StartNode.
	 * 
	 * @param node the node to visit
	 */
	public void visit(StartNode node);
	
	/**
	 * Visit a node of type GenerationNode.
	 * 
	 * @param node the node to visit
	 */
	public void visit(DoNode node);
	
	/**
	 * Visit a node of type UndoNode.
	 * 
	 * @param node the node to visit
	 */
	public void visit(UndoNode node);
	
	/**
	 * Visit a node of type RedoNode.
	 * 
	 * @param node the node to visit
	 */
	public void visit(RedoNode node);
	
	/**
	 * Visit a node of type ReceptionNode.
	 * 
	 * @param node the node to visit
	 */
	public void visit(ReceptionNode node);
	
	/**
	 * Visit a node of type RelayNode.
	 * 
	 * @param node the node to visit
	 */
	public void visit(RelayNode node);
	
	/**
	 * Visit a node of type VerificationNode.
	 * 
	 * @param node the node to visit
	 */
	public void visit(VerificationNode node);
	
	/**
	 * Visit a node of type EndNode.
	 * 
	 * @param node the node to visit
	 */
	public void visit(EndNode node);
	
}

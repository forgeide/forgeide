/*
 * $Id: CaretHandler.java 1904 2005-11-29 07:42:33Z sim $
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

package ch.iserver.ace.util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Helper class used to update the position of a caret of a particular user.
 */
public class CaretHandler implements DocumentListener {
	
	/**
	 * The dot position of the caret.
	 */
	private int dot;
	
	/**
	 * The mark position of the caret
	 */
	private int mark;
	
	/**
	 * Initializes a new CaretHandler.
	 * 
	 * @param dot the dot position
	 * @param mark the mark position
	 */
	public CaretHandler(int dot, int mark) {
		this.dot = dot;
		this.mark = mark;
	}
	
	/**
	 * Retrieves the dot position of the caret.
	 * 
	 * @return the dot position
	 */
	public int getDot() {
		return dot;
	}
	
	/**
	 * Sets the dot position of the caret.
	 * 
	 * @param dot the new dot position
	 */
	public void setDot(int dot) {
		this.dot = dot;
	}
	
	/**
	 * Sets the dot position of the caret, ensuring that the caret does not
	 * leave the document bounds.
	 * 
	 * @param dot the new dot position
	 * @param length the length of the document
	 */
	protected void setDot(int dot, int length) {
		this.dot = Math.max(Math.min(dot, length), 0);
	}
	
	/**
	 * Retrieves the mark position of the caret.
	 * 
	 * @return the mark position
	 */
	public int getMark() {
		return mark;
	}
	
	/**
	 * Sets the mark position of the caret.
	 * 
	 * @param mark the new mark position
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}
	
	/**
	 * Sets the mark position of the caret, ensuring that the caret does not
	 * leave the document bounds.
	 * 
	 * @param mark the new mark position
	 * @param length the length of the document
	 */
	protected void setMark(int mark, int length) {
		this.mark = Math.max(Math.min(mark, length), 0);
	}
	
	/**
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent e) {
		// ignored: attribute changes do not modify positions
	}
	
	/**
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent e) {
		int offset = e.getOffset();
		int length = e.getLength();
		int newDot = dot;
		if (newDot >= offset) {
			newDot += length;
		}
		int newMark = mark;
		if (newMark >= offset) {
			newMark += length;
		}		    
		setDot(newDot, e.getDocument().getLength());
		setMark(newMark, e.getDocument().getLength());
	}
	
	/**
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent e) {
		int offs0 = e.getOffset();
		int offs1 = e.getOffset() + e.getLength();
		int newDot = dot;
		if (newDot >= offs1) {
			newDot -= (offs1 - offs0);
		} else if (newDot >= offs0) {
			newDot = offs0;
		}
		int newMark = mark;
		if (newMark >= offs1) {
			newMark -= (offs1 - offs0);
		} else if (newMark >= offs0) {
			newMark = offs0;
		}
		setDot(newDot, e.getDocument().getLength());
		setMark(newMark, e.getDocument().getLength());
	}
	
}

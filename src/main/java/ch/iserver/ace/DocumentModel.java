/*
 * $Id: DocumentModel.java 908 2005-11-03 07:33:14Z sim $
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

package ch.iserver.ace;

import ch.iserver.ace.util.ParameterValidator;

/**
 * The DocumentModel used by the publisher of a document.
 */
public final class DocumentModel {
	
	/** The content of the document. */
	private final String content;
	
	/** The cursor dot position. */
	private final int dot;
	
	/** The cursor mark position. */
	private final int mark;
	
	/** The document details of the document. */
	private final DocumentDetails details;
	
	/**
	 * Creates a new DocumentModel.
	 * 
	 * @param content the content
	 * @param dot the cursor dot position
	 * @param mark the cursor mark position
	 * @param details the document details
	 */
	public DocumentModel(String content, int dot, int mark, DocumentDetails details) {
		ParameterValidator.notNull("content", content);
		ParameterValidator.notNull("details", details);
		this.content = content;
		this.dot = dot;
		this.mark = mark;
		this.details = details;
	}
	
	/**
	 * @return the content of the document
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * @return the cursor dot position
	 */
	public int getDot() {
		return dot;
	}
	
	/**
	 * @return the cursor mark position
	 */
	public int getMark() {
		return mark;
	}
	
	/**
	 * @return the document details of the document
	 */
	public DocumentDetails getDetails() {
		return details;
	}
	
}

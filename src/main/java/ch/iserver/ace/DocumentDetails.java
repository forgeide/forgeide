/*
 * $Id: DocumentDetails.java 2019 2005-12-01 10:00:48Z sim $
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

/**
 * The DocumentDetails contains information about a document.
 */
public class DocumentDetails {
	
	/** 
	 * The title of the document. 
	 */
	private final String title;
	
	/**
	 * Creates a new DocumentDetails object.
	 * 
	 * @param title the title of the document
	 */
	public DocumentDetails(String title) {
		this.title = title;
	}
	
	/**
	 * @return gets the title of the document
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null ) {
			return false;
		} else if (obj instanceof DocumentDetails) {
			DocumentDetails det = (DocumentDetails) obj;
			return this.getTitle().equals(det.getTitle());
		}
		return false;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hash = 13;
		hash += title.hashCode();
		return hash;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "DocumentDetails('"+title+"')";
	}
	
}

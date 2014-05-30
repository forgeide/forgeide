/*
 * $Id: ApplicationError.java 2019 2005-12-01 10:00:48Z sim $
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

package ch.iserver.ace.collaboration.jupiter.server.document;

import java.util.HashMap;
import java.util.Map;

import ch.iserver.ace.util.ParameterValidator;


public class DocumentEvent {

	private final int offset;

	private final int length;

	private final String text;

	private final Map attributes;
	
	public DocumentEvent(int offset, int length, String text) {
		this(offset, length, text, new HashMap());
	}

	public DocumentEvent(int offset, int length, String text, Map attributes) {
		ParameterValidator.notNegative("offset", offset);
		ParameterValidator.notNegative("length", length);
		ParameterValidator.notNull("attributes", attributes);

		this.offset = offset;
		this.length = length;
		this.text = text;
		this.attributes = attributes;
	}

	public Map getAttributes() {
		return attributes;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}

	public String getText() {
		return text;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass().equals(obj.getClass())) {
			DocumentEvent evt = (DocumentEvent) obj;
			return offset == evt.offset && length == evt.length
					&& text.equals(evt.text)
					&& attributes.equals(evt.attributes);
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		int hash = 7 + offset;
		hash += 11 * length;
		hash += 13 * text.hashCode();
		hash += 17 * attributes.hashCode();
		return hash;
	}
	
}

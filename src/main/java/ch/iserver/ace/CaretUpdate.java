/* $Id: CaretUpdate.java 2020 2005-12-01 10:04:18Z sim $
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
 * A CaretUpdate encapsulates the details of an update to the caret. The
 * caret consists of a dot (the position of the cursor) and a mark. If
 * both are at the same position, there is no selection. Otherwise the
 * selection starts at the mark and ends at the dot. 
 */
public class CaretUpdate  {
	
	/**
	 * Constant indicating the index of the dot in the interal array.
	 */
	public static final int DOT = 0;
	
	/**
	 * Constant indicating the index of the mark in the interal array.
	 */
	public static final int MARK = 1;
	
	/**
	 * Int array keeping dot and mark values.
	 */
	private int[] indices = new int[2];
	
	/**
	 * Creates a new CaretUpdate.
	 * 
	 * @param dot the dot value
	 * @param mark the mark value
	 */
	public CaretUpdate(int dot, int mark) {
		this.indices[DOT] = dot;
		this.indices[MARK] = mark;
	}
	
	/**
	 * Retrieves the dot value of this instance. The dot represents the current
	 * position of the blinking cursor.
	 * 
	 * @return the dot value
	 */
	public int getDot() {
		return indices[DOT];
	}
	
	/**
	 * Retrieves the mark value of this instance. The mark represents the
	 * current position of the other end of the selection.
	 * 
	 * @return the mark value
	 */
	public int getMark() {
		return indices[MARK];
	}
	
	/**
	 * Gets an array consisting of dot and mark. The dot value is in the
	 * first position of the array, the mark value in the second.
	 * 
	 * @return array consisting of dot and mark (index 0 and 1 respecitvely)
	 */
	public int[] getIndices() {
		return new int[] { getDot(), getMark() };
	}
	
	/**
	 * Returns a String representation of this object.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getName() + " [" + getDot() + "," + getMark() + "]";
	}
	
	/**
	 * Compares the passed in object <var>obj</var> to this instance. Two
	 * CaretUpdate instances are equal if they have the same values for their
	 * dot and mark properties.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof CaretUpdate) {
			CaretUpdate cu = (CaretUpdate) obj;
			return getDot() == cu.getDot() &&
			       getMark() == cu.getMark();
		} else {
			return false;
		}
	}
	
	/**
	 * Computes the hash code for a CaretUpdate.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getDot() + 13 * getMark();
	}
	
}

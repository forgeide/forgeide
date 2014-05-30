/*
 * $Id: JupiterVectorTime.java 2001 2005-12-01 07:33:09Z sim $
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
package ch.iserver.ace.algorithm.jupiter;

import ch.iserver.ace.algorithm.Timestamp;
import ch.iserver.ace.algorithm.VectorTime;

/**
 * This class models the vector time for the Jupiter control algorithm.
 */
public class JupiterVectorTime implements VectorTime, Cloneable {

	/**
	 * Counter for the number of local operations.
	 */
	private int localOperationCnt;

	/**
	 * Counter for the number of remote operations.
	 */
	private int remoteOperationCnt;

	/**
	 * Create a new JupiterVectorTime.
	 * 
	 * @param localCnt
	 *            the local operation count.
	 * @param remoteCnt
	 *            the remote operation count.
	 */
	public JupiterVectorTime(int localCnt, int remoteCnt) {
		if (localCnt < 0) {
			throw new IllegalArgumentException("local operation count cannot be negative");
		}
		if (remoteCnt < 0) {
			throw new IllegalArgumentException("remote operation count cannot be negative");
		}
		localOperationCnt = localCnt;
		remoteOperationCnt = remoteCnt;
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.VectorTime#getAt(int)
	 */
	public int getAt(int index) {
		if (index == 0) {
			return getLocalOperationCount();
		} else if (index == 1) {
			return getRemoteOperationCount();
		} else {
			throw new IndexOutOfBoundsException("" + index);
		}
	}
	
	/**
	 * @see ch.iserver.ace.algorithm.VectorTime#getLength()
	 */
	public int getLength() {
		return 2;
	}
	
	/**
	 * @see Timestamp#getComponents()
	 */
	public int[] getComponents() {
		return new int[] { getLocalOperationCount(), getRemoteOperationCount() };
	}

	/**
	 * @return Returns the local operation count.
	 */
	public int getLocalOperationCount() {
		return localOperationCnt;
	}

	/**
	 * @return Returns the remote operation count.
	 */
	public int getRemoteOperationCount() {
		return remoteOperationCnt;
	}

	/**
	 * Increment the local operation counter.
	 * 
	 * @return the counter after increment.
	 */
	public int incrementLocalOperationCount() {
		return ++localOperationCnt;
	}

	/**
	 * Increment the remote operation counter.
	 * 
	 * @return the counter after increment.
	 */
	public int incrementRemoteRequestCount() {
		return ++remoteOperationCnt;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(localOperationCnt);
		buffer.append(",");
		buffer.append(remoteOperationCnt);
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj.getClass().equals(getClass())) {
			JupiterVectorTime vector = (JupiterVectorTime) obj;
			return vector.localOperationCnt == localOperationCnt
					&& vector.remoteOperationCnt == remoteOperationCnt;
		} else {
			return false;
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashcode = 17;
		hashcode = 37 * hashcode + localOperationCnt;
		hashcode = 37 * hashcode + remoteOperationCnt;
		return hashcode;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
	}

}

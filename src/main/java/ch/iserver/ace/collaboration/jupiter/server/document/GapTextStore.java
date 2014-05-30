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

public class GapTextStore implements TextStore {

	private int highWatermark;
	
	private int lowWatermark;

	private int gapStart = -1;
	
	private int gapEnd = -1;

	private char[] content = new char[0];

	public GapTextStore() {
		this(30, 200);
	}
	
	public GapTextStore(int lowWatermark, int highWatermark) {
		if (!(lowWatermark < highWatermark)) {
			throw new IllegalArgumentException("lowWatermark must be smaller than highWatermark");
		}
		this.lowWatermark = lowWatermark;
		this.highWatermark = highWatermark;
	}

	/**
	 * Adjusts the gap so that is at the right offset and capable of handling
	 * the addition of a specified number of characters without having to be
	 * shifted. The <code>sizeHint</code> represents the range that will be
	 * filled afterwards. If the gap is already at the right offset, it must
	 * only be resized if it will be no longer between the low and high
	 * watermark. However, on delete (sizeHint &lt; 0) at the edges of the gap,
	 * the gap is only enlarged.
	 * 
	 * @param offset
	 *            the offset at which the change happens
	 * @param sizeHint
	 *            the number of character which will be inserted
	 */
	private void adjustGap(int offset, int sizeHint) {
		if (offset == gapStart) {
			int size = (gapEnd - gapStart) - sizeHint;
			if (lowWatermark <= size && size <= highWatermark)
				return;
		}

		moveAndResizeGap(offset, sizeHint);
	}

	/**
	 * Moves the gap to the specified offset and adjust its size to the
	 * anticipated change size. The given size represents the expected range of
	 * the gap that will be filled after the gap has been moved. Thus the gap is
	 * resized to actual size + the specified size and moved to the given
	 * offset.
	 * 
	 * @param offset
	 *            the offset where the gap is moved to
	 * @param size
	 *            the anticipated size of the change
	 */
	private void moveAndResizeGap(int offset, int size) {
		char[] newContent = null;
		int oldSize = gapEnd - gapStart;
		int newSize = highWatermark + size;

		if (newSize < 0) {
			if (oldSize > 0) {
				newContent = new char[content.length - oldSize];
				System.arraycopy(content, 0, newContent, 0, gapStart);
				System.arraycopy(content, gapEnd, newContent, gapStart, newContent.length - gapStart);
				content = newContent;
			}
			gapStart = gapEnd = offset;
			return;
		}

		newContent = new char[content.length + (newSize - oldSize)];

		int newGapStart = offset;
		int newGapEnd = newGapStart + newSize;

		if (oldSize == 0) {
			System.arraycopy(content, 0, newContent, 0, newGapStart);
			System.arraycopy(content, newGapStart, newContent, newGapEnd, newContent.length - newGapEnd);
		} else if (newGapStart < gapStart) {
			int delta = gapStart - newGapStart;
			System.arraycopy(content, 0, newContent, 0, newGapStart);
			System.arraycopy(content, newGapStart, newContent, newGapEnd, delta);
			System.arraycopy(content, gapEnd, newContent, newGapEnd + delta, content.length - gapEnd);
		} else {
			int delta = newGapStart - gapStart;
			System.arraycopy(content, 0, newContent, 0, gapStart);
			System.arraycopy(content, gapEnd, newContent, gapStart, delta);
			System.arraycopy(content, gapEnd + delta, newContent, newGapEnd, newContent.length - newGapEnd);
		}

		content = newContent;
		gapStart = newGapStart;
		gapEnd = newGapEnd;
	}

	/*
	 * @see ch.iserver.ace.text.ITextStore#getText(int, int)
	 */
	public String getText(int offset, int length) {
		int end = offset + length;

		if (content == null)
			return ""; //$NON-NLS-1$

		if (end <= gapStart)
			return new String(content, offset, length);

		if (gapStart < offset) {
			int gapLength = gapEnd - gapStart;
			return new String(content, offset + gapLength, length);
		}

		StringBuffer buf = new StringBuffer();
		buf.append(content, offset, gapStart - offset);
		buf.append(content, gapEnd, end - gapStart);
		return buf.toString();
	}

	/*
	 * @see org.eclipse.jface.text.ITextStore#getLength()
	 */
	public int getLength() {
		int length = gapEnd - gapStart;
		return (content.length - length);
	}

	/*
	 * @see org.eclipse.jface.text.ITextStore#replace(int, int,
	 *      java.lang.String)
	 */
	public void replace(int offset, int length, String text) {
		int textLength = (text == null ? 0 : text.length());

		// handle delete at the edges of the gap
		if (textLength == 0) {
			if (offset <= gapStart && offset + length >= gapStart
					&& gapStart > -1 && gapEnd > -1) {
				length -= gapStart - offset;
				gapStart = offset;
				gapEnd += length;
				return;
			}
		}

		// move gap
		adjustGap(offset + length, textLength - length);

		// overwrite
		int min = Math.min(textLength, length);
		for (int i = offset, j = 0; i < offset + min; i++, j++) {
			content[i] = text.charAt(j);
		}

		if (length > textLength) {
			// enlarge the gap
			gapStart -= (length - textLength);
		} else if (textLength > length) {
			// shrink gap
			gapStart += (textLength - length);
			for (int i = length; i < textLength; i++)
				content[offset + i] = text.charAt(i);
		}
	}

}

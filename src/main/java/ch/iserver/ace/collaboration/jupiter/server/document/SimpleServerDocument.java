/*
 * $Id: SimpleServerDocument.java 2833 2006-03-22 22:09:37Z sim $
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.Fragment;
import ch.iserver.ace.collaboration.jupiter.server.PortableDocumentImpl;
import ch.iserver.ace.collaboration.jupiter.server.ServerDocument;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;

public class SimpleServerDocument implements ServerDocument {
	
	private static final String PARTICIPANT_KEY = "participant-id";
	
	private DocumentPartitioner partitioner;

	private GapTextStore textStore;
	
	private Map participants = new HashMap();
	
	private Map selections = new HashMap();
	
	public SimpleServerDocument() {
		this.textStore = new GapTextStore(5, 20);
		this.partitioner = new SimplePartitioner();
	}
	
	protected void addParticipant(int participantId, RemoteUserProxy proxy, Selection handler) {
		participants.put(new Integer(participantId), proxy);
		selections.put(new Integer(participantId), handler);
	}
	
	protected void removeParticipant(int participantId) {
		participants.remove(new Integer(participantId));
		selections.remove(new Integer(participantId));
	}
	
	protected Selection getCaretHandler(int participantId) {
		return (Selection) selections.get(new Integer(participantId));
	}
	
	public void participantJoined(int participantId, RemoteUserProxy proxy) {
		addParticipant(participantId, proxy, new Selection());
	}

	public void participantLeft(int participantId) {
		removeParticipant(participantId);
	}

	public void updateCaret(int participantId, int dot, int mark) {
		Selection handler = getCaretHandler(participantId);
		if (handler == null) {
			throw new IllegalStateException("unkown participant: " + participantId);
		}
		handler.dot = dot;
		handler.mark = mark;
	}

	public void insertString(int participantId, int offset, String text) {
		textStore.replace(offset, 0, text);
		DocumentEvent event = new DocumentEvent(offset, 0, text, Collections.singletonMap(PARTICIPANT_KEY, new Integer(participantId)));
		partitioner.documentUpdated(event);
		updateSelections(offset, text.length());
	}

	public void removeString(int offset, int length) {
		textStore.replace(offset, length, "");
		DocumentEvent event = new DocumentEvent(offset, length, "");
		partitioner.documentUpdated(event);
		updateSelections(offset, -length);
	}

	public String getText() {
		return textStore.getText(0, getLength());
	}
	
	public int getLength() {
		return textStore.getLength();
	}

	public PortableDocument toPortableDocument() {
		PortableDocumentImpl result = new PortableDocumentImpl();
		addParticipants(result);
		addFragments(result);
		result.freeze();
		return result;
	}
	
	// query methods
	
	private void updateSelections(int offset, int length) {
		Iterator it = selections.values().iterator();
		while (it.hasNext()) {
			Selection handler = (Selection) it.next();
			if (length < 0) {
				handler.remove(offset, -length);
			} else if (length > 0) {
				handler.insert(offset, length, getLength());
			}
		}
	}
	
	private int getParticipantId(AttributedRegion region) {
		int participantId = -1;
		Integer value = (Integer) region.getAttribute(PARTICIPANT_KEY);
		if (value != null) {
			participantId = value.intValue();
		}
		return participantId;
	}
	
	public RemoteUserProxy getUserProxy(int participantId) {
		return (RemoteUserProxy) participants.get(new Integer(participantId));
	}
	
	public CaretUpdate getSelection(int participantId) {
		Selection handler = getCaretHandler(participantId);
		if (handler == null) {
			throw new IllegalStateException("unkown participant: " + participantId);
		}
		return new CaretUpdate(handler.dot, handler.mark);
	}
	
	public Iterator getFragments() {
		final AttributedRegion[] regions = partitioner.getRegions();
		return new Iterator() {
			int index = 0;
			
			public void remove() {
				throw new UnsupportedOperationException("remove");
			}
		
			public Object next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				AttributedRegion region = regions[index++];
				int participantId = getParticipantId(region);
				String text = textStore.getText(region.getStart(), region.getLength());
				Fragment fragment = new FragmentImpl(participantId, text);
				return fragment;
			}
		
			public boolean hasNext() {
				return index < regions.length;
			}
		};
	}

	
	/**
	 * Adds all the participants to the copy of the document.
	 * 
	 * @param result the document beeing built
	 */
	protected void addParticipants(PortableDocumentImpl result) {
		Iterator it = participants.keySet().iterator();
		while (it.hasNext()) {
			Integer id = (Integer) it.next();
			RemoteUserProxy user = getUserProxy(id.intValue());
			CaretUpdate selection = getSelection(id.intValue());
			result.addParticipant(id.intValue(), user, selection);
		}
	}
	
	/**
	 * Adds all the fragments to the copy of the document.
	 * 
	 * @param result the document beeing built
	 */
	protected void addFragments(PortableDocumentImpl result) {
		AttributedRegion[] regions = partitioner.getRegions();
		for (int i = 0; i < regions.length; i++) {
			AttributedRegion region = regions[i];
			int participantId = getParticipantId(region);
			String text = textStore.getText(region.getStart(), region.getLength());
			Fragment fragment = new FragmentImpl(participantId, text);
			result.addFragment(fragment);
		}
	}
	
	private static class FragmentImpl implements Fragment {
		private final int participantId;
		private final String text;
		
		public FragmentImpl(int participantId, String text) {
			this.participantId = participantId;
			this.text = text;
		}
		
		public int getParticipantId() {
			return participantId;
		}
		public String getText() {
			return text;
		}
	}
	
	private static class Selection {
		private int dot;
		private int mark;
		
		private void remove(int offset, int length) {
			if (dot >= offset && dot < offset + length) {
				dot = offset;
			} else {
				dot -= length;
			}
			if (mark >= offset && mark < offset + length) {
				mark = offset;
			} else {
				mark -= length;
			}
		}
		
		private void insert(int offset, int length, int documentLength) {
			int newDot = dot;
			if (newDot >= offset) {
				newDot += length;
			}
			int newMark = mark;
			if (newMark >= offset) {
				newMark += length;
			}		    
			setDot(newDot, documentLength);
			setMark(newMark, documentLength);
		}
		
		private void setDot(int dot, int length) {
			this.dot = Math.max(Math.min(dot, length), 0);
		}
		
		private void setMark(int mark, int length) {
			this.mark = Math.max(Math.min(mark, length), 0);
		}		
	}
}

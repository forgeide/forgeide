/*
 * $Id: ServerDocumentImpl.java 2833 2006-03-22 22:09:37Z sim $
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

package ch.iserver.ace.collaboration.jupiter.server;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.GapContent;
import javax.swing.text.SimpleAttributeSet;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.Fragment;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;
import ch.iserver.ace.util.CaretHandler;

/**
 * Default implementation of the ServerDocument. Implements also the
 * PortableDocument interface from the network layer. Uses the 
 * Swing Document classes to keep the structure of the document.
 */
public class ServerDocumentImpl extends AbstractDocument implements ServerDocument {

	/**
	 * The name of the attribute used to store the participant id.
	 */
	public static final String PARTICIPANT_ATTR = "participant";

	/**
	 * The mapping from participant id to caret handlers.
	 */
	private final Map carets = new HashMap();

	/**
	 * The default root object of the document.
	 */
	private BranchElement defaultRoot;
	
	/**
	 * The mapping from participant id to remote user proxy.
	 */
	private final Map participants = new TreeMap();

	/**
	 * Creates a new ServerDocumentImpl instance.
	 */
	public ServerDocumentImpl(RemoteUserProxy publisher) {
		super(new GapContent());
		defaultRoot = (BranchElement) createDefaultRoot();
		addParticipant(0, publisher);
		setCaretHandler(0, new CaretHandler(0, 0));
	}
	
	// --> utility methods <--
	
	/**
	 * Gets the participant with that participant id.
	 * 
	 * @param participantId the participant id
	 * @return the CaretHandler of that participant or null if the participant
	 *         is unknown
	 */
	private CaretHandler getCaretHandler(int participantId) {
		return (CaretHandler) carets.get(new Integer(participantId));
	}
	
	/**
	 * Sets the CaretHandler for that particular participant.
	 * 
	 * @param participantId the participant id
	 * @param handler the new CaretHandler
	 */
	private void setCaretHandler(int participantId, CaretHandler handler) {
		carets.put(new Integer(participantId), handler);
	}
	
	/**
	 * Gets the participant id from the attribute set. If the attribute set
	 * does not define a participant id, -1 is returned.
	 * 
	 * @param attr the attribute set
	 * @return the participant id or -1 if there is none
	 */
	protected int getParticipantId(AttributeSet attr) {
		int participantId = -1;
		Integer pid = (Integer) attr.getAttribute(ServerDocumentImpl.PARTICIPANT_ATTR);
		if (pid != null) {
			participantId = pid.intValue();
		}
		return participantId;
	}
	
	/**
	 * Adds a participant to the mapping.
	 * 
	 * @param participantId the id of the participant
	 * @param proxy the remote user proxy of the participant
	 */
	private void addParticipant(int participantId, RemoteUserProxy proxy) {
		participants.put(new Integer(participantId), proxy);
	}
	
	/**
	 * Removes a participant from the mapping.
	 * 
	 * @param participantId the participant to be removed
	 */
	private void removeParticipant(int participantId) {
		participants.remove(new Integer(participantId));
	}
	
	// --> AbstractDocument methods <--
	
	/**
	 * Creates the default root element of the document.
	 * 
	 * @return the default root element
	 */
	protected AbstractElement createDefaultRoot() {
		BranchElement map = (BranchElement) createBranchElement(null, null);
		Element line = createLeafElement(map, null, 0, 1);
		Element[] lines = new Element[1];
		lines[0] = line;
		map.replace(0, 0, lines);
		return map;
	}
	
	/**
	 * @see javax.swing.text.AbstractDocument#createLeafElement(javax.swing.text.Element, javax.swing.text.AttributeSet, int, int)
	 */
	protected Element createLeafElement(Element parent, AttributeSet a, int p0, int p1) {
		return new FragmentElement(parent, a, p0, p1);
	}

	/**
	 * @see javax.swing.text.Document#getDefaultRootElement()
	 */
	public Element getDefaultRootElement() {
		return defaultRoot;
	}

	/**
	 * @see javax.swing.text.AbstractDocument#getParagraphElement(int)
	 */
	public Element getParagraphElement(int pos) {
		return defaultRoot;
	}
	
	/**
	 * @see javax.swing.text.AbstractDocument#insertUpdate(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.text.AttributeSet)
	 */
	protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
		List added = new ArrayList();
		List removed = new ArrayList();
		int offset = chng.getOffset();
		int length = chng.getLength();

		int index = defaultRoot.getElementIndex(offset);
		Element candidate = defaultRoot.getElement(index);
		int start = candidate.getStartOffset();
		int end = candidate.getEndOffset();
		
		int participantId = getParticipantId(candidate.getAttributes());
		int nparticipantId = getParticipantId(attr);
		
		removed.add(candidate);

		if (participantId == nparticipantId) {
			added.add(createLeafElement(defaultRoot, attr, start, end));
			
		} else {
			if (start < offset) {
				added.add(createLeafElement(defaultRoot, candidate.getAttributes(), start, offset));
			}
			added.add(createLeafElement(defaultRoot, attr, offset, offset + length));
			if (offset + length < end) {
				added.add(createLeafElement(defaultRoot, candidate.getAttributes(), offset + length, end));
			}
		}

		Element[] addedEl = (Element[]) added.toArray(new Element[added.size()]);
		Element[] removedEl = (Element[]) removed.toArray(new Element[removed.size()]);
		defaultRoot.replace(index, removedEl.length, addedEl);
		ElementEdit edit = new ElementEdit(defaultRoot, index, removedEl, addedEl);
		chng.addEdit(edit);
		super.insertUpdate(chng, attr);
	}

	/**
	 * @see javax.swing.text.AbstractDocument#removeUpdate(javax.swing.text.AbstractDocument.DefaultDocumentEvent)
	 */
	protected void removeUpdate(DefaultDocumentEvent chng) {
		List removed = new ArrayList();		
		BranchElement map = (BranchElement) getDefaultRootElement();
		int offset = chng.getOffset();
		int length = chng.getLength();
		int doclen = chng.getDocument().getLength();
		length -= (doclen == length + offset) ? 1 : 0;
		int index0 = map.getElementIndex(offset);
		int index1 = map.getElementIndex(offset + length);
		if (index0 != index1) {
			AttributeSet attr0 = map.getElement(index0).getAttributes();
			AttributeSet attr1 = map.getElement(index1).getAttributes();
			for (int i = index0; i <= index1; i++) {
				removed.add(map.getElement(i));
			}
			int p0 = map.getElement(index0).getStartOffset();
			int p1 = map.getElement(index1).getEndOffset();
			Element[] aelems = new Element[1];
			if (p0 == offset) {
				if (index0 > 0) {
					Element el = map.getElement(index0 - 1);
					int p2 = el.getStartOffset();
					if (getParticipantId(el.getAttributes()) == getParticipantId(attr1)) {
						removed.add(0, el);
						aelems[0] = createLeafElement(map, attr1, p2, p1);
						index0--;
					} else {
						aelems[0] = createLeafElement(map, attr1, p0, p1);
					}
				} else {
					aelems[0] = createLeafElement(map, attr1, p0, p1);
				}
			} else {
				aelems[0] = createLeafElement(map, attr0, p0, p1);
			}
			Element[] relems = (Element[]) removed.toArray(new Element[removed.size()]);
			ElementEdit ee = new ElementEdit(map, index0, relems, aelems);
			chng.addEdit(ee);
			map.replace(index0, relems.length, aelems);
		}
		super.removeUpdate(chng);
	}
		
	// --> ServerDocument methods <--
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerDocument#participantJoined(int, ch.iserver.ace.net.RemoteUserProxy)
	 */
	public void participantJoined(int participantId, RemoteUserProxy proxy) {
		CaretHandler handler = new CaretHandler(0, 0);
		setCaretHandler(participantId, handler);
		addDocumentListener(handler);
		addParticipant(participantId, proxy);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerDocument#participantLeft(int)
	 */
	public void participantLeft(int participantId) {
		CaretHandler handler = getCaretHandler(participantId);
		removeDocumentListener(handler);
		setCaretHandler(participantId, null);
		removeParticipant(participantId);
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerDocument#getText()
	 */
	public String getText() {
		try {
			return getText(0, getLength());
		} catch (BadLocationException e) {
			throw new RuntimeException("internal error");
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerDocument#updateCaret(int, int, int)
	 */
	public void updateCaret(int participantId, int dot, int mark) {
		CaretHandler handler = getCaretHandler(participantId);
		if (handler == null) {
			handler = new CaretHandler(dot, mark);
			setCaretHandler(participantId, handler);
		} else {
			handler.setDot(Math.max(0, Math.min(getLength() - 1, dot)));
			handler.setMark(Math.max(0, Math.min(getLength() - 1, mark)));
		}
	}

	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerDocument#insertString(int, int, java.lang.String)
	 */
	public void insertString(int participantId, int offset, String text) {
		SimpleAttributeSet attr = new SimpleAttributeSet();
		attr.addAttribute(PARTICIPANT_ATTR, new Integer(participantId));
		try {
			insertString(offset, text, attr);
		} catch (BadLocationException e) {
			throw new ServerDocumentException(e);
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerDocument#removeString(int, int)
	 */
	public void removeString(int offset, int length) {
		try {
			remove(offset, length);
		} catch (BadLocationException e) {
			throw new ServerDocumentException(e);
		}
	}
	
	/**
	 * @see ch.iserver.ace.collaboration.jupiter.server.ServerDocument#toPortableDocument()
	 */
	public PortableDocument toPortableDocument() {
		PortableDocumentImpl result = new PortableDocumentImpl();
		addParticipants(result);
		addFragments(result);
		result.freeze();
		return result;
	}
	
	/**
	 * Adds all the participants to the copy of the document.
	 * 
	 * @param result the document beeing built
	 */
	protected void addParticipants(PortableDocumentImpl result) {
		Iterator it = participants.keySet().iterator();
		while (it.hasNext()) {
			int id = ((Integer) it.next()).intValue();
			RemoteUserProxy user = getUserProxy(id);
			CaretUpdate selection = getSelection(id);
			result.addParticipant(id, user, selection);			
		}
	}
	
	/**
	 * Adds all the fragments to the copy of the document.
	 * 
	 * @param result the document beeing built
	 */
	protected void addFragments(PortableDocumentImpl result) {
		BranchElement root = (BranchElement) getDefaultRootElement();
		Enumeration en = root.children();
		while (en.hasMoreElements()) {
			Fragment fragment = (Fragment) en.nextElement();
			if (en.hasMoreElements()) {
				result.addFragment(fragment);
			}
		}
	}
		
	/**
	 * Gets the user proxy for the given participant id.
	 * 
	 * @param participantId the participants id
	 * @return the user proxy for that participant or null
	 */
	protected RemoteUserProxy getUserProxy(int participantId) {
		return (RemoteUserProxy) participants.get(new Integer(participantId));
	}
	
	/**
	 * Gets the selection of a particular participant.
	 * 
	 * @param participantId the participants id
	 * @return the selection of the participant or null
	 */
	protected CaretUpdate getSelection(int participantId) {
		CaretHandler handler = getCaretHandler(participantId);
		if (handler != null) {
			return new CaretUpdate(handler.getDot(), handler.getMark());
		} else {
			return null;
		}
	}
	
	/**
	 * Gets an Iterator over the fragments of the document.
	 * 
	 * @return an Iterator that iterates over Fragment objects
	 */
	protected Iterator getFragments() {
		final BranchElement root = (BranchElement) getDefaultRootElement();
		return new Iterator() {
			private int idx = 0;
			public Object next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return root.getChildAt(idx++);
			}		
			public boolean hasNext() {
				return idx < root.getChildCount() - 1;
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}		
		};
	}

	/**
	 * Helper object to contain information about a participant.
	 */
	protected static class ParticipantInfo {
		
		/**
		 * The participant id. 
		 */
		private final int id;
		
		/**
		 * The remote user of the participant.
		 */
		private final RemoteUserProxy user;
		
		/**
		 * The participant's selection.
		 */
		private final CaretUpdate selection;
		
		/**
		 * Creates a new ParticipantInfo object.
		 * 
		 * @param id the participant id
		 * @param user the user
		 * @param selection the current selection
		 */
		protected ParticipantInfo(int id, RemoteUserProxy user, CaretUpdate selection) {
			this.id = id;
			this.user = user;
			this.selection = selection;
		}
		
		protected int getId() {
			return id;
		}
		
		protected CaretUpdate getSelection() {
			return selection;
		}
		
		protected RemoteUserProxy getUser() {
			return user;
		}
	}
	
	/**
	 * Implementation of the Fragment interface.
	 */
	protected static class FragmentImpl implements Fragment {
		
		/**
		 * The participant id of the fragment.
		 */
		private final int id;
		
		/**
		 * The text of the fragment.
		 */
		private final String text;
		
		/**
		 * Creates a new FragmentImpl instance.
		 * 
		 * @param id the participant id
		 * @param text the text
		 */
		protected FragmentImpl(int id, String text) {
			this.id = id;
			this.text = text;
		}
		
		/**
		 * @see ch.iserver.ace.Fragment#getParticipantId()
		 */
		public int getParticipantId() {
			return id;
		}
		
		/**
		 * @see ch.iserver.ace.Fragment#getText()
		 */
		public String getText() {
			return text;
		}
		
		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "[" + id + "|" + text + "]";
		}
	}
	
	/**
	 * Implementation of the Fragment interface which is also a Swing element
	 * used to represent the document structure.
	 */
	protected class FragmentElement extends LeafElement implements Fragment {
		
		protected FragmentElement(Element parent, AttributeSet a, int p0, int p1) {
			super(parent, a, p0, p1);
		}
		
		/**
		 * @see ch.iserver.ace.Fragment#getParticipantId()
		 */
		public int getParticipantId() {
			return ServerDocumentImpl.this.getParticipantId(getAttributes());
		}
		
		/**
		 * @see ch.iserver.ace.Fragment#getText()
		 */
		public String getText() {
			int length = getEndOffset() - getStartOffset();
			try {
				return getDocument().getText(getStartOffset(), length);
			} catch (BadLocationException e) {
				// unexpected code path
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "Fragment [pid=" + getParticipantId() + "] " + getText();
		}
		
		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof Fragment) {
				Fragment f = (Fragment) obj;
				return getText().equals(f.getText())
				        && getParticipantId() == f.getParticipantId();
			} else {
				return false;
			}
		}
		
		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return (getText() + "-" + getParticipantId()).hashCode();
		}
		
	}
		
}

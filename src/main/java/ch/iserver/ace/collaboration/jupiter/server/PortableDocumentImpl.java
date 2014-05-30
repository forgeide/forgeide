package ch.iserver.ace.collaboration.jupiter.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.Fragment;
import ch.iserver.ace.collaboration.jupiter.server.ServerDocumentImpl.FragmentImpl;
import ch.iserver.ace.collaboration.jupiter.server.ServerDocumentImpl.ParticipantInfo;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.RemoteUserProxy;

/**
 * Implementation of the PortableDocument interface from the network layer.
 */
public class PortableDocumentImpl implements PortableDocument {
	
	/**
	 * A list of ParticipantInfo objects.
	 */
	private final List participants;
	
	/**
	 * A list of Fragment objects.
	 */
	private final List fragments;
	
	/**
	 * True if the object is frozen. Changes are no longer allowed in
	 * frozen state.
	 */
	private boolean frozen;
	
	/**
	 * The array of participant ids. Initialized after the freeze method.
	 */
	private int[] participantIds;
	
	/**
	 * The mapping from participant id to user. Initialized after the
	 * freeze method.
	 */
	private Map users;
	
	/**
	 * The mapping from participant id to selection. Initialized after
	 * the freeze method.
	 */
	private Map selections;
	
	/**
	 * Creates a new PortableDocumentImpl instance.
	 */
	public PortableDocumentImpl() {
		fragments = new LinkedList();
		participants = new ArrayList();
	}
	
	/**
	 * Adds a copy of the given fragment to the document.
	 * 
	 * @param fragment the fragment to be added
	 */
	public void addFragment(Fragment fragment) {
		if (isFrozen()) {
			throw new IllegalStateException("cannot mofify in frozen state");
		}
		fragments.add(new FragmentImpl(fragment.getParticipantId(), fragment.getText()));
	}
	
	/**
	 * Adds a participant to the list of participants.
	 * 
	 * @param id the participant id
	 * @param user the user proxy
	 * @param selection the selection of that participant
	 */
	public void addParticipant(int id, RemoteUserProxy user, CaretUpdate selection) {
		if (isFrozen()) {
			throw new IllegalStateException("cannot mofify in frozen state");
		}
		participants.add(new ParticipantInfo(id, user, selection));
	}
	
	/**
	 * Freezes this object. Further modifications are not allowed.
	 */
	public void freeze() {
		this.frozen = true;
		this.participantIds = new int[participants.size()];
		this.users = new HashMap();
		this.selections = new HashMap();
		for (int i = 0; i < participantIds.length; i++) {
			ParticipantInfo info = (ParticipantInfo) participants.get(i);
			Integer key = new Integer(info.getId());
			this.participantIds[i] = info.getId();
			this.users.put(key, info.getUser());
			this.selections.put(key, info.getSelection());
		}
	}
	
	/**
	 * @return true iff this object has been freezed
	 */
	protected boolean isFrozen() {
		return frozen;
	}
	
	/**
	 * @see ch.iserver.ace.net.PortableDocument#getFragments()
	 */
	public Iterator getFragments() {
		return fragments.iterator();
	}
	
	/**
	 * @see ch.iserver.ace.net.PortableDocument#getParticipantIds()
	 */
	public int[] getParticipantIds() {
		return participantIds;
	}
	
	/**
	 * @see ch.iserver.ace.net.PortableDocument#getSelection(int)
	 */
	public CaretUpdate getSelection(int participantId) {
		return (CaretUpdate) selections.get(new Integer(participantId));
	}
	
	/**
	 * @see ch.iserver.ace.net.PortableDocument#getUserProxy(int)
	 */
	public RemoteUserProxy getUserProxy(int participantId) {
		return (RemoteUserProxy) users.get(new Integer(participantId));
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClass().getName());
		buffer.append("\n");
		int[] ids = getParticipantIds();
		for (int i = 0; i < ids.length; i++) {
			int id = ids[i];
			RemoteUserProxy user = getUserProxy(id);
			CaretUpdate update = getSelection(id);
			buffer.append(id);
			buffer.append(":");
			if (user != null) {
				buffer.append(user.getId());
			}
			buffer.append(":[");
			buffer.append(update.getDot());
			buffer.append(",");
			buffer.append(update.getMark());
			buffer.append("]\n");
		}
		buffer.append("-->\n");
		Iterator it = getFragments();
		while (it.hasNext()) {
			Fragment fragment = (Fragment) it.next();
			buffer.append(fragment);
			buffer.append("\n");
		}
		buffer.append("<--");
		return buffer.toString();
	}
}
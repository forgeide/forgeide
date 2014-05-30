/*
 * $Id: PortableDocumentImpl.java 2700 2006-01-11 11:27:08Z zbinl $
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

package ch.iserver.ace.net.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import ch.iserver.ace.CaretUpdate;
import ch.iserver.ace.Fragment;
import ch.iserver.ace.net.RemoteUserProxy;

/**
 * Default implementation of interface {@link ch.iserver.ace.net.PortableDocument} for 
 * the network layer.
 * This object contains the actual data of the document. 
 */
public class PortableDocumentImpl implements PortableDocumentExt {

	private static Logger LOG = Logger.getLogger(PortableDocumentImpl.class);
	
	/**
	 * A list of fragments
	 */
	private List fragments;
	
	/**
	 * A map of selections
	 */
	private Map selections; 	//participantId to caretupdate
	
	/**
	 * A map with the RemoteUserProxy's
	 */
	private Map proxies;		//participantId to remoteuserproxy
	
	/**
	 * The document id
	 */
	private String docId;
	
	/**
	 * The publisher id
	 */
	private String publisherId;
	
	/**
	 * The participant id of the local user
	 */
	private int participantId;
	
	/**
	 * Default constructor.
	 */
	public PortableDocumentImpl() {
		fragments = Collections.synchronizedList(new ArrayList());
		selections = Collections.synchronizedMap(new LinkedHashMap());
		proxies = Collections.synchronizedMap(new LinkedHashMap());
	}
	
	/************************************************/
	/** methods from interface PortableDocumentExt **/
	/************************************************/
	
	/**
	 * {@inheritDoc}
	 */
	public void addFragment(Fragment fragment) {
		fragments.add(fragment);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addParticipant(int id, RemoteUserProxyExt proxy) {
		proxies.put(new Integer(id), proxy);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSelection(int participantId, CaretUpdate selection) {
		selections.put(new Integer(participantId), selection);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDocumentId(String id) {
		this.docId = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDocumentId() {
		return docId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getPublisherId() {
		return publisherId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getParticipantId() {
		return participantId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setParticpantId(int id) {
		LOG.debug("setParticipantId(" + id + ")");
		this.participantId = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List getUsers() {
		return new ArrayList(proxies.values());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map getParticipantIdUserMapping() {
		return proxies;
	}
	
	/*********************************************/
	/** methods from interface PortableDocument **/
	/*********************************************/
	
	/**
	 * @see ch.iserver.ace.net.PortableDocument#getParticipantIds()
	 */
	public int[] getParticipantIds() {
		int[] ids = new int[proxies.size()];
		Iterator iter = proxies.keySet().iterator();
		int cnt = 0;
		while (iter.hasNext()) {
			Integer id = (Integer) iter.next();
			ids[cnt++] = id.intValue();
		}
		return ids;
	}

	/**
	 * @see ch.iserver.ace.net.PortableDocument#getUserProxy(int)
	 */
	public RemoteUserProxy getUserProxy(int participantId) {
		return (RemoteUserProxy) proxies.get(new Integer(participantId));
	}

	/**
	 * @see ch.iserver.ace.net.PortableDocument#getSelection(int)
	 */
	public CaretUpdate getSelection(int participantId) {
		return (CaretUpdate) selections.get(new Integer(participantId));
	}

	/**
	 * @see ch.iserver.ace.net.PortableDocument#getFragments()
	 */
	public Iterator getFragments() {
		return fragments.iterator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "PortableDocumentImpl("+docId+", "+publisherId+", "+proxies.keySet()+" participants, "+fragments.size()+" fragments, "+selections.keySet()+" selections)";
	}

}

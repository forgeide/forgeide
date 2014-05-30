/*
 * $Id: FragmentImpl.java 2424 2005-12-09 17:30:11Z zbinl $
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

import ch.iserver.ace.Fragment;

/**
 * Implementation of interface {@link ch.iserver.ace.Fragment} for
 * the network layer.
 * 
 * @see Fragment
 */
public class FragmentImpl implements Fragment {

	/**
	 * The participant id of the owner of this fragment 
	 */
	private int participantId;
	
	/**
	 * The text of this fragment
	 */
	private String text;
	
	
	/**
	 * Creates a new Fragment with the <code>participantId</code> 
	 * and the <code>text</code>.
	 * 
	 * @param participantId 	the participant id of this fragment's owner
	 * @param text			the text for this fragment
	 */
	public FragmentImpl(int participantId, String text) {
		this.participantId = participantId;
		this.text = text;
	}
	
	/*************************************/
	/** methods from interface Fragment **/
	/*************************************/
	
	/**
	 * @see ch.iserver.ace.Fragment#getParticipantId()
	 */
	public int getParticipantId() {
		return participantId;
	}

	/**
	 * @see ch.iserver.ace.Fragment#getText()
	 */
	public String getText() {
		return text;
	}

}

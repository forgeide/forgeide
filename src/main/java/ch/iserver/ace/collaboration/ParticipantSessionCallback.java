/*
 * $Id: ParticipantSessionCallback.java 1610 2005-11-22 07:52:18Z sim $
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

package ch.iserver.ace.collaboration;


/**
 * The ParticipantSessionCallback extends the SessionCallback interface and
 * adds methods used only for sessions in which the local user is a 
 * participant.
 */
public interface ParticipantSessionCallback extends SessionCallback {
	
	/**
	 * Sets the document content to the given <var>doc</var>.
	 * 
	 * @param doc the new document content
	 */
	void setDocument(PortableDocument doc);
	
	/**
	 * Called to notify the document controller that the session was
	 * terminated, that is the publisher closed the document.
	 */
	void sessionTerminated();
	
	/**
	 * Called to notify the document controller that the local user has been
	 * kicked out of the session.
	 */
	void kicked();
	
}

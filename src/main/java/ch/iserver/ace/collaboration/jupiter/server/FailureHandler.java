/*
 * $Id: FailureHandler.java 996 2005-11-07 13:54:58Z sim $
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

/**
 * Interface to be implemented by an object that is interested to be notified 
 * about failing ParticipantConnection instances.
 */
public interface FailureHandler {
	
	/**
	 * Notifies this handler that the ParticipantConnection to the given
	 * participant failed.
	 * 
	 * @param participantId the participant id of the failing connection
	 * @param reason added reason code to method signature
	 */
	void handleFailure(int participantId, int reason);
	
}

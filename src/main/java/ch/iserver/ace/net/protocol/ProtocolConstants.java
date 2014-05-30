/*
 * $Id:ProtocolConstants.java 1205 2005-11-14 07:57:10Z zbinl $
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

package ch.iserver.ace.net.protocol;

/**
 * This interface declares constants for the protocol
 * implementation of ACE. The values of the constants directly
 * match with the XML protocol message tags as defined in
 * <code>protocol.xsd</code>.
 */
public interface ProtocolConstants {
	
	/*********************************/
	/** constants for serialization **/
	/*********************************/
	public static final int NULL = -3;
	public static final int NO_TYPE = -2;
	public static final int SHUTDOWN = -1;
	public static final int PUBLISHED_DOCUMENTS = 0;
	public static final int PUBLISH = 1;
	public static final int CONCEAL = 2;
	public static final int SEND_DOCUMENTS = 3;
	public static final int DOCUMENT_DETAILS_CHANGED = 4;
	public static final int JOIN = 5;
	public static final int JOIN_DOCUMENT = 6;
	public static final int JOIN_REJECTED = 11;
	public static final int LEAVE = 7;
	public static final int KICKED = 8;
	public static final int INVITE = 9;
	public static final int INVITE_REJECTED = 10;
	public static final int USER_DISCOVERY = 11;
	public static final int USER_DISCARDED = 12;
	
	public static final int REQUEST = 100;
	public static final int CARET_UPDATE = 101;
	public static final int ACKNOWLEDGE = 102;
	public static final int PARTICIPANT_JOINED = 103;
	public static final int PARTICIPANT_LEFT = 104;
	public static final int INSERT = 105;
	public static final int DELETE = 106;
	public static final int SPLIT = 107;
	public static final int NOOP = 108;
	public static final int SESSION_TERMINATED = 109;
	
	
	public static final int CHANNEL_MAIN = 200;
	public static final int CHANNEL_SESSION = 201;
	//for future use
	public static final int CHANNEL_PROXY = 202;
	
	/********************/
	/** Top level tags **/
	/********************/
	public static final String TAG_ACE = "ace";
	public static final String TAG_NOTIFICATION = "notification";
	public static final String TAG_SESSION = "session";
	public static final String TAG_PUBLISHED_DOCS = "publishedDocs";
	public static final String TAG_PUBLISH = "publishDocs";
	public static final String TAG_CONCEAL = "concealDocs";
	public static final String TAG_JOIN = "join";
	public static final String TAG_JOIN_REJECTED = "joinRejected";
	public static final String TAG_JOIN_DOCUMENT = "document";
	public static final String TAG_DOCUMENT_DETAILS_CHANGED = "docDetailsChanged";
	public static final String TAG_DOC = "doc";
	public static final String TAG_LEAVE = "leave";
	public static final String TAG_KICKED = "kicked";
	public static final String TAG_INVITE = "invite";
	public static final String TAG_INVITE_REJECTED = "inviteRejected";
	public static final String TAG_CHANNEL = "channel";
	public static final String TAG_REASON = "reason";
	public static final String TAG_REQUEST = "request";
	public static final String TAG_RESPONSE = "response";
	public static final String TAG_OPERATION = "operation";
	public static final String TAG_INSERT = "insert";
	public static final String TAG_DELETE = "delete";
	public static final String TAG_SPLIT = "split";
	public static final String TAG_NOOP = "noop";
	public static final String TAG_TIMESTAMP = "timestamp";
	public static final String TAG_CARETUPDATE = "caretUpdate";
	public static final String TAG_PARTICIPANT_JOINED = "pJoined";
	public static final String TAG_PARTICIPANT_LEFT = "pLeft";
	public static final String TAG_ACKNOWLEDGE = "ack";
	public static final String TAG_FIRST = "first";
	public static final String TAG_SECOND = "second";
	public static final String TAG_SESSION_TERMINATED = "sessionTerminated";
	public static final String TAG_USER_DISCARDED = "userDiscarded";
	
	/***********************************/
	/** Sub-level tags 				**/
	/***********************************/
	public static final String NAME = "name";
	public static final String DOCUMENT_ID = "id";
	public static final String USER_ID = "userid";
	
	public static final String PARTICIPANTS = "participants";
	public static final String PARTICIPANT = "participant";
	public static final String PARTICIPANT_ID = "participantId";
	public static final String DOC_ID = "docId";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String ADDRESS = "address";
	public static final String PORT = "port";
	public static final String DNSSD_DISCOVERY = "dnssdDiscovered";
	public static final String DISCOVERY = "discovery";
	public static final String DATA = "data";
	public static final String SELECTION = "selection";
	public static final String MARK = "mark";
	public static final String DOT = "dot";
	public static final String TYPE = "type";
	public static final String CODE = "code";
	public static final String POSITION = "position";
	public static final String ORIGIN = "origin";
	public static final String TEXT = "text";
	public static final String ENCODED = "encoded";
	public static final String SITE_ID = "siteId";
	public static final String CARET = "caret";

	
	
}

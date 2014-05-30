/*
 * $Id:QueryInfo.java 2413 2005-12-09 13:20:12Z zbinl $
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
 * QueryInfo is a pure helper class used by the 
 * {@link ch.iserver.ace.net.protocol.ResponseParserHandler}
 * to parse and construct the response objects, respectively.
 */
public class QueryInfo {
	
	/**
	 * The id
	 */
	String id;
	
	/**
	 * the payload
	 */
	Object payload;
	
	/**
	 * the query type
	 */
	int queryType;
	
	/**
	 * Creates a new QueryInfo.
	 * 
	 * @param id			the id
	 * @param queryType	the query type
	 */
	public QueryInfo(String id, int queryType) {
		this.id = id;
		this.queryType = queryType;
	}
		
	/**
	 * Gets the payload.
	 * 
	 * @return	the payload
	 */
	public Object getPayload() {
		return payload;
	}

	/**
	 * Sets the payload.
	 * 
	 * @param payload 	the payload to set
	 */
	public void setPayload(Object payload) {
		this.payload = payload;
	}

	/**
	 * Gets the id.
	 * 
	 * @return	the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gets the query type.
	 * 
	 * @return the query type
	 */
	public int getQueryType() {
		return queryType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "QueryInfo("+id+", "+queryType+")";
	}
}
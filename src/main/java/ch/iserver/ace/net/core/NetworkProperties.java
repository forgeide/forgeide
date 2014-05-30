/*
 * $Id: NetworkProperties.java 2424 2005-12-09 17:30:11Z zbinl $
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

import java.io.IOException;
import java.util.Properties;

import ch.iserver.ace.ApplicationError;

/**
 * Static class for the central access to network properties. All properties
 * concerning the network layer are read from this class. All property values
 * are stored in a file named <code>net.properties</code>.
 */
public class NetworkProperties {

	/****************************/
	/** Protocol property keys **/
	/****************************/
	
	/**
	 * Key for the protocol port.
	 */
	public static final String KEY_PROTOCOL_PORT = "protocol.port";
	
	/**
	 * Key for the protocol version.
	 */
	public static final String KEY_PROTOCOL_VERSION = "protocol.version";
	
	
	/************************************/
	/** Bonjour zeroconf property keys **/
	/************************************/
	
	/**
	 * Key for the registration type (Bonjour specific).
	 */
	public static final String KEY_REGISTRATION_TYPE = "registration.type";
	
	/**
	 * Key for the TXT version (version TXT record protocol, Bonjour specific).
	 */
	public static final String KEY_TXT_VERSION = "txt.version";
	
	/**
	 * Key for the profile URI of the ACE protocol.
	 */
	public static final String KEY_PROFILE_URI = "profile.uri";
	

	/************************************/
	/** General properties			 **/
	/************************************/
	
	/**
	 * Key for the default encoding used by ACE.
	 */
	public static final String KEY_DEFAULT_ENCODING = "default.encoding";
	
	
	/****************************************/
	/** Keys for retry strategy properties **/
	/****************************************/
	
	/**
	 * Key for retry strategy used for DNSSD calls.
	 */
	public static final String KEY_INITIAL_WAITINGTIME = "initial.waitingtime";
	
	/**
	 * Key for retry strategy used for DNSSD calls.
	 */
	public static final String KEY_SUBSEQUENT_WAITINGTIME = "subsequent.waitingtime";
	
	/**
	 * Key for retry strategy used for DNSSD calls.
	 */
	public static final String KEY_NUMBER_OF_RETRIES = "number.retries";
	
	static Properties properties;
	
	/**
	 * Gets the value for a given key.
	 * 
	 * @param key	the key to get its value
	 * @return	the value 
	 */
	public static String get(String key) {
		if (properties == null) {
			init();
		}
		return (String)properties.get(key);
	}
	
	/**
	 * Gets the value for a given key. If the key is not 
	 * found, the defaultValue is returned.
	 * 
	 * @param key	the key to get the value
	 * @param defaultValue	the default value to return if the key is not found
	 * @return	the value for this key
	 */
	public static String get(String key, String defaultValue) {
		if (properties == null) {
			init();
		}
		return properties.getProperty(key, defaultValue);
	}
	
	/**
	 * Loads the properties.
	 */
	private static void init() {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(NetworkProperties.class.getResourceAsStream("net.properties"));
			} catch (IOException e) {
	    			throw new ApplicationError(e);
			}
		}
	}
	
	/**
	 * Main program to test the access to the property file.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println(NetworkProperties.get(KEY_DEFAULT_ENCODING, "was not found"));
	}
}

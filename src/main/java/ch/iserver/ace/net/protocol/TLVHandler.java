/*
 * $Id:TLVHandler.java 2413 2005-12-09 13:20:12Z zbinl $
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

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.jboss.logging.Logger;

import ch.iserver.ace.Fragment;
import ch.iserver.ace.net.PortableDocument;
import ch.iserver.ace.net.core.FragmentImpl;
import ch.iserver.ace.net.core.NetworkProperties;
import ch.iserver.ace.net.core.PortableDocumentExt;
import ch.iserver.ace.util.Base64;

/**
 * The TLVHandler encodes and decodes a PortableDocument to TLV.
 * 
 * @see ch.iserver.ace.net.PortableDocument
 */
public class TLVHandler {

	private static Logger LOG = Logger.getLogger(TLVHandler.class);
	
	private static final char SPACE = ' ';
	
	/**
	 * Encode a PortableDocument to a char[] array with
	 * TLV format.
	 * 
	 * @param doc the portable document to encode
	 * @return
	 */
	public static char[] create(PortableDocument doc) {
		String result = null;
		try {
			Iterator iter = doc.getFragments();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			BufferedWriter buffer = null;
			try {
				buffer = new BufferedWriter(new OutputStreamWriter(output, 
					NetworkProperties.get(NetworkProperties.KEY_DEFAULT_ENCODING)));
			} catch (UnsupportedEncodingException uee) {
				LOG.error(uee);
			}
			while (iter.hasNext()) {
				Fragment fragment = (Fragment) iter.next();
				int participantId = fragment.getParticipantId();
				char[] tag = createCharArray(participantId);
				char[] value = fragment.getText().toCharArray();
				char[] length = createCharArray(value.length);
				buffer.write(tag, 0, tag.length);
				buffer.write(SPACE);
				buffer.write(length, 0, length.length);
				buffer.write(SPACE);
				buffer.write(value, 0, value.length);
				if (iter.hasNext())
					buffer.write(SPACE);
			}
			buffer.flush();
			LOG.debug("--> encode("+output.size()+" bytes)");
			result = Base64.encodeBytes(output.toByteArray(), Base64.GZIP);
			LOG.debug("<-- encode("+result.length()+" bytes)");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("could not create TLV ["+e+", "+e.getMessage()+"]");
		}
		//TODO: TLV creation not optimal: the payload data resides 3 times in memory!!
		return result.toCharArray();
	}
	
	/**
	 * Decodes and parses, respectively, a raw data in TLV to an instance of 
	 * a PortableDocument.
	 * 
	 * @param payload
	 * @param document
	 */
	public static void parse(String payload, PortableDocumentExt document) {
		int endIndex = 0;
		int startIndex = 0;
		try {
			LOG.debug("--> decode("+payload.length()+" bytes)");
			byte[] decoded = Base64.decode(payload);
			String data = new String(decoded, NetworkProperties.get(NetworkProperties.KEY_DEFAULT_ENCODING));
			LOG.debug("<-- decode(" + data.length() + ")");
			
			 while ((endIndex = data.indexOf(SPACE, startIndex)) != -1) {
				String tag = data.substring(startIndex, endIndex++);
				startIndex = endIndex;
				endIndex = data.indexOf(SPACE, startIndex);
				String length = data.substring(startIndex, endIndex++);
				startIndex = endIndex;
				endIndex += Integer.parseInt(length);
				String value = data.substring(startIndex, endIndex++);
				Fragment fragment = new FragmentImpl(Integer.parseInt(tag), value);
				document.addFragment(fragment);
				startIndex = endIndex;
			}	
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("could not parse TLV ["+e+", "+e.getMessage()+"]");
		}
	}
	
	/**
	 * Creates a char[] from the given id.
	 * 
	 * @param id
	 * @return the char[] craeted
	 */
	private static char[] createCharArray(int id) {
		String str = Integer.toString(id);
		char[] charArr = str.toCharArray();
		return charArr;
	}
	
}

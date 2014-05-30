/*
 * $$Id: UUID.java 1205 2005-11-14 07:57:10Z zbinl $$
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

package ch.iserver.ace.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * This class is a random <code>UUID</code> generator. Use the 
 * {@link #nextUUID()} method to get a random UUID.
 * 
 */
public class UUID {
	private static final String hexChars = "0123456789abcdef";
	private static final byte INDEX_TYPE = 6;
	private static final byte INDEX_VARIATION = 8;
	private static final byte TYPE_RANDOM_BASED = 4;
	private static SecureRandom rnd;
	
	static {
		try {
			rnd = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a random UUID and returns the String representation of it.
	 * 
	 * @return a String representing a randomly generated UUID.
	 */
	public static String nextUUID() {
		// Generate 128-bit random number
		byte[] uuid = new byte[16];
		nextRandomBytes(uuid);

		// Set various bits such as type
		uuid[INDEX_TYPE] &= (byte) 0x0F;
		uuid[INDEX_TYPE] |= (byte) (TYPE_RANDOM_BASED << 4);
		uuid[INDEX_VARIATION] &= (byte) 0x3F;
		uuid[INDEX_VARIATION] |= (byte) 0x80;

		// Convert byte array into a UUID formatted string
		StringBuffer b = new StringBuffer(36);
		for (int i = 0; i < 16; i++) {
			if (i == 4 || i == 6 || i == 8 || i == 10)
				b.append('-');
			int hex = uuid[i] & 0xFF;
			b.append(hexChars.charAt(hex >> 4));
			b.append(hexChars.charAt(hex & 0x0F));
		}

		// Return UUID
		return b.toString();
	}

	/**
	 * Generates random bytes and places them into a user-supplied byte array.
	 * The number of random bytes produced is equal to the length of the byte
	 * array. Nicked from java.util.Random because the stupid SNAP board doesn't
	 * have this method!
	 * 
	 * @param bytes
	 *            the non-null byte array in which to put the random bytes.
	 */
	private static void nextRandomBytes(byte[] bytes) {
		int numRequested = bytes.length;
		int numGot = 0, rand = 0;
		while (true) {
			for (int i = 0; i < 4; i++) {
				if (numGot == numRequested)
					return;
				rand = (i == 0 ? rnd.nextInt() : rand >> 8);
				bytes[numGot++] = (byte) rand;
			}
		}
	}
}

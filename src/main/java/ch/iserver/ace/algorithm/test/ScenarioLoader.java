/*
 * $Id: ScenarioLoader.java 2430 2005-12-11 15:17:11Z sim $
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

package ch.iserver.ace.algorithm.test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Basic interface for classes that want to load scenarios. 
 */
public interface ScenarioLoader {

	/**
	 * Load a scenario, passing all the events to the given <var>builder</var>.
	 * 
	 * @param builder the builder that processes the events
	 * @param source the source from where to load the scenario
	 * @throws ScenarioLoaderException if the input is illegal or contains 
	 *         errors
	 * @throws IOException in case of IO related problems
	 */
	public void loadScenario(ScenarioBuilder builder, InputStream source) 
			throws IOException;

}

/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.utils.pinger;

import lombok.Getter;

import java.util.List;

/**
 * This class stores data about a pinged server.
 */
@Getter
public class PingerResponse {

	private String description;
	private Players players;
	private Version version;
	private String favicon;
	private int time;

	@Getter
	public static class Players {
		private int max;
		private int online;
		private List<Player> sample;
	}

	@Getter
	public static class Player {
		private String name;
		private String id;
	}

	@Getter
	public static class Version {
		private String name;
		private String protocol;
	}

}

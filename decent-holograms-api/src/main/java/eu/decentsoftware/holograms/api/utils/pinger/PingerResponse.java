package eu.decentsoftware.holograms.api.utils.pinger;

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

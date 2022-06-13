package eu.decent.holograms.profile;

import eu.decent.holograms.api.profile.Profile;

public class DefaultProfile implements Profile {

    private final String name;

    /**
     * Create a new profile for the given player.
     *
     * @param name The player's nickname.
     */
    public DefaultProfile(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}

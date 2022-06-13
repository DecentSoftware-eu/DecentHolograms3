package eu.decent.holograms.api.profile;

import eu.decent.holograms.api.utils.collection.DictRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of profiles.
 */
public abstract class ProfileRegistry extends DictRegistry<String, Profile> {

    /**
     * Creates a new profile for the given player.
     *
     * @param name The player's nickname.
     */
    public abstract void register(@NotNull String name);

}

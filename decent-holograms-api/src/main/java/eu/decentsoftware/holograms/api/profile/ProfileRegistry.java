package eu.decentsoftware.holograms.api.profile;

import eu.decentsoftware.holograms.api.utils.collection.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of profiles.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class ProfileRegistry extends Registry<String, Profile> {

    /**
     * Creates a new profile for the given player.
     *
     * @param name The player's nickname.
     */
    public abstract void register(@NotNull String name);

}

package eu.decentsoftware.holograms.api.profile;

import eu.decentsoftware.holograms.api.intent.Manager;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of profiles.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface ProfileRegistry extends Manager {

    /**
     * Creates a new profile for the given player.
     *
     * @param name The player's nickname.
     */
    void registerProfile(@NotNull String name);

    /**
     * Get the profile of the given player.
     *
     * @param name The name of the player.
     * @return The profile or null if the given player doesn't have one.
     */
    Profile getProfile(@NotNull String name);

    /**
     * Remove the profile of the given player.
     *
     * @param name The name of the player.
     */
    void removeProfile(@NotNull String name);

}

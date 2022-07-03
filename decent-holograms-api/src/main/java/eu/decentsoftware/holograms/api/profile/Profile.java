package eu.decentsoftware.holograms.api.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a profile of a player.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface Profile {

    /**
     * Get the nickname of the player owning this profile.
     *
     * @return The nickname of the player owning this profile.
     */
    @NotNull
    String getName();

    /**
     * Get the context of this profile.
     *
     * @return The context of this profile.
     * @see ProfileContext
     */
    @NotNull
    ProfileContext getContext();

    /**
     * Get the owner of the profile.
     *
     * @return The owning {@link Player} of the profile or null if the player is not online.
     */
    @Nullable
    default Player getPlayer() {
        return Bukkit.getPlayer(getName());
    }

}

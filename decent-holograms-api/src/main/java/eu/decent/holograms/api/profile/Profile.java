package eu.decent.holograms.api.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a profile of a player.
 *
 * @author d0by
 */
public interface Profile {

    /**
     * Get the nickname of the player owning this profile.
     *
     * @return The nickname of the player owning this profile.
     */
    String getName();

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

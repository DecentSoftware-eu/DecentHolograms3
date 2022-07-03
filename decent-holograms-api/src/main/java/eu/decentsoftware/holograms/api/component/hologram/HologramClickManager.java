package eu.decentsoftware.holograms.api.component.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This manager handles hologram clicks and provides a way to manage
 * the per-player clickable entity.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramClickManager {

    /**
     * Attempt to detect the currently watched line by the given player. This method will try
     * to find the line, that the player is currently watching. If the player is not watching
     * any line, this method will return false.
     *
     * @param player The player to detect the watched line for.
     * @return True if the player is watching a line in this hologram, false otherwise.
     */
    boolean attemptDetectWatchedLine(@NotNull Player player);

}

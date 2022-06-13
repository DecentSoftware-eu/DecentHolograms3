package eu.decent.holograms.api.placeholders;

import org.bukkit.entity.Player;

/**
 * This class represents a custom placeholder.
 */
public interface Placeholder {

    /**
     * Get the replacement supplier for this placeholder.
     *
     * @return the replacement supplier.
     */
    PlaceholderReplacementSupplier getReplacementSupplier();

    /**
     * Get the default replacement for this placeholder.
     *
     * @return The default replacement.
     */
    String getDefaultReplacement();

    /**
     * Get the replacement for this placeholder.
     *
     * @param player The player to get the replacement for.
     * @param argument The argument to get the replacement for.
     * @return The replacement.
     */
    String getReplacement(Player player, String argument);


}

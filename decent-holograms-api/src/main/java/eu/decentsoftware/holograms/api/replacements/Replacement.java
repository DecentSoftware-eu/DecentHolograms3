package eu.decentsoftware.holograms.api.replacements;

import org.bukkit.entity.Player;

/**
 * This class represents a custom placeholder.
 *
 * @author d0by
 */
public interface Replacement {

    /**
     * Get the replacement supplier for this placeholder.
     *
     * @return the replacement supplier.
     */
    ReplacementSupplier getReplacementSupplier();

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

package eu.decent.holograms.api.placeholders;

import org.bukkit.entity.Player;

/**
 * This functional interface represents the supplier, that finds the replacements of placeholders.
 */
@FunctionalInterface
public interface PlaceholderReplacementSupplier {

    /**
     * Get the replacement from this supplier.
     *
     * @param player The player to get it for.
     * @param argument The argument to get it for.
     * @return The replacement.
     */
    String getReplacement(Player player, String argument);

}

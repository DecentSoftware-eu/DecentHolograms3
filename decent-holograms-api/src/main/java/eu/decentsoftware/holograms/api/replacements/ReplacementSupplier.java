package eu.decentsoftware.holograms.api.replacements;

import org.bukkit.entity.Player;

/**
 * This functional interface represents the supplier, that finds the replacements of placeholders.
 *
 * @author d0by
 * @since 3.0.0
 */
@FunctionalInterface
public interface ReplacementSupplier {

    /**
     * Get the replacement from this supplier.
     *
     * @param player The player to get it for.
     * @param argument The argument to get it for.
     * @return The replacement.
     */
    String getReplacement(Player player, String argument);

}

package eu.decentsoftware.holograms.api.replacements;

import eu.decentsoftware.holograms.api.utils.collection.Registry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of placeholders.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class ReplacementRegistry extends Registry<String, Replacement> {

    /**
     * Replace all registered placeholders, that the given String contains.
     *
     * @param player The player to replace them for.
     * @param string The string.
     * @return The resulting String.
     */
    public abstract String replace(Player player, @NotNull String string);

}

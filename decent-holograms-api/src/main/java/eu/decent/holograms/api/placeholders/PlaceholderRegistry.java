package eu.decent.holograms.api.placeholders;

import eu.decent.holograms.api.utils.collection.DictRegistry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of placeholders.
 */
public abstract class PlaceholderRegistry extends DictRegistry<String, Placeholder> {

    /**
     * Replace all registered placeholders, that the given String contains.
     *
     * @param player The player to replace them for.
     * @param string The string.
     * @return The resulting String.
     */
    public abstract String replacePlaceholders(@NotNull Player player, @NotNull String string);

}

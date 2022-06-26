package eu.decentsoftware.holograms.components.line.content.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a wrapper for an entity, that can be used in a line.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@Setter
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class DecentEntity {

    // TODO: Add support for custom models, metadata, items?

    private @NotNull EntityType type;
    private boolean glowing;

    /**
     * Parse the given {@link String} to a {@link DecentEntity}. The string
     * must be in the format of an item line content.
     *
     * @param string The string to parse.
     * @return The parsed {@link DecentEntity}.
     */
    @NotNull
    public static DecentEntity fromString(@NotNull String string) {
        string = string.trim();

        String entityTypeName = string.split(" ")[0];
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(entityTypeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            entityType = EntityType.PIG;
        }
        boolean glowing = string.contains("--glowing") || string.contains("--glow");

        return new DecentEntity(entityType, glowing);
    }

}

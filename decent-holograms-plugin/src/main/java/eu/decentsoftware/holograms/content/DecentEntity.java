/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.content;

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

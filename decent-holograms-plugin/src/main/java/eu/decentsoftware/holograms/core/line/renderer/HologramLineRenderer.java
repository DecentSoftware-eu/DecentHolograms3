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

package eu.decentsoftware.holograms.core.line.renderer;

import eu.decentsoftware.holograms.api.hologram.HologramLine;
import eu.decentsoftware.holograms.api.hologram.HologramLineType;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This class represents a line renderer. A line renderer is a class that
 * stores the data of a line and renders it for a player. There are different
 * types of line renderers for different types of lines.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramLineRenderer {

    /**
     * Get the type of this line renderer.
     *
     * @return The type of this line renderer.
     * @see HologramLineType
     */
    @NonNull
    HologramLineType getType();

    /**
     * Destroy this line renderer. This method should be called when the parent
     * {@link HologramLine} is destroyed.
     */
    void destroy();

    /**
     * Get the height of the rendered line.
     *
     * @return The height of the rendered line.
     */
    double getHeight();

    /**
     * Get the width of the rendered line.
     *
     * @return The width of the rendered line.
     */
    double getWidth(@NonNull Player player);

    /**
     * Display the parent {@link HologramLine} for a player.
     *
     * @param player The player to display the line for.
     */
    void display(@NonNull Player player);

    /**
     * Hide the parent {@link HologramLine} for a player.
     *
     * @param player The player to hide the line for.
     */
    void hide(@NonNull Player player);

    /**
     * Update the contents of the parent {@link HologramLine} for a player.
     *
     * @param player The player to update the line for.
     */
    void updateContent(@NonNull Player player);

    /**
     * Teleport the parent {@link HologramLine} to a location for a player.
     *
     * @param player   The player to teleport the line to.
     */
    void updateLocation(@NonNull Player player, @NonNull Location location);

}

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

package eu.decentsoftware.holograms.api.component.page;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a hologram page. A page is a collection of lines.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramPage {

    /**
     * Get the parent {@link Hologram} of this page.
     *
     * @return The parent {@link Hologram} of this page.
     */
    @NotNull
    Hologram getParent();

    /**
     * Get the {@link HologramLineHolder} of this page.
     *
     * @return the {@link HologramLineHolder} of this page.
     * @see HologramLineHolder
     */
    @NotNull
    HologramLineHolder getLineHolder();

    /**
     * Show this page to the specified player. This method displays all the lines
     * of this page to the player.
     *
     * @param player The player to show this page to.
     */
    void display(@NotNull Player player);

    /**
     * Hide this page from the specified player. This method hides all the lines
     * of this page from the player.
     *
     * @param player The player to hide this page from.
     */
    void hide(@NotNull Player player);

    /**
     * Update this page for the specified player. This method updates all the lines
     * of this page for the player.
     *
     * @param player The player to update this page for.
     */
    void update(@NotNull Player player);

    /**
     * Teleport this page for the specified player. This method teleports all the lines
     * of this page for the player.
     *
     * @param player   The player to teleport this page for.
     * @param location The location to teleport the page to.
     */
    void teleport(@NotNull Player player, @NotNull Location location);

    /**
     * Recalculates the positions of all the lines of this page. Making sure they are
     * aligned correctly. This method also teleports the lines to the correct location
     * for each viewer.
     */
    void recalculate();

    /**
     * Recalculates the positions of all the lines of this page. Making sure they are
     * aligned correctly. This method also teleports the lines to the correct location
     * for the given player.
     */
    void recalculate(@NotNull Player player, boolean horizontal, boolean vertical, boolean heads);

    /**
     * Get the next {@link Location}, that's free to use for a line.
     *
     * @return The next {@link Location}, that's free to use for a line.
     */
    Location getNextLineLocation();

}

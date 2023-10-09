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

package eu.decentsoftware.holograms.api.hologram;

import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * This interface is used to handle clicks on a hologram.
 *
 * @author d0by
 * @since 3.0.0
 */
@FunctionalInterface
public interface HologramClickHandler {

    /**
     * Handle a click on a hologram.
     *
     * @param player    The player who clicked on the hologram.
     * @param clickType The type of the click.
     * @param page      The page that was clicked.
     * @param line      The line that was clicked.
     */
    void onClick(@NonNull Player player, @NonNull ClickType clickType, @NonNull HologramPage page, @NonNull HologramLine line);

}

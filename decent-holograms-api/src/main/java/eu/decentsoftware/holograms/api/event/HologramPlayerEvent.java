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

package eu.decentsoftware.holograms.api.event;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Event, that is fired when something happens with hologram and player.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class HologramPlayerEvent extends HologramEvent {

    private final Player player;

    /**
     * Create a new instance of {@link HologramPlayerEvent}.
     *
     * @param hologram The hologram that is being affected.
     * @param player The player that is being affected.
     */
    public HologramPlayerEvent(@NotNull Hologram hologram, @NotNull Player player) {
        super(hologram);
        this.player = player;
    }

    /**
     * Get the player that is being affected.
     *
     * @return The player that is being affected.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

}

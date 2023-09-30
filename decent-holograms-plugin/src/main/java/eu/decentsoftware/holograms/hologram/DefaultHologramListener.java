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

package eu.decentsoftware.holograms.hologram;

import eu.decentsoftware.holograms.api.hologram.HologramVisibilityManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

class DefaultHologramListener implements Listener {

    private final DefaultHologramRegistry hologramRegistry;

    public DefaultHologramListener(DefaultHologramRegistry hologramRegistry) {
        this.hologramRegistry = hologramRegistry;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        hologramRegistry.getHolograms().forEach((hologram) -> hologram.getVisibilityManager().removePlayer(event.getPlayer()));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        hideAllHologramsOnTeleport(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        hideAllHologramsOnTeleport(event.getPlayer());
    }

    /**
     * Hide all holograms for the player. They will be shown again immediately
     * but this will prevent the holograms from becoming invisible for the player
     * due to how client handles teleportation.
     *
     * @param player Player to hide the holograms for.
     * @see HologramVisibilityManager#updateVisibility(Player, boolean)
     */
    private void hideAllHologramsOnTeleport(Player player) {
        hologramRegistry.getHolograms().forEach((hologram) -> {
            //
            hologram.getVisibilityManager().updateVisibility(player, false);
        });
    }

}

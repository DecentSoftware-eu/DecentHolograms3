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

package eu.decentsoftware.holograms.internal;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
class PluginHologramListener implements Listener {

    private final PluginHologramManager manager;

    @Contract(pure = true)
    PluginHologramListener(@NonNull PluginHologramManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(@NonNull PlayerJoinEvent event) {
        // Nothing to do here, hologram visibility is updated regularly
    }

    @EventHandler
    public void onQuit(@NonNull PlayerQuitEvent event) {
        this.manager.getHolograms().forEach((hologram) -> hologram.getVisibilityManager().removePlayer(event.getPlayer()));
    }

    @EventHandler
    public void onTeleport(@NonNull PlayerTeleportEvent event) {
        // TODO: is this needed? it most likely is but only for some client versions, figure out which ones
        hideAllHologramsOnTeleport(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(@NonNull PlayerRespawnEvent event) {
        // TODO: is this needed? it most likely is but only for some client versions, figure out which ones
        hideAllHologramsOnTeleport(event.getPlayer());
    }

    /**
     * Hide all holograms for the player. They will be shown again immediately
     * but this will prevent the holograms from becoming invisible for the player
     * due to how client handles teleportation.
     *
     * @param player Player to hide the holograms for.
     */
    private void hideAllHologramsOnTeleport(@NonNull Player player) {
        this.manager.getHolograms().forEach((hologram) -> {
            //
            hologram.getVisibilityManager().updateVisibility(player);
        });
    }

}

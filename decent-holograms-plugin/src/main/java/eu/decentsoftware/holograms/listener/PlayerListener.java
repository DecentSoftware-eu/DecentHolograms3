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

package eu.decentsoftware.holograms.listener;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * This listener handles some events related to the players.
 *
 * @author d0by
 * @since 3.0.0
 */
public class PlayerListener implements Listener {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PLUGIN.getProfileRegistry().registerProfile(player.getUniqueId());
        PLUGIN.getNMSManager().hook(player);

        // -- Notify the player about a new version available
        if (Config.isUpdateAvailable() && player.hasPermission(Config.ADMIN_PERM)) {
            Lang.sendUpdateMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PLUGIN.getProfileRegistry().removeProfile(player.getUniqueId());
        PLUGIN.getNMSManager().unhook(player);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        PLUGIN.getHologramRegistry().getHolograms().forEach((hologram) -> {
            // Hide all holograms for the player. They will be shown again immediately
            // but this will prevent the holograms from becoming invisible for the player.
            hologram.getVisibilityManager().updateVisibility(player, false);
        });
    }

    @EventHandler
    public void onTeleport(PlayerDeathEvent e) {
        Player player = e.getEntity();
        PLUGIN.getHologramRegistry().getHolograms().forEach((hologram) -> {
            // Hide all holograms for the player. They will be shown again immediately
            // but this will prevent the holograms from becoming invisible for the player.
            hologram.getVisibilityManager().updateVisibility(player, false);
        });
    }

}

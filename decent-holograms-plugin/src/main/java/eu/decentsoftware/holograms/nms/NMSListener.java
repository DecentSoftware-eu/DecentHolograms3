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

package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.click.ClickType;
import eu.decentsoftware.holograms.core.CoreHologram;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.nms.event.PacketPlayInUseEntityEvent;
import eu.decentsoftware.holograms.profile.Profile;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
class NMSListener implements Listener {

    private final DecentHolograms plugin;
    private final NMSManager nmsManager;

    @Contract(pure = true)
    public NMSListener(@NonNull DecentHolograms plugin, @NonNull NMSManager nmsManager) {
        this.plugin = plugin;
        this.nmsManager = nmsManager;
    }

    @EventHandler
    public void onJoin(@NonNull PlayerJoinEvent event) {
        nmsManager.hook(event.getPlayer());
    }

    @EventHandler
    public void onQuit(@NonNull PlayerQuitEvent event) {
        nmsManager.unhook(event.getPlayer());
    }

    @EventHandler
    public void onPacketPlayInUseEntity(@NonNull PacketPlayInUseEntityEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileRegistry().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        int clickableEntityId = profile.getContext().getClickableEntityId();
        if (clickableEntityId != event.getEntityId()) {
            return;
        }

        ClickType clickType = event.getClickType();
        CoreHologramLine clickedLine = profile.getContext().getWatchedLine();
        CoreHologramPage<?> clickedPage = profile.getContext().getWatchedPage();
        CoreHologram<?> clickedHologram = profile.getContext().getWatchedHologram();
        if (clickedLine == null || clickedPage == null || clickedHologram == null) {
            return;
        }

        clickedHologram.onClick(player, clickType, clickedPage, clickedLine);
    }

}

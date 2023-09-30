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
import eu.decentsoftware.holograms.api.hologram.component.ClickType;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.nms.event.PacketPlayInUseEntityEvent;
import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PacketListener implements Listener {

    private final DecentHolograms plugin;

    public PacketListener(DecentHolograms plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPacketPlayInUseEntity(@NotNull PacketPlayInUseEntityEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileRegistry().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        int clickableEntityId = profile.getContext().getClickableEntityId();
        if (clickableEntityId != event.getEntityId()) {
            return;
        }

        HologramLine clickedLine = profile.getContext().getWatchedLine();
        if (clickedLine == null) {
            return;
        }

        ClickType clickType = event.getClickType();
        if (clickedLine.getClickHandler() != null && clickedLine.getClickHandler().onClick(player, clickType)) {
            // If line has a click handler, and it handled the click, return.
            return;
        }

        // Otherwise, pass the click to the parent page.
        if (clickedLine.getParent().getClickHandler() != null) {
            clickedLine.getParent().getClickHandler().onClick(player, clickType);
        }
    }

}

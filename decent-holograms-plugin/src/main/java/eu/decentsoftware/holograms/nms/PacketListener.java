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
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.hologram.line.DefaultHologramLine;
import eu.decentsoftware.holograms.nms.event.PacketPlayInUseEntityEvent;
import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PacketListener implements Listener {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    @EventHandler
    public void onPacketPlayInUseEntity(@NotNull PacketPlayInUseEntityEvent event) {
        Player player = event.getPlayer();
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player.getName());
        if (profile == null) {
            return;
        }

        int clickableEntityId = profile.getContext().getClickableEntityId();
        if (clickableEntityId != event.getEntityId()) {
            return;
        }

        HologramLine clickedLine = profile.getContext().getWatchedLine();
        if (!(clickedLine instanceof DefaultHologramLine)) {
            return;
        }

        // TODO
        DefaultHologramLine line = (DefaultHologramLine) clickedLine;
        if (line.getClickConditionHolder().check(profile)) {
            line.getClickActionHolder().execute(profile);
        }
    }

}

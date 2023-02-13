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
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.components.line.DefaultHologramLine;
import eu.decentsoftware.holograms.nms.listener.PacketListener;
import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public class PacketListenerImpl implements PacketListener {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    @Override
    public void handlePacketPlayInUseEntity(@NotNull Player player, int entityId, @NotNull ClickType clickType) {
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player.getName());
        if (profile == null) {
            return;
        }

        int clickableEntityId = profile.getContext().getClickableEntityId();
        if (clickableEntityId != entityId) {
            return;
        }

        HologramLine clickedLine = profile.getContext().getWatchedLine();
        if (!(clickedLine instanceof DefaultHologramLine)) {
            return;
        }

        DefaultHologramLine line = (DefaultHologramLine) clickedLine;
        if (line.getClickConditionHolder().check(profile)) {
            line.getClickActionHolder().execute(profile);
        }
    }

}

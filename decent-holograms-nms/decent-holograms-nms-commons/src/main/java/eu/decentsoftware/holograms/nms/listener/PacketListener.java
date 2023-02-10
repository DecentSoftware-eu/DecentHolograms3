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

package eu.decentsoftware.holograms.nms.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is used to handle incoming packets. It simply contains methods
 * that are called when a packet is received.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface PacketListener {

    /**
     * Handle the PacketPlayInUseEntity packet. This packet is sent when a player
     * interacts with an entity.
     *
     * @param player    The player who sent the packet.
     * @param entityId  The entity id of the entity that was interacted with.
     * @param clickType The type of click.
     */
    void handlePacketPlayInUseEntity(@NotNull Player player, int entityId, @NotNull ClickType clickType);

}

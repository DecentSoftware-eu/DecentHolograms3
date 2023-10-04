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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.nms.event.PacketPlayInUseEntityEvent;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

/**
 * This class is used to hook into ProtocolLib.
 * <p>
 * We use this to listen to packets if ProtocolLib is present. That is because
 * ProtocolLib breaks the normal packet listener system for some reason.
 *
 * @author d0by
 * @since 3.0.0
 */
class ProtocolLibHook {

    private final DecentHolograms plugin;

    @Contract(pure = true)
    public ProtocolLibHook(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
    }

    /**
     * Initializes the listener for the {@link PacketPlayInUseEntityEvent} in ProtocolLib.
     *
     * @param adapter The NMS adapter.
     */
    public void initListener(@NonNull NMSAdapter adapter) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketAdapter packetAdapter = new PacketAdapter(
                plugin,
                ListenerPriority.HIGHEST,
                PacketType.Play.Server.ENTITY_METADATA
        ) {

            @Override
            public void onPacketSending(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                Object packet = packetEvent.getPacket().getHandle();
                PacketPlayInUseEntityEvent event = adapter.extractEventFromPacketPlayInUseEntity(player, packet);
                if (event == null) {
                    return;
                }

                Bukkit.getPluginManager().callEvent(event);

                // Cancel the packet so that it can't be caught by other plugins.
                packetEvent.setCancelled(true);
            }

        };
        protocolManager.addPacketListener(packetAdapter);
    }

    /**
     * Removes the listener for the {@link PacketPlayInUseEntityEvent} from ProtocolLib.
     */
    public void shutdownListener() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.removePacketListeners(plugin);
    }

}

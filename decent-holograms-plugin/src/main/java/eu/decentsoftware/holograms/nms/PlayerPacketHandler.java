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

import eu.decentsoftware.holograms.nms.event.PacketPlayInUseEntityEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerPacketHandler extends ChannelDuplexHandler {

    public static final String IDENTIFIER = "decent_holograms_packet_handler";
    private final Player player;
    private final NMSManager nmsManager;

    public PlayerPacketHandler(Player player, NMSManager nmsManager) {
        this.player = player;
        this.nmsManager = nmsManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        PacketPlayInUseEntityEvent event = this.nmsManager.getAdapter().extractEventFromPacketPlayInUseEntity(this.player, packet);
        if (event != null) {
            Bukkit.getPluginManager().callEvent(event);
            return;
        }
        super.channelRead(ctx, packet);
    }
}

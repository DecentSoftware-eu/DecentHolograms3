package eu.decentsoftware.holograms.api.nms.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import eu.decentsoftware.holograms.api.utils.Common;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class is responsible for listening to outgoing packets.
 *
 * @author d0by
 * @since 3.0.0
 */
public class PacketListener {

    private static final NMSAdapter NMS_ADAPTER = DecentHologramsAPI.getInstance().getNMSProvider().getAdapter();
    private static final String IDENTIFIER = "DecentHolograms";
    private boolean usingProtocolLib = false;

    /**
     * Create a new instance of {@link PacketListener}. This constructor will
     * initialize the listener.
     */
    public PacketListener() {
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            // If ProtocolLib is present, use it for packet listening.
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            PacketAdapter packetAdapter = new PacketAdapter(
                    DecentHologramsAPI.getInstance(),
                    ListenerPriority.HIGHEST,
                    PacketType.Play.Server.ENTITY_METADATA) {

                @Override
                public void onPacketSending(PacketEvent packetEvent) {
                    Player player = packetEvent.getPlayer();
                    if (player != null) {
                        PacketContainer packet = packetEvent.getPacket();
                        if (PacketHandlerCommons.handlePacket(packet, player)) {
                            packetEvent.setCancelled(true);
                        }
                    }
                }

            };
            protocolManager.addPacketListener(packetAdapter);
            usingProtocolLib = true;
            Common.log("Using ProtocolLib for packet listening.");
        } else {
            hookAll();
        }
    }

    /**
     * Destroy this packet listener, unhooking all handlers.
     */
    public void destroy() {
        if (usingProtocolLib) {
            if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
                ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
                protocolManager.removePacketListeners(DecentHologramsAPI.getInstance());
                usingProtocolLib = false;
            }
        } else {
            unhookAll();
        }
    }

    /**
     * Create a packet handler for the given players. This will start listening
     * to the packets for the given player.
     *
     * @param player The player to listen to.
     * @return True if the packet handler was created, false otherwise.
     */
    public boolean hook(@NotNull Player player) {
        if (usingProtocolLib) {
            return true;
        }

        try {
            ChannelPipeline pipeline = NMS_ADAPTER.getPipeline(player);
            if (pipeline.get(IDENTIFIER) == null) {
                ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                        if (!PacketHandlerCommons.handlePacket(packet, player)) {
                            super.channelRead(channelHandlerContext, packet);
                        }
                    }
                };
                pipeline.addBefore("packet_handler", IDENTIFIER, channelDuplexHandler);
            }
            return true;
        } catch (Exception ignored) {
        }
        return true;
    }

    /**
     * Hook all players.
     *
     * @see #hook(Player)
     */
    public void hookAll() {
        if (!usingProtocolLib) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                hook(player);
            }
        }
    }

    /**
     * Stop listening to the packets for the given player.
     *
     * @param player The player to stop listening to.
     * @return True if the packet handler was destroyed, false otherwise.
     */
    public boolean unhook(@NotNull Player player) {
        if (usingProtocolLib) {
            return true;
        }

        try {
            ChannelPipeline pipeline = NMS_ADAPTER.getPipeline(player);
            if (pipeline.get(IDENTIFIER) != null) {
                pipeline.remove(IDENTIFIER);
            }
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Unhook all players.
     *
     * @see #unhook(Player)
     */
    public void unhookAll() {
        if (!usingProtocolLib) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                unhook(player);
            }
        }
    }

}
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
import eu.decentsoftware.holograms.nms.utils.Version;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * This class is responsible for initializing the NMS adapter,
 * handling the packet listener and providing access to them.
 *
 * @author d0by
 * @since 3.0.0
 */
public class NMSManager {

    private final DecentHolograms plugin;
    private final NMSAdapter adapter;
    private final NMSListener listener;
    private ProtocolLibHook protocolLibHook;

    /**
     * Initializes the NMS adapter. If the version is not supported, an exception is thrown.
     *
     * @throws IllegalStateException If the current version is not supported.
     */
    public NMSManager(@NonNull DecentHolograms plugin) throws IllegalStateException {
        this.plugin = plugin;
        this.adapter = initNMSAdapter();
        if (this.adapter == null) {
            throw new IllegalStateException(String.format("Version %s is not supported!", Version.CURRENT.name()));
        }
        this.listener = new NMSListener(plugin, this);
        Bukkit.getPluginManager().registerEvents(this.listener, plugin);
    }

    /**
     * Reload the NMS manager, re-hooking all online players.
     */
    public void reload() {
        hookAll();
        // TODO: Test without ProtocolLib to see if the support is even needed.
//        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
//            if (this.protocolLibHook == null) {
//                this.protocolLibHook = new ProtocolLibHook(this.plugin);
//            }
//            this.protocolLibHook.initListener(this.adapter);
//        } else {
//            hookAll();
//        }
    }

    /**
     * Shutdown the NMS manager, unhooking all players. This will make this
     * manager unusable. After calling this method, this manager should not
     * be referenced anymore.
     */
    public void shutdown() {
        HandlerList.unregisterAll(this.listener);

        unhookAll();
//        if (usingProtocolLib()) {
//            this.protocolLibHook.shutdownListener();
//        } else {
//            unhookAll();
//        }
    }

    /**
     * Create a packet handler for the given players. This will start listening
     * to the packets for the given player.
     * <p>
     * If the player already has a packet handler, it will be replaced.
     *
     * @param player The player to listen to.
     */
    public void hook(@NonNull Player player) {
        if (usingProtocolLib()) {
            return;
        }

        executeOnPipeline(player, pipeline -> {
            if (pipeline.get(PlayerPacketHandler.IDENTIFIER) != null) {
                pipeline.remove(PlayerPacketHandler.IDENTIFIER);
            }
            pipeline.addBefore(
                    "packet_handler",
                    PlayerPacketHandler.IDENTIFIER,
                    new PlayerPacketHandler(player, this)
            );
        });
    }

    public void hookAll() {
        if (usingProtocolLib()) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            hook(player);
        }
    }

    /**
     * Stop listening to the packets for the given player.
     *
     * @param player The player to stop listening to.
     */
    public void unhook(@NonNull Player player) {
        if (usingProtocolLib()) {
            return;
        }

        executeOnPipeline(player, pipeline -> {
            if (pipeline.get(PlayerPacketHandler.IDENTIFIER) != null) {
                pipeline.remove(PlayerPacketHandler.IDENTIFIER);
            }
        });
    }

    public void unhookAll() {
        if (!usingProtocolLib()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                unhook(player);
            }
        }
    }

    private void executeOnPipeline(@NonNull Player player, @NonNull Consumer<ChannelPipeline> operation) {
        try {
            if (!player.isOnline()) {
                return;
            }

            ChannelPipeline pipeline = this.adapter.getPipeline(player);
            EventLoop eventLoop = pipeline.channel().eventLoop();

            if (eventLoop.inEventLoop()) {
                operation.accept(pipeline);
            } else {
                eventLoop.execute(() -> executeOnPipeline(player, operation));
            }
        } catch (Exception e) {
            this.plugin.getLogger().warning("Failed to execute operation on " + player.getName() + "'s channel pipeline!");
            e.printStackTrace();
        }
    }

    public NMSAdapter getAdapter() {
        return this.adapter;
    }

    private boolean usingProtocolLib() {
        return false;//Bukkit.getPluginManager().isPluginEnabled("ProtocolLib") && this.protocolLibHook != null;
    }

    @Nullable
    private NMSAdapter initNMSAdapter() {
        String version = Version.CURRENT.name();
        String className = "eu.decentsoftware.holograms.nms.NMSAdapter_" + version;
        try {
            Class<?> clazz = Class.forName(className);
            return (NMSAdapter) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}

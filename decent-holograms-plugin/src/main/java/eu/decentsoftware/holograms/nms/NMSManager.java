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
import eu.decentsoftware.holograms.nms.utils.Version;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is responsible for initializing the NMS adapter,
 * handling the packet listener and providing access to them.
 *
 * @author d0by
 * @since 3.0.0
 */
public class NMSManager {

	private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
	private static final String IDENTIFIER = "DecentHolograms";
	@Getter
	private static NMSManager instance;

	@Getter
	private final @NonNull NMSAdapter adapter;
	private boolean usingProtocolLib = false;

	/**
	 * Initializes the NMS adapter. If the version is not supported, an exception is thrown.
	 *
	 * @throws IllegalStateException If the current version is not supported.
	 */
	public NMSManager() throws IllegalStateException {
		if (instance != null) {
			throw new IllegalStateException("NMSManager is already initialized!");
		}
		instance = this;
		NMSAdapter adapter;
		if ((adapter = initNMSAdapter()) == null) {
			throw new IllegalStateException(String.format("Version %s is not supported!", Version.CURRENT.name()));
		}
		this.adapter = adapter;
	}

	/**
	 * (Re)initialize the packet listener.
	 */
	public void reload() {
		this.shutdown();

		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			// If ProtocolLib is present, use it for packet listening.
			ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
			PacketAdapter packetAdapter = new PacketAdapter(
					PLUGIN,
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

					if (event.isCancelled()) {
						packetEvent.setCancelled(true);
					}
				}

			};
			protocolManager.addPacketListener(packetAdapter);
			usingProtocolLib = true;
		} else {
			hookAll();
		}
	}

	/**
	 * Shutdown the NMS manager, unhooking all players.
	 */
	public void shutdown() {
		if (usingProtocolLib) {
			if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
				ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
				protocolManager.removePacketListeners(PLUGIN);
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
			ChannelPipeline pipeline = adapter.getPipeline(player);
			if (pipeline.get(IDENTIFIER) == null) {
				ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
					@Override
					public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
						PacketPlayInUseEntityEvent event = adapter.extractEventFromPacketPlayInUseEntity(player, packet);
						if (event == null) {
							super.channelRead(channelHandlerContext, packet);
							return;
						}

						Bukkit.getPluginManager().callEvent(event);

						if (!event.isCancelled()) {
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
			ChannelPipeline pipeline = adapter.getPipeline(player);
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

	/**
	 * Attempts to find the correct NMS adapter for the current version.
	 *
	 * @return The NMS adapter or null if none was found.
	 */
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

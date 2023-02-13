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

import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * This class represents a NMS adapter. It is responsible for adapting
 * to different versions of Minecraft. There is one adapter for each
 * supported Minecraft version.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public interface NMSAdapter {

	/*
	 *  General
	 */

	/**
	 * Sends a packet to a player.
	 *
	 * @param player The player to send the packet to.
	 * @param packet The packet to send.
	 */
	void sendPacket(@NotNull Player player, Object packet);

	/**
	 * Sends a packet to all online players.
	 *
	 * @param packet The packet to send.
	 */
	default void sendGlobalPacket(Object packet) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			sendPacket(player, packet);
		}
	}

	/**
	 * Sends a packet to all players in a world.
	 *
	 * @param world  The world to send the packet to.
	 * @param packet The packet to send.
	 */
	default void sendWorldPacket(@NotNull World world, Object packet) {
		for (Player player : world.getPlayers()) {
			sendPacket(player, packet);
		}
	}

	/**
	 * Sends a packet to all players in a world within
	 * a certain radius around a location.
	 *
	 * @param radius The radius to send the packet to.
	 * @param l      The location to send the packet to.
	 * @param packet The packet to send.
	 */
	default void sendRangedPacket(double radius, @NotNull Location l, Object packet) {
		World world = l.getWorld();
		if (world == null) {
			return;
		}

		for (Player player : world.getPlayers()) {
			if (player.getLocation().distanceSquared(l) < radius * radius) {
				sendPacket(player, packet);
			}
		}
	}

	/*
	 *  Player
	 */

	/**
	 * Get the player's channel pipeline.
	 *
	 * @param player The player to get the pipeline of.
	 * @return The player's channel pipeline.
	 */
	ChannelPipeline getPipeline(@NotNull Player player);

	/*
	 *  Packets
	 */

	void sendRedstoneParticle(Player player, Color c, Location l, float size);

	Object updateTimePacket(long worldAge, long day);

	Object packetGameState(int mode, float value);

	Object packetTimes(int in, int stay, int out);

	Object packetTitleMessage(String text);

	Object packetSubtitleMessage(String text);

	Object packetActionbarMessage(String text);

	Object packetJsonMessage(String text);

	Object packetResetTitle();

	Object packetClearTitle();

	Object packetHeaderFooter(String header, String footer);

	Object packetEntityAnimation(int eid, int animation);

	Object packetBlockAction(Location l, int action, int param, int blockType);

	Object packetBlockChange(Location l, int blockId, byte blockData);

	Object packetTeleportEntity(int eid, Location l, boolean onGround);

	/*
	 *  Entity Metadata
	 */

	void sendEntityMetadata(Player player, int eid, Object... objects);

	void sendEntityMetadata(Player player, int eid, List<?> objects);

	Object getMetaCustom(int id, Class<?> type, Object value);

	Object getMetaEntityRemainingAir(int airTicksLeft);

	/**
	 * Create a new entity metadata object, that represents the entities custom name.
	 *
	 * @param name The name.
	 * @return The entity metadata object.
	 */
	Object getMetaEntityCustomName(String name);

	/**
	 * Create a new entity metadata object, that represents the entities custom name.
	 * <p>
	 * This method requires the name to be serialized to a IChatBaseComponent.
	 *
	 * @param name The name as IChatBaseComponent.
	 * @return The entity metadata object.
	 */
	Object getMetaEntityCustomName(Object name);

	Object getMetaEntityCustomNameVisible(boolean visible);

	Object getMetaEntitySilenced(boolean silenced);

	Object getMetaEntityGravity(boolean gravity);

	Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming,
	                               boolean invisible, boolean glowing, boolean flyingElytra);

	Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker);

	Object getMetaItemStack(ItemStack itemStack);

	/*
	 *  Entity
	 */

	int getFreeEntityId();

	int getEntityTypeId(EntityType type);

	double getEntityHeight(EntityType type);

	void spawnEntity(Player player, int eid, UUID id, EntityType type, Location l);

	void spawnEntityLiving(Player player, int eid, UUID id, EntityType type, Location l);

	void setEquipment(Player player, int eid, EntityEquipmentSlot slot, ItemStack itemStack);

	void teleportEntity(Player player, int eid, Location l, boolean onGround);

	void updatePassengers(Player player, int eid, int... passengers);

	void removeEntity(Player player, int eid);

}

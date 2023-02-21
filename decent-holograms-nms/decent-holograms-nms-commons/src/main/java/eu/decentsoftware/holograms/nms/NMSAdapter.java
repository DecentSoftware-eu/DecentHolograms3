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

import eu.decentsoftware.holograms.nms.utils.EntityEquipmentSlot;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
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

    /**
     * Create a packet to update the world time for a player.
     *
     * @param worldAge The world age.
     * @param day      The day.
     * @return The packet.
     */
    Object updateTimePacket(long worldAge, long day);

    /**
     * Create a packet to update the game state for a player.
     *
     * @param mode  The game state mode.
     * @param value The game state value.
     * @return The packet.
     * @see <a href="https://wiki.vg/Protocol#Game_Event">https://wiki.vg/Protocol#Game_Event</a>
     */
    Object packetGameState(int mode, float value);

    /**
     * Create a packet to update the title times for a player.
     *
     * @param in   The fade in time.
     * @param stay The stay time.
     * @param out  The fade out time.
     * @return The packet.
     */
    Object packetTimes(int in, int stay, int out);

    /**
     * Create a packet to update the title for a player.
     *
     * @param text The title text.
     * @return The packet.
     */
    Object packetTitleMessage(String text);

    /**
     * Create a packet to update the subtitle for a player.
     *
     * @param text The subtitle text.
     * @return The packet.
     */
    Object packetSubtitleMessage(String text);

    /**
     * Create a packet to update the action bar for a player.
     *
     * @param text The action bar text.
     * @return The packet.
     */
    Object packetActionbarMessage(String text);

    /**
     * Create a packet to send a json message to a player.
     *
     * @param text The json message.
     * @return The packet.
     */
    Object packetJsonMessage(String text);

    /**
     * Create a packet to reset the title for a player.
     *
     * @return The packet.
     */
    Object packetResetTitle();

    /**
     * Create a packet to clear the title for a player.
     *
     * @return The packet.
     */
    Object packetClearTitle();

    /**
     * Create a packet to update the player list header and footer for a player.
     *
     * @param header The header text.
     * @param footer The footer text.
     * @return The packet.
     */
    Object packetHeaderFooter(String header, String footer);

    /**
     * Create a packet to play entity animation for a player.
     *
     * @param eid       The entity id.
     * @param animation The animation id.
     * @return The packet.
     */
    Object packetEntityAnimation(int eid, int animation);

    /**
     * Create a packet to play a block action for a player.
     *
     * @param l         The location of the block.
     * @param action    The action id.
     * @param param     The parameter.
     * @param blockType The block type.
     * @return The packet.
     */
    Object packetBlockAction(Location l, int action, int param, int blockType);

    /**
     * Create a packet to play a block change for a player.
     *
     * @param l         The location of the block.
     * @param blockId   The block id.
     * @param blockData The block data.
     * @return The packet.
     */
    Object packetBlockChange(Location l, int blockId, byte blockData);

    /*
     *  Entity Metadata
     */

    /**
     * Send the packet to update the entity metadata to the player.
     * <p>
     * You can get the entity metadata object by using the methods
     * in this class, starting with "getMeta".
     *
     * @param player  The player to send the packet to.
     * @param eid     The entity id.
     * @param objects The entity metadata objects.
     */
    void sendEntityMetadata(Player player, int eid, Object... objects);

    /**
     * Send the packet to update the entity metadata to the player.
     * <p>
     * You can get the entity metadata object by using the methods
     * in this class, starting with "getMeta".
     *
     * @param player  The player to send the packet to.
     * @param eid     The entity id.
     * @param objects The entity metadata objects.
     */
    void sendEntityMetadata(Player player, int eid, List<?> objects);

    /**
     * Create a new entity metadata object, that represents a custom metadata value.
     *
     * @param id    The id of the metadata object.
     * @param type  The type of the metadata object.
     * @param value The value of the metadata object.
     * @return The entity metadata object.
     */
    Object getMetaCustom(int id, Class<?> type, Object value);

    /**
     * Create a new entity metadata object, that represents the remaining air ticks
     * of an entity.
     *
     * @param airTicksLeft The remaining air ticks.
     * @return The entity metadata object.
     */
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

    /**
     * Create a new entity metadata object, that represents the entities custom name
     * visibility.
     *
     * @param visible Whether the name is visible.
     * @return The entity metadata object.
     */
    Object getMetaEntityCustomNameVisible(boolean visible);

    /**
     * Create a new entity metadata object, that represents the entities silent state.
     *
     * @param silenced Whether the entity is silent.
     * @return The entity metadata object.
     */
    Object getMetaEntitySilenced(boolean silenced);

    /**
     * Create a new entity metadata object, that represents the entities no gravity state.
     *
     * @param gravity Whether the entity has gravity.
     * @return The entity metadata object.
     */
    Object getMetaEntityGravity(boolean gravity);

    /**
     * Create a new entity metadata object, that represents the entities properties.
     *
     * @param onFire       Whether the entity is on fire.
     * @param crouched     Whether the entity is crouched.
     * @param sprinting    Whether the entity is sprinting.
     * @param swimming     Whether the entity is swimming.
     * @param invisible    Whether the entity is invisible.
     * @param glowing      Whether the entity is glowing. (1.9+)
     * @param flyingElytra Whether the entity is flying with an elytra. (1.9+)
     * @return The entity metadata object.
     */
    Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming,
                                   boolean invisible, boolean glowing, boolean flyingElytra);

    /**
     * Create a new entity metadata object, that represents the properties of an armor stand.
     *
     * @param small       Whether the armor stand is small.
     * @param arms        Whether the armor stand has arms.
     * @param noBasePlate Whether the armor stand has a baseplate.
     * @param marker      Whether the armor stand is a marker. (Has no hitbox)
     * @return The entity metadata object.
     */
    Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker);

    /**
     * Create a new entity metadata object, that represents an item stack.
     * <p>
     * This is used for the Item entity, to define the item that is dropped.
     *
     * @param itemStack The item stack.
     * @return The entity metadata object.
     */
    Object getMetaItemStack(ItemStack itemStack);

    /*
     *  Entity
     */

    /**
     * Get a free, unique entity id.
     * <p>
     * All entity ids coming from this method are unique and will not be used
     * by any other entity. This is because the entity ids are generated by
     * the server and are not reused.
     *
     * @return The free entity id.
     */
    int getFreeEntityId();

    /**
     * Get the entity type id of a certain entity type.
     *
     * @param type The entity type.
     * @return The entity type id.
     */
    int getEntityTypeId(EntityType type);

    /**
     * Get the entity height of a certain entity type.
     *
     * @param type The entity type.
     * @return The entity height.
     */
    double getEntityHeight(EntityType type);

    /**
     * Send the spawn packet for an entity to the given player. This spawns
     * the entity at the given location only for the given player.
     *
     * @param player The player to spawn the entity for.
     * @param eid    The entity id.
     * @param id     The entity uuid.
     * @param type   The entity type.
     * @param l      The location to spawn the entity at.
     */
    void spawnEntity(Player player, int eid, UUID id, EntityType type, Location l);

    /**
     * Send the spawn packet for a living entity to the given player. This spawns
     * the entity at the given location only for the given player.
     *
     * @param player The player to spawn the entity for.
     * @param eid    The entity id.
     * @param id     The entity uuid.
     * @param type   The entity type.
     * @param l      The location to spawn the entity at.
     */
    void spawnEntityLiving(Player player, int eid, UUID id, EntityType type, Location l);

    /**
     * Send the packet to update the equipment of an entity to the given player.
     *
     * @param player    The player to send the packet to.
     * @param eid       The entity id.
     * @param slot      The slot to update.
     * @param itemStack The item stack to set.
     */
    void setEquipment(Player player, int eid, EntityEquipmentSlot slot, ItemStack itemStack);

    /**
     * Send the packet to teleport an entity to the given player.
     *
     * @param player   The player to send the packet to.
     * @param eid      The entity id.
     * @param l        The location to teleport the entity to.
     * @param onGround Whether the entity is on the ground.
     */
    void teleportEntity(Player player, int eid, Location l, boolean onGround);

    /**
     * Send the packet to update the passengers of an entity to the given player.
     *
     * @param player     The player to send the packet to.
     * @param eid        The entity id.
     * @param passengers The passengers to set.
     */
    void updatePassengers(Player player, int eid, int... passengers);

    /**
     * Send the packet to destroy an entity to the given player.
     *
     * @param player The player to send the packet to.
     * @param eid    The entity id.
     */
    void removeEntity(Player player, int eid);

}

package eu.decentsoftware.holograms.api.nms;

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
 * This class represents a NMS adapter. It is responsible for adapting to different versions of Minecraft.
 * There is one adapter for each supported Minecraft version.
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
     * Sends a packet to all players in a world within a certain radius around a location.
     *
     * @param radius The radius to send the packet to.
     * @param l      The location to send the packet to.
     * @param packet The packet to send.
     */
    default void sendRangedPacket(double radius, @NotNull Location l, Object packet) {
        double radiusSquared = radius * radius;
        for (Player player : l.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(l) < radiusSquared) {
                sendPacket(player, packet);
            }
        }
    }

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

    void sendEntityMetadata(Player player, int eid, List<Object> objects);

    Object getMetaCustom(int id, Class<?> type, Object value);

    Object getMetaEntityRemainingAir(int airTicksLeft);

    Object getMetaEntityCustomName(String name);

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

    int getEntityTypeId(EntityType type);

    double getEntityHeight(EntityType type);

    void spawnEntity(Player player, int eid, UUID id, EntityType type, Location l);

    void spawnEntityLiving(Player player, int eid, UUID id, EntityType type, Location l);

    void setHelmet(Player player, int eid, ItemStack itemStack);

    void teleportEntity(Player player, int eid, Location l, boolean onGround);

    void updatePassengers(Player player, int eid, int... passengers);

    void removeEntity(Player player, int eid);

}

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

import com.google.common.base.Optional;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NMSAdapter_v1_9_R1 implements NMSAdapter {

    private static final Field ENTITY_COUNT_FIELD;

    static {
        try {
            ENTITY_COUNT_FIELD = Entity.class.getDeclaredField("entityCount");
            ENTITY_COUNT_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializer for packet data.
     *
     * @implNote We use this one instance for all packets, clearing it after each use. This is
     * because we don't want to create a new instance every time we need to send a packet.
     */
    private final PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());

    /*
     *  Utils
     */

    private IChatBaseComponent s(String s) {
        return IChatBaseComponent.ChatSerializer.a(s);
    }

    private String c(IChatBaseComponent c) {
        return IChatBaseComponent.ChatSerializer.a(c);
    }

    private ItemStack i(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    private BlockPosition blockPos(@NotNull Location l) {
        return new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    private EnumItemSlot slot(EntityEquipmentSlot slot) {
        switch (slot) {
            case MAINHAND:
                return EnumItemSlot.MAINHAND;
            case OFFHAND:
                return EnumItemSlot.OFFHAND;
            case FEET:
                return EnumItemSlot.FEET;
            case LEGS:
                return EnumItemSlot.LEGS;
            case CHEST:
                return EnumItemSlot.CHEST;
            case HEAD:
                return EnumItemSlot.HEAD;
            default:
                break;
        }
        return null; // This should never happen...
    }

    /*
     *  General
     */

    @Override
    public void sendPacket(@NotNull Player player, Object packet) {
        if (packet instanceof Packet) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
        }
    }

    /*
     *  Player
     */

    @Override
    public ChannelPipeline getPipeline(@NotNull Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }

    /*
     *  Packets
     */

    @Override
    public Object updateTimePacket(long worldAge, long day) {
        return new PacketPlayOutUpdateTime(worldAge, day, true);
    }

    @Override
    public Object packetGameState(int mode, float value) {
        return new PacketPlayOutGameStateChange(mode, value);
    }

    @Override
    public Object packetTimes(int in, int stay, int out) {
        return new PacketPlayOutTitle(in, stay, out);
    }

    @Override
    public Object packetTitleMessage(String text) {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, s(text));
    }

    @Override
    public Object packetSubtitleMessage(String text) {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, s(text));
    }

    @Override
    public Object packetActionbarMessage(String text) {
        return new PacketPlayOutChat(s(text), (byte) 2);
    }

    @Override
    public Object packetJsonMessage(String text) {
        return new PacketPlayOutChat(s(text));
    }

    @Override
    public Object packetResetTitle() {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null);
    }

    @Override
    public Object packetClearTitle() {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, null);
    }

    @Override
    public Object packetHeaderFooter(String header, String footer) {
        serializer.clear();
        serializer.a(s(header));
        serializer.a(s(footer));

        try {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            packet.b(serializer);
            return packet;
        } catch (Exception e) {
            DecentHologramsAPI.getInstance().getLogger().warning("Failed to create header/footer packet: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Object packetEntityAnimation(int eid, int animation) {
        serializer.clear();
        serializer.b(eid);
        serializer.writeByte(animation);

        try {
            PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
            packet.a(serializer);
            return packet;
        } catch (Exception e) {
            DecentHologramsAPI.getInstance().getLogger().warning("Failed to create entity animation packet: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Object packetBlockAction(Location l, int action, int param, int blockType) {
        return new PacketPlayOutBlockAction(blockPos(l), Block.getByCombinedId(blockType).getBlock(), action, param);
    }

    @Override
    public Object packetBlockChange(Location l, int blockId, byte blockData) {
        serializer.clear();
        serializer.a(blockPos(l));
        serializer.b(blockId << 4 | (blockData & 15));

        try {
            PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange();
            packet.a(serializer);
            return packet;
        } catch (Exception e) {
            DecentHologramsAPI.getInstance().getLogger().warning("Failed to create block change packet: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Object packetTeleportEntity(int eid, Location l, boolean onGround) {
        serializer.clear();
        serializer.b(eid);
        serializer.writeDouble(l.getX());
        serializer.writeDouble(l.getY());
        serializer.writeDouble(l.getZ());
        serializer.writeByte((byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        serializer.writeByte((byte) ((int) (l.getPitch() * 256.0F / 360.0F)));
        serializer.writeBoolean(onGround);

        try {
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
            packet.a(serializer);
            return packet;
        } catch (Exception e) {
            DecentHologramsAPI.getInstance().getLogger().warning("Failed to create teleport entity packet: " + e.getMessage());
            return null;
        }
    }

    /*
     *  Entity Metadata
     */

    @Override
    public void sendEntityMetadata(Player player, int eid, Object... objects) {
        sendEntityMetadata(player, eid, Arrays.asList(objects));
    }

    @Override
    public void sendEntityMetadata(Player player, int eid, List<?> objects) {
        try {
            serializer.clear();
            serializer.b(eid);
            DataWatcher.a((List<DataWatcher.Item<?>>) objects, serializer);

            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
            packet.a(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            DecentHologramsAPI.getInstance().getLogger().warning("Failed to send entity metadata packet: " + e.getMessage());
        }
    }

    @Override
    public Object getMetaCustom(int id, Class<?> type, Object value) {
        return null;
    }

    @Override
    public Object getMetaEntityRemainingAir(int airTicksLeft) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(1, DataWatcherRegistry.b), airTicksLeft);
    }

    @Override
    public Object getMetaEntityCustomName(String name) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(2, DataWatcherRegistry.e), s(name));
    }

    @Override
    public Object getMetaEntityCustomName(Object name) {
        if (!(name instanceof IChatBaseComponent)) {
            return null;
        }
        return new DataWatcher.Item<>(new DataWatcherObject<>(2, DataWatcherRegistry.e), (IChatBaseComponent) name);
    }

    @Override
    public Object getMetaEntityCustomNameVisible(boolean visible) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(3, DataWatcherRegistry.h), visible);
    }

    @Override
    public Object getMetaEntitySilenced(boolean silenced) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(4, DataWatcherRegistry.h), silenced);
    }

    @Override
    public Object getMetaEntityGravity(boolean gravity) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(5, DataWatcherRegistry.h), gravity);
    }

    @Override
    public Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra) {
        byte data = 0x00;
        data += onFire ? 0x01 : 0x00;
        data += crouched ? 0x02 : 0x00;
        data += sprinting ? 0x08 : 0x00;
        data += swimming ? 0x10 : 0x00;
        data += invisible ? 0x20 : 0x00;
        data += glowing ? 0x40 : 0x00;
        data += flyingElytra ? 0x80 : 0x00;
        return new DataWatcher.Item<>(new DataWatcherObject<>(0, DataWatcherRegistry.a), data);
    }

    @Override
    public Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker) {
        byte data = 0x00;
        data += small ? 0x01 : 0x00;
        data += arms ? 0x02 : 0x00;
        data += noBasePlate ? 0x08 : 0x00;
        data += marker ? 0x10 : 0x00;
        return new DataWatcher.Item<>(new DataWatcherObject<>(13, DataWatcherRegistry.a), data);
    }

    @Override
    public Object getMetaItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(8, DataWatcherRegistry.f), Optional.fromNullable(i(itemStack)));
    }

    /*
     *  Entity
     */

    @Override
    public int getFreeEntityId() {
        try {
            /*
             * We are getting the new entity ids the same way as the server does. This is to ensure
             * that the ids are unique and don't conflict with any other entities.
             */
            int entityCount = ENTITY_COUNT_FIELD.getInt(null);
            ENTITY_COUNT_FIELD.setInt(null, entityCount + 1);
            return entityCount;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get new entity ID", e);
        }
    }

    @Override
    public int getEntityTypeId(EntityType type) {
        // noinspection deprecation
        return EntityTypes.a(type.getName());
    }

    @Override
    public double getEntityHeight(EntityType type) {
        return 0;
    }

    @Override
    public void spawnEntity(Player player, int eid, UUID id, EntityType type, Location l) {
        serializer.clear();
        serializer.b(eid);
        serializer.a(id);
        serializer.writeByte(getEntityTypeId(type));
        serializer.writeDouble(l.getX());
        serializer.writeDouble(l.getY());
        serializer.writeDouble(l.getZ());
        serializer.writeByte(MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(l.getPitch() * 256.0F / 360.0F));
        serializer.writeInt(1);
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);

        try {
            PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
            packet.a(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            DecentHologramsAPI.getInstance().getLogger().warning("Failed to send entity spawn packet: " + e.getMessage());
        }
    }

    @Override
    public void spawnEntityLiving(Player player, int eid, UUID id, EntityType type, Location l) {
        serializer.clear();
        serializer.b(eid);
        serializer.a(id);
        serializer.writeByte(getEntityTypeId(type));
        serializer.writeDouble(l.getX());
        serializer.writeDouble(l.getY());
        serializer.writeDouble(l.getZ());
        serializer.writeByte(MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(l.getPitch() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);

        try {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
            packet.a(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            DecentHologramsAPI.getInstance().getLogger().warning("Failed to send entity spawn packet: " + e.getMessage());
        }
    }

    @Override
    public void setEquipment(Player player, int eid, EntityEquipmentSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(eid, slot(slot), i(itemStack));
        sendPacket(player, packet);
    }

    @Override
    public void teleportEntity(Player player, int eid, Location l, boolean onGround) {
        sendPacket(player, packetTeleportEntity(eid, l, onGround));
    }

    @Override
    public void updatePassengers(Player player, int eid, int... passengers) {
        serializer.clear();
        serializer.b(eid);
        serializer.a(passengers);

        try {
            PacketPlayOutMount packet = new PacketPlayOutMount();
            packet.a(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            DecentHologramsAPI.getInstance().getLogger().warning("Failed to send entity mount packet: " + e.getMessage());
        }
    }

    @Override
    public void removeEntity(Player player, int eid) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(eid);
        sendPacket(player, packet);
    }

}

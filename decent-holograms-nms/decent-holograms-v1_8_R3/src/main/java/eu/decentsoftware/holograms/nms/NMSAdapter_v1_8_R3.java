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

import eu.decentsoftware.holograms.nms.reflect.ReflectUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class NMSAdapter_v1_8_R3 implements NMSAdapter {

    private static final Map<String, Integer> ENTITY_TYPE_NAME_ID_MAP;
    private static final Field ENTITY_COUNT_FIELD;

    static {
        ENTITY_TYPE_NAME_ID_MAP = ReflectUtil.getFieldValue(EntityTypes.class, "g");
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

    @Contract("null -> null")
    private ItemStack i(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    @Contract("_ -> new")
    private @NotNull BlockPosition blockPos(@NotNull Location l) {
        return new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ());
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
        serializer.a(header);
        serializer.a(footer);

        try {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            packet.b(serializer);
            return packet;
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            DataWatcher.a((List<DataWatcher.WatchableObject>) objects, serializer);

            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
            packet.a(serializer);
            sendPacket(player, packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getMetaCustom(int id, Class<?> type, Object value) {
        return null;
    }

    @Override
    public Object getMetaEntityRemainingAir(int airTicksLeft) {
        return new DataWatcher.WatchableObject(2, 1, airTicksLeft);
    }

    @Override
    public Object getMetaEntityCustomName(String name) {
        return new DataWatcher.WatchableObject(4, 2, name);
    }

    @Override
    public Object getMetaEntityCustomName(Object name) {
        if (!(name instanceof IChatBaseComponent)) {
            return null;
        }
        return new DataWatcher.WatchableObject(4, 2, c((IChatBaseComponent) name));
    }

    @Override
    public Object getMetaEntityCustomNameVisible(boolean visible) {
        return new DataWatcher.WatchableObject(0, 3, (byte) (visible ? 1 : 0));
    }

    @Override
    public Object getMetaEntitySilenced(boolean silenced) {
        return new DataWatcher.WatchableObject(0, 4, (byte) (silenced ? 1 : 0));
    }

    @Override
    public Object getMetaEntityGravity(boolean gravity) {
        return new DataWatcher.WatchableObject(0, 5, (byte) (gravity ? 1 : 0));
    }

    @Override
    public Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra) {
        byte data = 0x00;
        data += onFire ? 0x01 : 0x00;
        data += crouched ? 0x02 : 0x00;
        data += sprinting ? 0x08 : 0x00;
        data += swimming ? 0x10 : 0x00;
        data += invisible ? 0x20 : 0x00;
        return new DataWatcher.WatchableObject(0, 0, data);
    }

    @Override
    public Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker) {
        byte data = 0x00;
        data += small ? 0x01 : 0x00;
        data += arms ? 0x02 : 0x00;
        data += noBasePlate ? 0x08 : 0x00;
        data += marker ? 0x10 : 0x00;
        return new DataWatcher.WatchableObject(0, 10, data);
    }

    @Override
    public Object getMetaItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return new DataWatcher.WatchableObject(5, 8, i(itemStack));
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
        return ENTITY_TYPE_NAME_ID_MAP == null ? type.getTypeId() : ENTITY_TYPE_NAME_ID_MAP.get(type.getName());
    }

    @Override
    public double getEntityHeight(EntityType type) {
        return 0;
    }

    @Override
    public void spawnEntity(Player player, int eid, UUID id, EntityType type, Location l) {
        serializer.clear();
        serializer.b(eid);
        serializer.writeByte(getEntityTypeId(type));
        serializer.writeInt(MathHelper.floor(l.getX() * 32));
        serializer.writeInt(MathHelper.floor(l.getY() * 32));
        serializer.writeInt(MathHelper.floor(l.getZ() * 32));
        serializer.writeByte(MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(l.getPitch() * 256.0F / 360.0F));
        serializer.writeInt(1);
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);

        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
        try {
            packet.a(serializer);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        sendPacket(player, packet);
    }

    @Override
    public void spawnEntityLiving(Player player, int eid, UUID id, EntityType type, Location l) {
        serializer.clear();
        serializer.b(eid);
        serializer.writeByte(getEntityTypeId(type));
        serializer.writeInt(MathHelper.floor(l.getX() * 32));
        serializer.writeInt(MathHelper.floor(l.getY() * 32));
        serializer.writeInt(MathHelper.floor(l.getZ() * 32));
        serializer.writeByte(MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(l.getPitch() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
        try {
            packet.a(serializer);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        sendPacket(player, packet);
    }

    @Override
    public void setEquipment(Player player, int eid, EntityEquipmentSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(eid, slot.getLegacySlotId(), i(itemStack));
        sendPacket(player, packet);
    }

    @Override
    public void teleportEntity(Player player, int eid, Location l, boolean onGround) {
        sendPacket(player, new PacketPlayOutEntityTeleport(
                eid,
                MathHelper.floor(l.getX() * 32.0),
                MathHelper.floor(l.getY() * 32.0),
                MathHelper.floor(l.getZ() * 32.0),
                (byte) ((int) (l.getYaw() * 256.0F / 360.0F)),
                (byte) ((int) (l.getPitch() * 256.0F / 360.0F)),
                onGround
        ));
    }

    @Override
    public void updatePassengers(Player player, int eid, int... passengers) {
        /*
         * This will only update the first passenger. This is because the packet only supports
         * one passenger. If you want to update more than one passenger, you will have to send
         * multiple packets.
         *
         * We don't care about multiple passengers in the context of DecentHolograms.
         */

        serializer.clear();
        serializer.writeInt(eid);
        serializer.writeInt(passengers != null && passengers.length >= 1 ? passengers[0] : -1);
        serializer.writeByte(0);

        try {
            PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity();
            packet.a(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeEntity(Player player, int eid) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(eid);
        sendPacket(player, packet);
    }

}

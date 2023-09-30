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

import eu.decentsoftware.holograms.api.hologram.component.ClickType;
import eu.decentsoftware.holograms.nms.event.PacketPlayInUseEntityEvent;
import eu.decentsoftware.holograms.nms.utils.ReflectField;
import eu.decentsoftware.holograms.nms.utils.ReflectUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class NMSAdapter_v1_8_R3 implements NMSAdapter {

    private static final Map<String, Integer> ENTITY_TYPE_NAME_ID_MAP;
    private static final ReflectField ENTITY_COUNT_FIELD;
    private static final ReflectField USE_PACKET_ENTITY_ID_FIELD;

    static {
        ENTITY_TYPE_NAME_ID_MAP = ReflectUtil.getFieldValue(EntityTypes.class, "g");
        ENTITY_COUNT_FIELD = new ReflectField(Entity.class, "entityCount");
        USE_PACKET_ENTITY_ID_FIELD = new ReflectField(PacketPlayInUseEntity.class, "a");
    }

    /**
     * Serializer for packet data.
     *
     * @implNote We use this one instance for all packets, clearing it after each use. This is
     * because we don't want to create a new instance every time we need to send a packet.
     */
    private final PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());

    public NMSAdapter_v1_8_R3() {
        System.out.println("Entity Type ID for ArmorStand: " + getEntityTypeId(EntityType.ARMOR_STAND));
    }

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
    public PacketPlayInUseEntityEvent extractEventFromPacketPlayInUseEntity(Player player, Object packet) {
        if (!(packet instanceof PacketPlayInUseEntity)) {
            return null;
        }
        PacketPlayInUseEntity useEntityPacket = (PacketPlayInUseEntity) packet;
        int entityId = (int) USE_PACKET_ENTITY_ID_FIELD.get(useEntityPacket);
        ClickType clickType;
        switch (useEntityPacket.a()) {
            case ATTACK:
                clickType = player.isSneaking() ? ClickType.SHIFT_LEFT : ClickType.LEFT;
                break;
            case INTERACT:
            case INTERACT_AT:
                clickType = player.isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT;
                break;
            default:
                return null;
        }
        return new PacketPlayInUseEntityEvent(player, entityId, clickType);
    }

    @Override
    public void sendUpdateTimePacket(Player player, long worldAge, long day) {
        sendPacket(player, new PacketPlayOutUpdateTime(worldAge, day, true));
    }

    @Override
    public void updateGameState(Player player, int mode, float value) {
        sendPacket(player, new PacketPlayOutGameStateChange(mode, value));
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        PacketPlayOutTitle titleTimesPacket = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.TIMES,
                null,
                fadeIn,
                stay,
                fadeOut
        );
        sendPacket(player, titleTimesPacket);

        if (title != null) {
            PacketPlayOutTitle titleMessagePacket = new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.TITLE,
                    s(title)
            );
            sendPacket(player, titleMessagePacket);
        }

        if (subtitle != null) {
            PacketPlayOutTitle subtitleMessagePacket = new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                    s(subtitle)
            );
            sendPacket(player, subtitleMessagePacket);
        }
    }

    @Override
    public void resetTitle(Player player) {
        PacketPlayOutTitle resetPacket = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.RESET,
                null
        );
        sendPacket(player, resetPacket);
    }

    @Override
    public void sendActionBar(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(s(message), (byte) 2);
        sendPacket(player, packet);
    }

    @Override
    public void sendHeaderFooter(Player player, String header, String footer) {
        serializer.clear();
        serializer.a(header);
        serializer.a(footer);

        try {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            packet.b(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEntityAnimation(Player player, int eid, int animation) {
        serializer.clear();
        serializer.b(eid);
        serializer.writeByte(animation);

        try {
            PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
            packet.a(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendBlockAction(Player player, Location location, int action, int param, int blockType) {
        PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(
                blockPos(location),
                Block.getByCombinedId(blockType).getBlock(),
                action,
                param
        );
        sendPacket(player, packet);
    }

    /*
     *  Entity Metadata
     */

    @SuppressWarnings("unchecked")
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
        return null; // Not supported
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
        return null; // Not supported
    }

    @Override
    public Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra) {
        byte data = 0x00;
        data += (byte) (onFire ? 0x01 : 0x00);
        data += (byte) (crouched ? 0x02 : 0x00);
        data += (byte) (sprinting ? 0x08 : 0x00);
        data += (byte) (swimming ? 0x10 : 0x00);
        data += (byte) (invisible ? 0x20 : 0x00);
        return new DataWatcher.WatchableObject(0, 0, data);
    }

    @Override
    public Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker) {
        byte data = 0x00;
        data += (byte) (small ? 0x01 : 0x00);
        data += (byte) (arms ? 0x02 : 0x00);
        data += (byte) (noBasePlate ? 0x08 : 0x00);
        data += (byte) (marker ? 0x10 : 0x00);
        return new DataWatcher.WatchableObject(0, 10, data);
    }

    @Override
    public Object getMetaItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return new DataWatcher.WatchableObject(5, 10, i(itemStack));
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
            int entityCount = (int) ENTITY_COUNT_FIELD.get(null);
            ENTITY_COUNT_FIELD.set(null, entityCount + 1);
            return entityCount;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get new entity ID", e);
        }
    }

    @Override
    public int getEntityTypeId(EntityType type) {
        // TODO: Find a better way to get the entity type id
//        if (type == EntityType.ARMOR_STAND) {
//            return 30;
//        }
//        if (type == EntityType.DROPPED_ITEM) {
//            return 2;
//        }
        // noinspection deprecation
        return ENTITY_TYPE_NAME_ID_MAP == null ? type.getTypeId() : ENTITY_TYPE_NAME_ID_MAP.get(type.getName());
    }

    @Override
    public double getEntityHeight(EntityType type) {
        return 0;
    }

    @Override
    public void spawnEntity(Player player, int eid, UUID id, EntityType type, Location location) {
        serializer.clear();
        serializer.b(eid);
        serializer.writeByte(getEntityTypeId(type));
        serializer.writeInt(MathHelper.floor(location.getX() * 32));
        serializer.writeInt(MathHelper.floor(location.getY() * 32));
        serializer.writeInt(MathHelper.floor(location.getZ() * 32));
        serializer.writeByte(MathHelper.d(location.getYaw() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(location.getPitch() * 256.0F / 360.0F));
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
    public void spawnEntityLiving(Player player, int eid, UUID id, EntityType type, Location location) {
        serializer.clear();
        serializer.b(eid);
        serializer.writeByte(getEntityTypeId(type));
        serializer.writeInt(MathHelper.floor(location.getX() * 32));
        serializer.writeInt(MathHelper.floor(location.getY() * 32));
        serializer.writeInt(MathHelper.floor(location.getZ() * 32));
        serializer.writeByte(MathHelper.d(location.getYaw() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(location.getPitch() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(location.getYaw() * 256.0F / 360.0F));
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeByte(127);

        try {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
            ReflectUtil.setFieldValue(packet, "l", new DataWatcher(null));
            packet.a(serializer);
            sendPacket(player, packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEquipment(Player player, int eid, EquipmentSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(
                eid,
                CraftEquipmentSlot.getSlotIndex(slot),
                i(itemStack)
        );
        sendPacket(player, packet);
    }

    @Override
    public void teleportEntity(Player player, int eid, Location location, boolean onGround) {
        sendPacket(player, new PacketPlayOutEntityTeleport(
                eid,
                MathHelper.floor(location.getX() * 32.0),
                MathHelper.floor(location.getY() * 32.0),
                MathHelper.floor(location.getZ() * 32.0),
                (byte) ((int) (location.getYaw() * 256.0F / 360.0F)),
                (byte) ((int) (location.getPitch() * 256.0F / 360.0F)),
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
        serializer.writeInt(passengers != null && passengers.length >= 1 ? passengers[0] : -1);
        serializer.writeInt(eid);
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

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

import com.mojang.datafixers.util.Pair;
import eu.decentsoftware.holograms.api.hologram.ClickType;
import eu.decentsoftware.holograms.nms.event.PacketPlayInUseEntityEvent;
import eu.decentsoftware.holograms.nms.utils.ReflectField;
import eu.decentsoftware.holograms.nms.utils.ReflectUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_20_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntityType;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"unused", "unchecked"})
public class NMSAdapter_v1_20_R2 implements NMSAdapter {

    private static final ReflectField ENTITY_COUNT_FIELD;
    private static final ReflectField USE_PACKET_ENTITY_ID_FIELD;
    private static final ReflectField USE_PACKET_ACTION_FIELD;
    private static final ReflectField PLAYER_CONNECTION_NETWORK_MANAGER_FIELD;
    private static final DataWatcherObject<Byte> ENTITY_META_FLAGS;
    private static final DataWatcherObject<Integer> ENTITY_META_AIR_TICKS;
    private static final DataWatcherObject<Optional<IChatBaseComponent>> ENTITY_META_CUSTOM_NAME;
    private static final DataWatcherObject<Boolean> ENTITY_META_CUSTOM_NAME_VISIBLE;
    private static final DataWatcherObject<Boolean> ENTITY_META_SILENCED;
    private static final DataWatcherObject<Boolean> ENTITY_META_GRAVITY;
    private static final DataWatcherObject<Byte> ARMOR_STAND_META_FLAGS;
    private static final DataWatcherObject<ItemStack> ITEM_ITEM_STACK_META;

    static {
        ENTITY_COUNT_FIELD = new ReflectField(Entity.class, "d");
        USE_PACKET_ENTITY_ID_FIELD = new ReflectField(PacketPlayInUseEntity.class, "a");
        USE_PACKET_ACTION_FIELD = new ReflectField(PacketPlayInUseEntity.class, "b");
        PLAYER_CONNECTION_NETWORK_MANAGER_FIELD = new ReflectField(PlayerConnection.class, "c");
        ENTITY_META_FLAGS = (DataWatcherObject<Byte>) new ReflectField(Entity.class, "ao").get(null);
        ENTITY_META_AIR_TICKS = (DataWatcherObject<Integer>) new ReflectField(Entity.class, "aT").get(null);
        ENTITY_META_CUSTOM_NAME = (DataWatcherObject<Optional<IChatBaseComponent>>) new ReflectField(Entity.class, "aU").get(null);
        ENTITY_META_CUSTOM_NAME_VISIBLE = (DataWatcherObject<Boolean>) new ReflectField(Entity.class, "aV").get(null);
        ENTITY_META_SILENCED = (DataWatcherObject<Boolean>) new ReflectField(Entity.class, "aW").get(null);
        ENTITY_META_GRAVITY = (DataWatcherObject<Boolean>) new ReflectField(Entity.class, "aX").get(null);
        ARMOR_STAND_META_FLAGS = (DataWatcherObject<Byte>) new ReflectField(EntityArmorStand.class, "bC").get(null);
        ITEM_ITEM_STACK_META = (DataWatcherObject<ItemStack>) new ReflectField(EntityItem.class, "c").get(null);
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
            ((CraftPlayer) player).getHandle().c.b((Packet<?>) packet);
        }
    }

    /*
     *  Player
     */

    @Override
    public ChannelPipeline getPipeline(@NotNull Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().c;
        NetworkManager networkManager = (NetworkManager) PLAYER_CONNECTION_NETWORK_MANAGER_FIELD.get(connection);
        Channel channel = networkManager.n;
        return channel.pipeline();
    }

    /*
     *  Packets
     */

    @Override
    public PacketPlayInUseEntityEvent extractEventFromPacketPlayInUseEntity(Player player, Object packet) {
        if (!(packet instanceof PacketPlayInUseEntity)) {
            return null;
        }

        PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
        int entityId = (int) USE_PACKET_ENTITY_ID_FIELD.get(packetPlayInUseEntity);
        Object action = USE_PACKET_ACTION_FIELD.get(packetPlayInUseEntity);
        Method method = ReflectUtil.getMethod(action.getClass(), "a");
        if (method == null) {
            return null;
        }
        method.setAccessible(true);

        try {
            Enum<?> bEnumValue = (Enum<?>) method.invoke(action);
            int ordinal = bEnumValue.ordinal();
            // 0 = INTERACT
            // 1 = ATTACK
            // 2 = INTERACT_AT
            //
            // https://wiki.vg/Protocol#Interact
            ClickType clickType;
            if (ordinal == 1) {
                clickType = player.isSneaking() ? ClickType.SHIFT_LEFT : ClickType.LEFT;
            } else {
                clickType = player.isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT;
            }
            return new PacketPlayInUseEntityEvent(player, entityId, clickType);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sendUpdateTimePacket(Player player, long worldAge, long day) {
        sendPacket(player, new PacketPlayOutUpdateTime(worldAge, day, true));
    }

    @Override
    public void updateGameState(Player player, int mode, float value) {
        PacketPlayOutGameStateChange.a stupidInstanceOfMode = null; // Gotta' love it
        switch (mode) {
            case 0:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.a;
                break;
            case 1:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.b;
                break;
            case 2:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.c;
                break;
            case 3:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.d;
                break;
            case 4:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.e;
                break;
            case 5:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.f;
                break;
            case 6:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.g;
                break;
            case 7:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.h;
                break;
            case 8:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.i;
                break;
            case 9:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.j;
                break;
            case 10:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.k;
                break;
            case 11:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.l;
                break;
            case 12:
                stupidInstanceOfMode = PacketPlayOutGameStateChange.m;
                break;
        }
        sendPacket(player, new PacketPlayOutGameStateChange(stupidInstanceOfMode, value));
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void resetTitle(Player player) {
        player.resetTitle();
    }

    @Override
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendHeaderFooter(Player player, String header, String footer) {
        sendPacket(player, new PacketPlayOutPlayerListHeaderFooter(s(header), s(footer)));
    }

    @Override
    public void sendEntityAnimation(Player player, int eid, int animation) {
        serializer.clear();
        serializer.writeInt(eid);
        serializer.writeByte(animation);

        try {
            PacketPlayOutAnimation packet = new PacketPlayOutAnimation(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendBlockAction(Player player, Location location, int action, int param, int blockType) {
        BlockPosition blockPosition = blockPos(location);
        Block block = ((CraftBlock) location.getBlock()).getNMS().b();
        PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(blockPosition, block, action, param);
        sendPacket(player, packet);
    }

    /*
     *  Entity Metadata
     */

    @Override
    public void sendEntityMetadata(Player player, int eid, List<?> objects) {
        List<DataWatcher.b<?>> bs = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof DataWatcher.Item) {
                DataWatcher.Item<?> item = (DataWatcher.Item<?>) object;
                bs.add(item.e());
            }
        }

        try {
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(eid, bs);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getMetaCustom(int id, Class<?> type, Object value) {
        return null;
    }

    @Override
    public Object getMetaEntityRemainingAir(int airTicksLeft) {
        return new DataWatcher.Item<>(ENTITY_META_AIR_TICKS, airTicksLeft);
    }

    @Override
    public Object getMetaEntityCustomName(String name) {
        return new DataWatcher.Item<>(ENTITY_META_CUSTOM_NAME, Optional.of(s(name)));
    }

    @Override
    public Object getMetaEntityCustomName(Object name) {
        return new DataWatcher.Item<>(ENTITY_META_CUSTOM_NAME, Optional.of((IChatBaseComponent) name));
    }

    @Override
    public Object getMetaEntityCustomNameVisible(boolean visible) {
        return new DataWatcher.Item<>(ENTITY_META_CUSTOM_NAME_VISIBLE, visible);
    }

    @Override
    public Object getMetaEntitySilenced(boolean silenced) {
        return new DataWatcher.Item<>(ENTITY_META_SILENCED, silenced);
    }

    @Override
    public Object getMetaEntityGravity(boolean gravity) {
        return new DataWatcher.Item<>(ENTITY_META_GRAVITY, gravity);
    }

    @Override
    public Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra) {
        byte data = 0x00;
        data += (byte) (onFire ? 0x01 : 0x00);
        data += (byte) (crouched ? 0x02 : 0x00);
        data += (byte) (sprinting ? 0x08 : 0x00);
        data += (byte) (swimming ? 0x10 : 0x00);
        data += (byte) (invisible ? 0x20 : 0x00);

        return new DataWatcher.Item<>(ENTITY_META_FLAGS, data);
    }

    @Override
    public Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker) {
        byte data = 0x00;
        data += (byte) (small ? 0x01 : 0x00);
        data += (byte) (arms ? 0x04 : 0x00);
        data += (byte) (noBasePlate ? 0x08 : 0x00);
        data += (byte) (marker ? 0x10 : 0x00);

        return new DataWatcher.Item<>(ARMOR_STAND_META_FLAGS, data);
    }

    @Override
    public Object getMetaItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return new DataWatcher.Item<>(ITEM_ITEM_STACK_META, i(itemStack));
    }

    /*
     *  Entity
     */

    @Override
    public int getFreeEntityId() {
        /*
         * We are getting the new entity ids the same way as the server does. This is to ensure
         * that the ids are unique and don't conflict with any other entities.
         */
        AtomicInteger counter = (AtomicInteger) ENTITY_COUNT_FIELD.get(null);
        if (counter != null) {
            return counter.incrementAndGet();
        }
        // Should never happen
        return -1;
    }

    @Override
    public int getEntityTypeId(EntityType type) {
        return BuiltInRegistries.h.a(CraftEntityType.bukkitToMinecraft(type));
    }

    @Override
    public double getEntityHeight(EntityType type) {
        return CraftEntityType.bukkitToMinecraft(type).n().b;
    }

    @Override
    public void spawnEntity(Player player, int eid, UUID id, EntityType type, Location location) {
        serializer.clear();
        serializer.c(eid);
        serializer.b(id.getMostSignificantBits());
        serializer.b(id.getLeastSignificantBits());
        serializer.c(getEntityTypeId(type));
        serializer.writeDouble(location.getX());
        serializer.writeDouble(location.getY());
        serializer.writeDouble(location.getZ());
        serializer.writeByte((byte) (location.getYaw() * 256.0F / 360.0F));
        serializer.writeByte((byte) (location.getPitch() * 256.0F / 360.0F));
        if (type == EntityType.DROPPED_ITEM) {
            serializer.writeInt(1);
        } else {
            serializer.writeInt(0);
        }
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);

        try {
            PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void spawnEntityLiving(Player player, int eid, UUID id, EntityType type, Location location) {
        spawnEntity(player, eid, id, type, location);
    }

    @Override
    public void setEquipment(Player player, int eid, EquipmentSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        List<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
        EnumItemSlot enumItemSlot = CraftEquipmentSlot.getNMS(slot);
        list.add(new Pair<>(enumItemSlot, i(itemStack)));

        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(eid, list);
        sendPacket(player, packet);
    }

    @Override
    public void teleportEntity(Player player, int eid, Location location, boolean onGround) {
        serializer.clear();
        serializer.c(eid);
        serializer.writeDouble(location.getX());
        serializer.writeDouble(location.getY());
        serializer.writeDouble(location.getZ());
        serializer.writeByte((byte) (location.getYaw() * 256.0F / 360.0F));
        serializer.writeByte((byte) (location.getPitch() * 256.0F / 360.0F));
        serializer.writeBoolean(onGround);

        try {
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(serializer);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePassengers(Player player, int eid, int... passengers) {
        serializer.clear();
        serializer.writeInt(eid);
        serializer.writeInt(passengers.length);
        for (int passenger : passengers) {
            serializer.writeInt(passenger);
        }

        try {
            PacketPlayOutMount packet = new PacketPlayOutMount(serializer);
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

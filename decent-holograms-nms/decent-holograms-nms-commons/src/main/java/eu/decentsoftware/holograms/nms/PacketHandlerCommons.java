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
import eu.decentsoftware.holograms.nms.reflect.ReflectConstructor;
import eu.decentsoftware.holograms.nms.reflect.ReflectField;
import eu.decentsoftware.holograms.nms.reflect.ReflectMethod;
import eu.decentsoftware.holograms.nms.utils.Version;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class responsible for handling packets.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
final class PacketHandlerCommons {

    private static final Class<?> ENTITY_USE_PACKET_CLASS;
    private static final ReflectField<Integer> ENTITY_USE_PACKET_ID_FIELD;
    private static final Class<?> PACKET_DATA_SERIALIZER_CLASS;
    private static final ReflectConstructor PACKET_DATA_SERIALIZER_CONSTRUCTOR;
    private static final ReflectMethod ENTITY_USE_PACKET_A_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_READ_INT_METHOD;

    static {
        if (Version.afterOrEqual(17)) {
            ENTITY_USE_PACKET_CLASS = ReflectUtil.getNMClass("network.protocol.game.PacketPlayInUseEntity");
            PACKET_DATA_SERIALIZER_CLASS = ReflectUtil.getNMClass("network.PacketDataSerializer");
        } else {
            ENTITY_USE_PACKET_CLASS = ReflectUtil.getNMSClass("PacketPlayInUseEntity");
            PACKET_DATA_SERIALIZER_CLASS = ReflectUtil.getNMSClass("PacketDataSerializer");
        }
        ENTITY_USE_PACKET_ID_FIELD = new ReflectField<>(ENTITY_USE_PACKET_CLASS, "a");
        PACKET_DATA_SERIALIZER_CONSTRUCTOR = new ReflectConstructor(PACKET_DATA_SERIALIZER_CLASS, ByteBuf.class);
        if (Version.afterOrEqual(19)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "k");
        } else if (Version.afterOrEqual(17)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "j");
        } else if (Version.afterOrEqual(14)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "i");
        } else if (Version.afterOrEqual(9)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "g");
        } else {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "e");
        }
        if (Version.afterOrEqual(17)) {
            ENTITY_USE_PACKET_A_METHOD = new ReflectMethod(ENTITY_USE_PACKET_CLASS, "a", PACKET_DATA_SERIALIZER_CLASS);
        } else {
            ENTITY_USE_PACKET_A_METHOD = new ReflectMethod(ENTITY_USE_PACKET_CLASS, "b", PACKET_DATA_SERIALIZER_CLASS);
        }
    }


    /**
     * Handle the PacketPlayInEntityUse packet and detect possible hologram clicks.
     *
     * @param packet The packet.
     * @param player The player that clicked.
     */
    public static boolean handlePacket(@NotNull Object packet, @NotNull Player player) {
        if (!packet.getClass().isAssignableFrom(ENTITY_USE_PACKET_CLASS)) {
            return false;
        }

        int entityId = ENTITY_USE_PACKET_ID_FIELD.getValue(packet);
        ClickType clickType = getClickType(packet, player);
        if (clickType == null) {
            return false;
        }

        // TODO: Fix this.
//        PacketListener listener = NMSManager.getInstance().getPacketListener();
//        if (listener != null) {
//            listener.handlePacketPlayInUseEntity(player, entityId, clickType);
//            return true;
//        }

        // Shouldn't happen.
        return false;
    }

    private static int getEntityUseActionOrdinal(Object packet) {
        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        ENTITY_USE_PACKET_A_METHOD.invoke(packet, packetDataSerializer);
        PACKET_DATA_SERIALIZER_READ_INT_METHOD.invoke(packetDataSerializer);
        return PACKET_DATA_SERIALIZER_READ_INT_METHOD.invoke(packetDataSerializer);
    }

    private static ClickType getClickType(Object packet, Player player) {
        int ordinal = getEntityUseActionOrdinal(packet);
        if (ordinal == 1) {
            return player.isSneaking() ? ClickType.SHIFT_LEFT : ClickType.LEFT;
        } else {
            return player.isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT;
        }
    }

}
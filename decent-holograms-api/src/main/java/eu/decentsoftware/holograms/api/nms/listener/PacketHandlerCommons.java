package eu.decentsoftware.holograms.api.nms.listener;

import eu.decentsoftware.holograms.api.utils.reflect.R;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class responsible for handling packets.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class PacketHandlerCommons {

    private static Class<?> ENTITY_USE_PACKET_CLASS;
    private static Field ENTITY_USE_PACKET_ID_FIELD;
    private static Constructor<?> PACKET_DATA_SERIALIZER_CONSTRUCTOR;
    private static Method ENTITY_USE_PACKET_A_METHOD;
    private static Method PACKET_DATA_SERIALIZER_READ_INT_METHOD;

    static {
        try {
            Class<?> PACKET_DATA_SERIALIZER_CLASS;
            if (Version.afterOrEqual(17)) {
                ENTITY_USE_PACKET_CLASS = R.getNMClass("network.protocol.game.PacketPlayInUseEntity");
                PACKET_DATA_SERIALIZER_CLASS = R.getNMClass("network.PacketDataSerializer");
            } else {
                ENTITY_USE_PACKET_CLASS = R.getNMSClass("PacketPlayInUseEntity");
                PACKET_DATA_SERIALIZER_CLASS = R.getNMSClass("PacketDataSerializer");
            }
            ENTITY_USE_PACKET_ID_FIELD = ENTITY_USE_PACKET_CLASS.getField("a");
            PACKET_DATA_SERIALIZER_CONSTRUCTOR = PACKET_DATA_SERIALIZER_CLASS.getConstructor(ByteBuf.class);
            if (Version.afterOrEqual(19)) {
                PACKET_DATA_SERIALIZER_READ_INT_METHOD = PACKET_DATA_SERIALIZER_CLASS.getMethod("a");
            } else if (Version.afterOrEqual(17)) {
                PACKET_DATA_SERIALIZER_READ_INT_METHOD = PACKET_DATA_SERIALIZER_CLASS.getMethod("j");
            } else if (Version.afterOrEqual(14)) {
                PACKET_DATA_SERIALIZER_READ_INT_METHOD = PACKET_DATA_SERIALIZER_CLASS.getMethod("i");
            } else if (Version.afterOrEqual(9)) {
                PACKET_DATA_SERIALIZER_READ_INT_METHOD = PACKET_DATA_SERIALIZER_CLASS.getMethod("g");
            } else {
                PACKET_DATA_SERIALIZER_READ_INT_METHOD = PACKET_DATA_SERIALIZER_CLASS.getMethod("e");
            }
            if (Version.afterOrEqual(17)) {
                ENTITY_USE_PACKET_A_METHOD = ENTITY_USE_PACKET_CLASS.getMethod("a", PACKET_DATA_SERIALIZER_CLASS);
            } else {
                ENTITY_USE_PACKET_A_METHOD = ENTITY_USE_PACKET_CLASS.getMethod("b", PACKET_DATA_SERIALIZER_CLASS);
            }
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the PacketPlayInEntityUse packet and detect possible hologram clicks.
     *
     * @param packet The packet.
     * @param player The player that clicked.
     */
    public static boolean handlePacket(Object packet, Player player) {
        if (packet == null || !packet.getClass().isAssignableFrom(ENTITY_USE_PACKET_CLASS)) {
            return false;
        }
        int entityId;
        try {
            entityId = (int) ENTITY_USE_PACKET_ID_FIELD.get(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        ClickType clickType = getClickType(packet, player);
        // TODO: Handle click.
        return true;
    }

    private static int getEntityUseActionOrdinal(Object packet) {
        try {
            Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
            ENTITY_USE_PACKET_A_METHOD.invoke(packet, packetDataSerializer);
            PACKET_DATA_SERIALIZER_READ_INT_METHOD.invoke(packetDataSerializer);
            return (int) PACKET_DATA_SERIALIZER_READ_INT_METHOD.invoke(packetDataSerializer);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
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
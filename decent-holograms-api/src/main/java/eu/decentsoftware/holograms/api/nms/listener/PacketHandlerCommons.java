package eu.decentsoftware.holograms.api.nms.listener;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.utils.reflect.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * Utility class responsible for handling packets.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class PacketHandlerCommons {

    private static final Class<?> ENTITY_USE_PACKET_CLASS;
    private static final ReflectField<Integer> ENTITY_USE_PACKET_ID_FIELD;
    private static final Class<?> PACKET_DATA_SERIALIZER_CLASS;
    private static final ReflectConstructor PACKET_DATA_SERIALIZER_CONSTRUCTOR;
    private static final ReflectMethod ENTITY_USE_PACKET_A_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_READ_INT_METHOD;

    static {
        if (Version.afterOrEqual(17)) {
            ENTITY_USE_PACKET_CLASS = R.getNMClass("network.protocol.game.PacketPlayInUseEntity");
            PACKET_DATA_SERIALIZER_CLASS = R.getNMClass("network.PacketDataSerializer");
        } else {
            ENTITY_USE_PACKET_CLASS = R.getNMSClass("PacketPlayInUseEntity");
            PACKET_DATA_SERIALIZER_CLASS = R.getNMSClass("PacketDataSerializer");
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
    public static boolean handlePacket(Object packet, Player player) {
        if (packet == null || !packet.getClass().isAssignableFrom(ENTITY_USE_PACKET_CLASS)) {
            return false;
        }

        // Get the players profile.
        Profile profile = DecentHologramsAPI.getInstance().getProfileRegistry().get(player.getName());
        if (profile == null) {
            return false;
        }

        // Get the clicked entity id.
        int entityId = ENTITY_USE_PACKET_ID_FIELD.getValue(packet);
        if (entityId != profile.getContext().getClickableEntityId()) {
            // The clicked entity id is not the one we need.
            return false;
        }

        // Get the clicked line.
        Line clickedLine = profile.getContext().getWatchedLine();
        if (clickedLine == null) {
            return false;
        }

        // Get the click type.
        ClickType clickType = getClickType(packet, player);
        if (clickType == null) {
            return false;
        }

        // Pass the click to the line.
        if (clickedLine.onClick(profile, clickType)) {
            // Pass the click to the line's page.
            return clickedLine.getParent().onClick(profile, clickType);
        }

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
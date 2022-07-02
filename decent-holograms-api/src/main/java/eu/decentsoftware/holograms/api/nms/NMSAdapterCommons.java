package eu.decentsoftware.holograms.api.nms;

import eu.decentsoftware.holograms.api.utils.reflect.R;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectField;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

@UtilityClass
public final class NMSAdapterCommons {

    private static final ReflectMethod CRAFT_PLAYER_GET_HANDLE_METHOD;
    private static ReflectField<?> ENTITY_PLAYER_CONNECTION_FIELD;
    private static ReflectField<?> PLAYER_CONNECTION_NETWORK_MANAGER_FIELD;
    private static ReflectField<?> NETWORK_MANAGER_CHANNEL_FIELD;

    static {
        Class<?> entityPlayerClass;
        Class<?> playerConnectionClass;
        Class<?> craftPlayerClass;
        Class<?> networkManagerClass;
        if (Version.afterOrEqual(17)) {
            entityPlayerClass = R.getNMClass("server.level.EntityPlayer");
            playerConnectionClass = R.getNMClass("server.network.PlayerConnection");
            craftPlayerClass = R.getObcClass("entity.CraftPlayer");
            networkManagerClass = R.getNMClass("network.NetworkManager");
            for (Field field : entityPlayerClass.getFields()) {
                if (field.getType().isAssignableFrom(playerConnectionClass) && ENTITY_PLAYER_CONNECTION_FIELD == null) {
                    ENTITY_PLAYER_CONNECTION_FIELD = new ReflectField<>(entityPlayerClass, field.getName());
                    break;
                }
            }
            for (Field field : networkManagerClass.getFields()) {
                if (field.getType().isAssignableFrom(Channel.class) && NETWORK_MANAGER_CHANNEL_FIELD == null) {
                    NETWORK_MANAGER_CHANNEL_FIELD = new ReflectField<>(networkManagerClass, field.getName());
                    break;
                }
            }
            for (Field field : playerConnectionClass.getFields()) {
                if (field.getType().isAssignableFrom(networkManagerClass) && PLAYER_CONNECTION_NETWORK_MANAGER_FIELD == null) {
                    PLAYER_CONNECTION_NETWORK_MANAGER_FIELD = new ReflectField<>(playerConnectionClass, field.getName());
                    break;
                }
            }
        } else {
            entityPlayerClass = R.getNMSClass("EntityPlayer");
            playerConnectionClass = R.getNMSClass("PlayerConnection");
            craftPlayerClass = R.getObcClass("entity.CraftPlayer");
            networkManagerClass = R.getNMSClass("NetworkManager");
            ENTITY_PLAYER_CONNECTION_FIELD = new ReflectField<>(entityPlayerClass, "playerConnection");
            PLAYER_CONNECTION_NETWORK_MANAGER_FIELD = new ReflectField<>(playerConnectionClass, "networkManager");
            NETWORK_MANAGER_CHANNEL_FIELD = new ReflectField<>(networkManagerClass, "channel");
        }
        CRAFT_PLAYER_GET_HANDLE_METHOD = new ReflectMethod(craftPlayerClass, "getHandle");
    }

    public static Object getPlayerConnection(@NotNull Player player) {
        Object entityPlayer = CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player);
        return ENTITY_PLAYER_CONNECTION_FIELD.getValue(entityPlayer);
    }

    public static ChannelPipeline getPipeline(@NotNull Player player) {
        Object playerConnection = getPlayerConnection(player);
        Object networkManager = PLAYER_CONNECTION_NETWORK_MANAGER_FIELD.getValue(playerConnection);
        Channel channel = (Channel) NETWORK_MANAGER_CHANNEL_FIELD.getValue(networkManager);
        return channel.pipeline();
    }

}

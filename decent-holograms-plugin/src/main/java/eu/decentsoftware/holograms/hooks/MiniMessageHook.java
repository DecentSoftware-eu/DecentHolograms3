package eu.decentsoftware.holograms.hooks;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

/**
 * This class provides methods for using MiniMessage.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class MiniMessageHook {

    /**
     * Serialize the given string to a IChatBaseComponent using MiniMessage.
     *
     * @param string The string to serialize.
     * @return The serialized IChatBaseComponent.
     */
    @NotNull
    public static Object serializeMinecraft(@NotNull String string) {
        Component component = MiniMessage.miniMessage().deserialize(string);
        return MinecraftComponentSerializer.get().serialize(component);
    }

}
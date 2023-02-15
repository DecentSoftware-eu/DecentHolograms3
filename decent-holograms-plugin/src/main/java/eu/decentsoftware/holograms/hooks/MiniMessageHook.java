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

package eu.decentsoftware.holograms.hooks;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * This class provides methods for using MiniMessage.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class MiniMessageHook {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .character('§')
            .hexCharacter('#')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private static final TagResolver[] HOLOGRAM_RESOLVERS = {
            StandardTags.color(),
            StandardTags.decorations(),
            StandardTags.reset(),
            StandardTags.gradient(),
            StandardTags.rainbow()
    };

    /**
     * Replace all legacy components in the given string with MiniMessage components
     * and serialize them to IChatBaseComponent.
     *
     * @param string The string to serialize.
     * @return The serialized IChatBaseComponent.
     */
    @NotNull
    public static Object serializeMinecraft(@NotNull String string) {
        String serialized = MiniMessage.miniMessage().serialize(SERIALIZER.deserialize(string));
        serialized = serialized.replace("\\<", "<");
        TextComponent component = (TextComponent) MiniMessage.miniMessage().deserialize(serialized, HOLOGRAM_RESOLVERS);
        return MinecraftComponentSerializer.get().serialize(component);
    }

    /**
     * Replace all MiniMessage components in the given string with legacy components.
     *
     * @param string The string to serialize.
     * @return The serialized string.
     */
    @NotNull
    public static String serializeMinecraftLegacy(@NotNull String string) {
        String serialized = MiniMessage.miniMessage().serialize(SERIALIZER.deserialize(string));
        serialized = serialized.replace("\\<", "<");
        TextComponent component = (TextComponent) MiniMessage.miniMessage().deserialize(serialized, HOLOGRAM_RESOLVERS);
        return SERIALIZER.serialize(component);
    }

}
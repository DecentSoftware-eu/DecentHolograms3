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

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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
            StandardTags.rainbow(),
            StandardTags.selector()
    };

    @NonNull
    public static Object serializeToIChatBaseComponent(@NonNull String string, boolean legacy) {
        TextComponent component = serializeToComponent(string, legacy);
        return MinecraftComponentSerializer.get().serialize(component);
    }

    @NonNull
    public static String serializeToString(@NonNull String string, boolean legacy) {
        TextComponent component = serializeToComponent(string, legacy);
        return SERIALIZER.serialize(component);
    }

    private static TextComponent serializeToComponent(@NonNull String string, boolean legacy) {
        String serialized = MiniMessage.miniMessage().serialize(SERIALIZER.deserialize(string));
        serialized = serialized.replace("\\<", "<");
        TextComponent component = (TextComponent) MiniMessage.miniMessage().deserialize(serialized, HOLOGRAM_RESOLVERS);
        if (legacy) {
            component = mapRGBColorsToLegacy(component);
        }
        return component;
    }

    @NonNull
    public static TextComponent mapRGBColorsToLegacy(@NonNull TextComponent component) {
        TextComponent.Builder builder = component.toBuilder().mapChildren((child) -> {
            TextColor color = child.color();
            if (color == null) {
                return child;
            }
            NamedTextColor namedColor = NamedTextColor.nearestTo(color);
            return (BuildableComponent<?, ?>) child.color(namedColor);
        });
        return builder.build();
    }

}
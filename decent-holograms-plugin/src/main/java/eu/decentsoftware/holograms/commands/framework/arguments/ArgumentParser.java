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

package eu.decentsoftware.holograms.commands.framework.arguments;

import com.google.common.collect.ImmutableMap;
import eu.decentsoftware.holograms.commands.framework.arguments.parsers.HologramParser;
import eu.decentsoftware.holograms.commands.framework.arguments.parsers.PositionParser;
import eu.decentsoftware.holograms.commands.framework.arguments.parsers.WorldParser;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.internal.PluginHologram;
import eu.decentsoftware.holograms.utils.location.Position3D;
import lombok.NonNull;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * An argument parser is used to parse arguments from a {@link Arguments} instance
 * into a specific type.
 *
 * @param <T> The type of the argument to parse.
 * @author d0by
 * @see Arguments
 * @since 3.0.0
 */
public interface ArgumentParser<T> {

    Map<Class<?>, ArgumentParser<?>> PARSERS = ImmutableMap.of(
            DefaultHologram.class, new HologramParser(),
            Position3D.class, new PositionParser(),
            World.class, new WorldParser()
    );

    /**
     * Parses the next argument from the given {@link Arguments}.
     *
     * @param args     The arguments to parse from.
     * @param sender   The sender to parse for. May be {@code null}.
     * @param hologram The hologram to parse for. May be {@code null}.
     * @return The parsed argument or {@link Optional#empty()} if the argument could not be parsed.
     */
    Optional<T> parse(final @NonNull Arguments args, final @Nullable CommandSender sender, final @Nullable PluginHologram hologram);

    /**
     * Parses the next argument from the given {@link Arguments}.
     *
     * @param args     The arguments to parse from.
     * @param hologram The hologram to parse for. May be {@code null}.
     * @return The parsed argument or {@link Optional#empty()} if the argument could not be parsed.
     */
    default Optional<T> parse(final @NonNull Arguments args, final @Nullable PluginHologram hologram) {
        return this.parse(args, null, hologram);
    }

    /**
     * Parses the next argument from the given {@link Arguments}.
     *
     * @param args   The arguments to parse from.
     * @param sender The sender to parse for. May be {@code null}.
     * @return The parsed argument or {@link Optional#empty()} if the argument could not be parsed.
     */
    default Optional<T> parse(final @NonNull Arguments args, final @Nullable CommandSender sender) {
        return this.parse(args, sender, null);
    }

    /**
     * Parses the next argument from the given {@link Arguments}.
     *
     * @param args The arguments to parse from.
     * @return The parsed argument or {@link Optional#empty()} if the argument could not be parsed.
     */
    default Optional<T> parse(final @NonNull Arguments args) {
        return this.parse(args, null, null);
    }

}

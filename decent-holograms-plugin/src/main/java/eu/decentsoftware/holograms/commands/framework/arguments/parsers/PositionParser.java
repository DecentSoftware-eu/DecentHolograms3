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

package eu.decentsoftware.holograms.commands.framework.arguments.parsers;

import eu.decentsoftware.holograms.commands.framework.arguments.ArgumentParser;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.internal.PluginHologram;
import eu.decentsoftware.holograms.utils.location.Position3D;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PositionParser implements ArgumentParser<Position3D> {

    @Override
    public Optional<Position3D> parse(final @NonNull Arguments args, final @Nullable CommandSender sender, final @Nullable PluginHologram hologram) {
        final Optional<Double> x = args.peek().map(string -> parseRelative(string, sender, hologram));
        final Optional<Double> y = args.peek().map(string -> parseRelative(string, sender, hologram));
        final Optional<Double> z = args.peek().map(string -> parseRelative(string, sender, hologram));

        if (x.isPresent() && y.isPresent() && z.isPresent()) {
            args.nextString();
            args.nextString();
            args.nextString();

            return Optional.of(new Position3D(x.get(), y.get(), z.get()));
        }

        return Optional.empty();
    }

    @Nullable
    private Double parseRelative(final @NonNull String string, final @Nullable CommandSender sender, final @Nullable PluginHologram hologram) {
        if (string.startsWith("~")) {
            final String substring = string.substring(1);
            final double relativeX;
            if (hologram != null) {
                relativeX = hologram.getPositionManager().getLocation().getX();
            } else if (sender instanceof Player) {
                relativeX = ((Player) sender).getLocation().getX();
            } else {
                relativeX = 0.0d;
            }

            try {
                return substring.isEmpty() ? relativeX : Double.parseDouble(substring) + relativeX;
            } catch (final NumberFormatException e) {
                return null;
            }
        } else {
            try {
                return Double.parseDouble(string);
            } catch (final NumberFormatException e) {
                return null;
            }
        }
    }

}

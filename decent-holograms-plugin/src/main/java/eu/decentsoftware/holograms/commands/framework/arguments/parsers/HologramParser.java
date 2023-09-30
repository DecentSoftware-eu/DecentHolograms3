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

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.commands.framework.arguments.ArgumentParser;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.DefaultHologramRegistry;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HologramParser implements ArgumentParser<DefaultHologram> {

    @Override
    public Optional<DefaultHologram> parse(final @NonNull Arguments args, final @Nullable CommandSender sender, final @Nullable DefaultHologram hologram) {
        final DefaultHologramRegistry registry = DecentHolograms.getInstance().getHologramRegistry();
        return args.nextString().map(registry::getHologram);
    }

}

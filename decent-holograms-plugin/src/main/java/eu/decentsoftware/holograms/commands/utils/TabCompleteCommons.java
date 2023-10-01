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

package eu.decentsoftware.holograms.commands.utils;

import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.editor.move.MoveLocationBinder;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.DefaultHologramRegistry;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class TabCompleteCommons {

    public static List<String> getMatchingNotMovingEditableHologramNames(DefaultHologramRegistry hologramRegistry, Arguments args) {
        return hologramRegistry.getHolograms().stream()
                .filter(hologram -> hologram.getSettings().isEditable())
                .filter(hologram -> !(hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder))
                .map(DefaultHologram::getName)
                .filter(name -> name.toLowerCase().startsWith(args.peek().orElse("").toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<String> getMatchingEditableHologramNames(DefaultHologramRegistry hologramRegistry, Arguments args) {
        return hologramRegistry.getHolograms().stream()
                .filter(hologram -> hologram.getSettings().isEditable())
                .map(DefaultHologram::getName)
                .filter(name -> name.toLowerCase().startsWith(args.peek().orElse("").toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<String> getMatchingHologramNames(DefaultHologramRegistry hologramRegistry, Arguments args) {
        return hologramRegistry.getHolograms().stream()
                .map(DefaultHologram::getName)
                .filter(name -> name.toLowerCase().startsWith(args.peek().orElse("").toLowerCase()))
                .collect(Collectors.toList());
    }

}

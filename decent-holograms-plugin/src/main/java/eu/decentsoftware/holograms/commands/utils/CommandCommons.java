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

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.DefaultHologramRegistry;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Common methods and constants for commands.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class CommandCommons {

    /**
     * Returns the first hologram in the player's view.
     *
     * @param player Player to check.
     * @return The first hologram in the player's view.
     */
    @Nullable
    public static DefaultHologram getHologramInView(DefaultHologramRegistry hologramRegistry, @NonNull Player player) {
        Location location = player.getEyeLocation();
        Vector lookDirection = location.getDirection();

        for (int i = 3; i < 24; i += 3) {
            location.add(lookDirection);

            List<DefaultHologram> holograms = getHologramsNearLocation(hologramRegistry, location);

            if (!holograms.isEmpty()) {
                return holograms.get(0);
            }
        }

        return null;
    }

    /**
     * Attempts to find a hologram by its name or, if the sender is a player, in the player's view.
     * <p>
     * If the hologram is not found, the sender will be notified.
     * <p>
     * If the hologram is found but not editable, the sender will be notified.
     *
     * @param sender The sender.
     * @param name   Name of the hologram.
     * @return The hologram, if found.
     */
    public static Optional<DefaultHologram> getEditableHologramInViewOrByName(DefaultHologramRegistry hologramRegistry, @NotNull CommandSender sender, @Nullable String name) {
        DefaultHologram hologram = null;
        if (sender instanceof Player && name == null) {
            hologram = getHologramInView(hologramRegistry, (Player) sender);
        } else if (name != null) {
            hologram = hologramRegistry.getHologram(name);
        }

        if (hologram == null) {
            Lang.confTell(sender, "editor.error.invalid_hologram_name_or_view");
            return Optional.empty();
        }

        if (!hologram.getSettings().isEditable()) {
            Lang.confTell(sender, "editor.error.not_editable", hologram.getName());
            return Optional.empty();
        }

        return Optional.of(hologram);
    }

    /**
     * Returns all holograms within 4 blocks of the location.
     *
     * @param location Location to check.
     * @return All holograms within 4 blocks of the location.
     */
    @NotNull
    public static List<DefaultHologram> getHologramsNearLocation(DefaultHologramRegistry hologramRegistry, @NonNull Location location) {
        List<DefaultHologram> holograms = new ArrayList<>();
        for (DefaultHologram hologram : hologramRegistry.getHolograms()) {
            if (hologram.getPositionManager().getActualLocation().distanceSquared(location) <= 4 * 4) {
                holograms.add(hologram);
            }
        }
        return holograms;
    }

}

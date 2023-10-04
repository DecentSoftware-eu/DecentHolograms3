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
import eu.decentsoftware.holograms.internal.PluginHologram;
import eu.decentsoftware.holograms.internal.PluginHologramManager;
import eu.decentsoftware.holograms.utils.ComponentMessage;
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
    public static PluginHologram getHologramInView(@NonNull PluginHologramManager hologramManager, @NonNull Player player) {
        Location location = player.getEyeLocation();
        Vector lookDirection = location.getDirection();

        for (int i = 3; i < 24; i += 3) {
            location.add(lookDirection);

            List<PluginHologram> holograms = getHologramsNearLocation(hologramManager, location);

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
     * @param hologramManager Hologram manager.
     * @param sender          The sender.
     * @param name            Name of the hologram.
     * @return The hologram, if found.
     */
    public static Optional<PluginHologram> getHologramInViewOrByName(@NonNull PluginHologramManager hologramManager, @NotNull CommandSender sender, @Nullable String name) {
        PluginHologram hologram = null;
        if (sender instanceof Player && name == null) {
            hologram = getHologramInView(hologramManager, (Player) sender);
        } else if (name != null) {
            hologram = hologramManager.getHologram(name);
        }

        if (hologram == null) {
            Lang.confTell(sender, "editor.error.invalid_hologram_name_or_view");
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
    public static List<PluginHologram> getHologramsNearLocation(@NonNull PluginHologramManager hologramManager, @NonNull Location location) {
        List<PluginHologram> holograms = new ArrayList<>();
        for (PluginHologram hologram : hologramManager.getHolograms()) {
            if (hologram.getPositionManager().getActualBukkitLocation().distanceSquared(location) <= 4 * 4) {
                holograms.add(hologram);
            }
        }
        return holograms;
    }

    /**
     * Sends pagination buttons to the player.
     *
     * @param player     Player to send to.
     * @param page       Current page number.
     * @param totalPages Total number of pages.
     * @param command    Command to execute when the button is clicked. Will be formatted
     *                   with the page number so make sure to include a %d in the command.
     */
    public static void sendChatPaginationButtons(
            @NonNull Player player,
            int page,
            int totalPages,
            @NonNull String command
    ) {
        ComponentMessage message = new ComponentMessage(" ");
        message.append(Lang.formatString("&3««««« Prev Page"));
        if (page > 1) {
            message.clickCommand(String.format(command, page - 1));
            message.hoverText(Lang.formatString("&aClick to view the previous page"));
        }
        message.append(Lang.formatString("&3 | "));
        message.reset();
        message.append(Lang.formatString("&3Next Page »»»»»"));
        if (page < totalPages) {
            message.clickCommand(String.format(command, page + 1));
            message.hoverText(Lang.formatString("&aClick to view the next page"));
        }
        message.send(player);
    }

}

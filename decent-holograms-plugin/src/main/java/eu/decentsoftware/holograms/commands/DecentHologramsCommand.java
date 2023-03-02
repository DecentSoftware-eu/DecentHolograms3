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

package eu.decentsoftware.holograms.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.processing.CommandContainer;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.editor.move.MoveController;
import eu.decentsoftware.holograms.editor.move.MoveLocationBinder;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.DefaultHologramRegistry;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@CommandContainer
public class DecentHologramsCommand {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    @CommandMethod("dh reload")
    @CommandDescription("Reload the plugin")
    @CommandPermission(Config.ADMIN_PERM)
    public void reload(@NonNull CommandSender sender) {
        SchedulerUtil.async(() -> {
            long start = System.currentTimeMillis();
            PLUGIN.reload();
            long end = System.currentTimeMillis();
            Lang.confTell(sender, "plugin.reloaded", end - start);
        });
    }

    @CommandMethod(value = "dh move [name]", requiredSender = Player.class)
    @CommandDescription("Move a hologram")
    public void move(
            @NonNull Player player,
            @Argument("name") String name
    ) {
        MoveController moveController = PLUGIN.getEditor().getMoveController();

        // Cancel move if already moving
        if (moveController.cancel(player)) {
            Lang.confTell(player, "editor.move.cancel");
            return;
        }

        DefaultHologram hologram = name != null ? PLUGIN.getHologramRegistry().getHologram(name) : getHologramInView(player);
        if (hologram == null) {
            Lang.confTell(player, "editor.move.error.no_hologram");
            return;
        }

        if (hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder) {
            Lang.confTell(player, "editor.move.error.already_being_moved");
            return;
        }

        if (moveController.initiate(player, hologram)) {
            Lang.confTell(player, "editor.move.initiate", hologram.getName());
        }
    }

    @Nullable
    private DefaultHologram getHologramInView(@NonNull Player player) {
        // Ray trace
        Location location = player.getEyeLocation();
        Vector lookDirection = location.getDirection();

        for (int i = 0; i < 10; i++) {
            location.add(lookDirection);

            List<DefaultHologram> holograms = getHologramsNearLocation(location, 3);

            if (!holograms.isEmpty()) {
                return holograms.get(0);
            }
        }

        return null;
    }

    @NotNull
    private List<DefaultHologram> getHologramsNearLocation(@NonNull Location location, double maxDistance) {
        DefaultHologramRegistry registry = PLUGIN.getHologramRegistry();

        List<DefaultHologram> holograms = new ArrayList<>();

        for (DefaultHologram hologram : registry.getHolograms()) {
            if (hologram.getPositionManager().getActualLocation().distanceSquared(location) <= maxDistance * maxDistance) {
                holograms.add(hologram);
            }
        }

        return holograms;
    }

}

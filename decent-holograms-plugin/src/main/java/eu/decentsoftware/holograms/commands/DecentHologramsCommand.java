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
import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.DefaultHologramRegistry;
import eu.decentsoftware.holograms.hologram.HologramContext;
import eu.decentsoftware.holograms.profile.Profile;
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
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        DefaultHologram hologram = name != null ? PLUGIN.getHologramRegistry().getHologram(name) : getHologramInView(player);
        if (hologram == null) {
            Lang.confTell(player, "editor.move.error.no_hologram");
            return;
        }

        HologramContext context = hologram.getContext();
        if (context.getMover() != null) {
            Lang.confTell(player, "editor.move.error.already_moving");
            return;
        }

        profile.getContext().setMovingHologram(hologram);
        context.setMover(player.getUniqueId());
        context.setMoverDistance(5);

        hologram.getPositionManager().bindLocation(() -> {
            final Location location = player.getEyeLocation();
            final Vector lookDirection = location.getDirection();

            location.add(lookDirection.multiply(hologram.getContext().getMoverDistance()));

            final HologramPage page = hologram.getPage(hologram.getVisibilityManager().getPage(player));
            if (page == null) {
                return location;
            }

            final double height = page.getLines().stream()
                    .mapToDouble((line) -> line.getSettings().getHeight())
                    .sum();

            location.add(0, height / 2, 0);

            // Snap to block center if sneaking
            if (player.isSneaking()) {
                location.setX(location.getBlockX() + 0.5);
                location.setY(location.getBlockY());
                location.setZ(location.getBlockZ() + 0.5);
            }

            System.out.println("World: " + location.getWorld().getName() + " X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ());
            System.out.println(hologram.getVisibilityManager().isViewing(player));

            return location;
        });

        Lang.confTell(player, "editor.move.initiate", hologram.getName());
    }

    @Nullable
    private DefaultHologram getHologramInView(final @NonNull Player player) {
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
    private List<DefaultHologram> getHologramsNearLocation(Location location, double maxDistance) {
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

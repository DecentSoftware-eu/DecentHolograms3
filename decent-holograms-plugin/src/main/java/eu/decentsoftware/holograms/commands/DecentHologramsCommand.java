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
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@CommandContainer
public class DecentHologramsCommand {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private static final String ROOT_ALIASES = "dh|decentholograms|holograms|holo";

    // ==================== MAIN COMMAND ==================== //

    @CommandMethod(ROOT_ALIASES)
    @CommandDescription("The main command of the plugin.")
    public void dh(@NonNull CommandSender sender) {
        if (sender.hasPermission(Config.ADMIN_PERM)) {
            Lang.confTell(sender, "plugin.help");
        } else {
            Lang.sendVersionMessage(sender);
        }
    }

    // ==================== RELOAD COMMAND ==================== //

    @CommandMethod(ROOT_ALIASES + " reload")
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

    // ==================== VERSION COMMAND ==================== //

    @CommandMethod(ROOT_ALIASES + " version|ver")
    @CommandDescription("Show the plugin version")
    @CommandPermission(Config.ADMIN_PERM)
    public void version(@NonNull CommandSender sender) {
        Lang.sendVersionMessage(sender);
    }

    // ==================== DELETE COMMAND ==================== //

    @CommandMethod(value = ROOT_ALIASES + " delete|del [name]")
    @CommandDescription("Delete a hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void delete(
            @NonNull CommandSender sender,
            @Argument(value = "name", suggestions = "holograms") String name
    ) {
        DefaultHologramRegistry registry = PLUGIN.getHologramRegistry();
        DefaultHologram hologram = null;
        if (name == null && sender instanceof Player) {
            hologram = getHologramInView((Player) sender);
        } else if (name != null) {
            hologram = registry.getHologram(name);
        }

        if (hologram == null) {
            Lang.confTell(sender, "editor.invalid_hologram_name_or_view");
            return;
        }

        if (!hologram.getSettings().isEditable()) {
            Lang.confTell(sender, "editor.not_editable", hologram.getName());
            return;
        }

        if (hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder) {
            MoveLocationBinder binder = (MoveLocationBinder) hologram.getPositionManager().getLocationBinder();
            PLUGIN.getEditor().getMoveController().cancel(binder.getPlayer());
            Lang.confTell(binder.getPlayer(), "editor.move.cancel");
        }

        registry.removeHologram(hologram.getName());
        hologram.delete();

        Lang.confTell(sender, "editor.delete.success", hologram.getName());
    }

    // ==================== MOVE COMMAND ==================== //

    @CommandMethod(value = ROOT_ALIASES + " move|mv [name]", requiredSender = Player.class)
    @CommandDescription("Move a hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void move(
            @NonNull Player player,
            @Argument(value = "name", suggestions = "not_moving_holograms") String name
    ) {
        MoveController moveController = PLUGIN.getEditor().getMoveController();

        // Cancel move if already moving
        if (moveController.cancel(player)) {
            Lang.confTell(player, "editor.move.cancel");
            return;
        }

        DefaultHologram hologram = name != null ? PLUGIN.getHologramRegistry().getHologram(name) : getHologramInView(player);
        if (hologram == null) {
            Lang.confTell(player, "editor.invalid_hologram_name_or_view");
            return;
        }

        if (!hologram.getSettings().isEditable()) {
            Lang.confTell(sender, "editor.not_editable", hologram.getName());
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

    @CommandMethod(value = ROOT_ALIASES + " movehere|mvhr <name>", requiredSender = Player.class)
    @CommandDescription("Move a hologram to yourself")
    @CommandPermission(Config.ADMIN_PERM)
    public void movehere(
            @NonNull Player player,
            @Argument(value = "name", suggestions = "not_moving_holograms") String name
    ) {
        DefaultHologram hologram = PLUGIN.getHologramRegistry().getHologram(name);
        if (hologram == null) {
            Lang.confTell(player, "editor.invalid_hologram_name", name);
            return;
        }

        if (!hologram.getSettings().isEditable()) {
            Lang.confTell(player, "editor.not_editable", hologram.getName());
            return;
        }

        if (hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder) {
            Lang.confTell(player, "editor.move.already_being_moved");
            return;
        }

        hologram.getPositionManager().setLocation(player.getLocation());
        hologram.recalculate();
        hologram.getConfig().save();
        Lang.confTell(player, "editor.move.success", hologram.getName());
    }

    // ==================== TELEPORT COMMAND ==================== //

    @CommandMethod(value = ROOT_ALIASES + " teleport|goto|tp <name>", requiredSender = Player.class)
    @CommandDescription("Teleport to a hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void teleport(
            @NonNull Player player,
            @Argument(value = "name", suggestions = "not_moving_holograms") String name
    ) {
        DefaultHologram hologram = PLUGIN.getHologramRegistry().getHologram(name);
        if (hologram == null) {
            Lang.confTell(player, "editor.invalid_hologram_name", name);
            return;
        }

        if (!hologram.getSettings().isEditable()) {
            Lang.confTell(player, "editor.not_editable", hologram.getName());
            return;
        }

        Location location = hologram.getPositionManager().getLocation();
        player.teleport(location);

        Lang.confTell(player, "editor.teleported", hologram.getName());
    }

    // ==================== SUGGESTIONS ==================== //

    @Suggestions("holograms")
    public List<String> suggestEditableHologramNames(CommandContext<Player> context, String input) {
        return PLUGIN.getHologramRegistry().getHolograms().stream()
                .filter(hologram -> hologram.getSettings().isEditable())
                .map(DefaultHologram::getName)
                .filter(name -> input == null || input.isEmpty() || Common.startsWithIgnoreCase(name, input))
                .collect(Collectors.toList());
    }

    @Suggestions("not_moving_holograms")
    public List<String> suggestMovableHologramNames(CommandContext<Player> context, String input) {
        return PLUGIN.getHologramRegistry().getHolograms().stream()
                .filter(hologram -> !(hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder))
                .filter(hologram -> hologram.getSettings().isEditable())
                .map(DefaultHologram::getName)
                .filter(name -> input == null || input.isEmpty() || Common.startsWithIgnoreCase(name, input))
                .collect(Collectors.toList());
    }

    // ==================== UTILS ==================== //

    @Nullable
    private DefaultHologram getHologramInView(@NonNull Player player) {
        // Ray trace
        Location location = player.getEyeLocation();
        Vector lookDirection = location.getDirection();

        for (int i = 0; i < 10; i += 2) {
            location.add(lookDirection);

            List<DefaultHologram> holograms = getHologramsNearLocation(location);

            if (!holograms.isEmpty()) {
                return holograms.get(0);
            }
        }

        return null;
    }

    @NotNull
    private List<DefaultHologram> getHologramsNearLocation(@NonNull Location location) {
        DefaultHologramRegistry registry = PLUGIN.getHologramRegistry();

        List<DefaultHologram> holograms = new ArrayList<>();

        for (DefaultHologram hologram : registry.getHolograms()) {
            if (hologram.getPositionManager().getActualLocation().distanceSquared(location) <= 4 * 4) {
                holograms.add(hologram);
            }
        }

        return holograms;
    }

}

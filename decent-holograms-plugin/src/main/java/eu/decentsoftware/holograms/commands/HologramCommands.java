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

import cloud.commandframework.annotations.*;
import cloud.commandframework.annotations.processing.CommandContainer;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.api.hologram.component.PositionManager;
import eu.decentsoftware.holograms.editor.move.MoveLocationBinder;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.DefaultHologramRegistry;
import eu.decentsoftware.holograms.hologram.page.DefaultHologramPage;
import eu.decentsoftware.holograms.utils.Common;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains all commands related to holograms.
 *
 * @author d0by
 * @see DecentHologramsCommand
 * @since 3.0.0
 */
@SuppressWarnings("unused")
@CommandContainer
public class HologramCommands {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    // ==================== CREATE COMMAND ==================== //

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " create|c|new <name>", requiredSender = Player.class)
    @CommandDescription("Create a new hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void create(
            @NonNull Player player,
            @Argument("name") @NonNull String name,
            @Flag(value = "move", aliases = {"m"}) boolean move,
            @Flag(value = "empty", aliases = {"e"}) boolean empty,
            @Flag(value = "location", aliases = {"l"}) @Nullable Location location,
            @Flag(value = "world", aliases = {"w"}) @Nullable World world
    ) {
        if (!name.matches(Config.NAME_REGEX)) {
            Lang.confTell(player, "editor.create.invalid_name", name);
            return;
        }

        DefaultHologramRegistry registry = PLUGIN.getHologramRegistry();
        if (registry.getHologram(name) != null) {
            Lang.confTell(player, "editor.create.already_exists", name);
            return;
        }

        if (location == null) {
            location = player.getLocation();
        }

        if (world != null) {
            location.setWorld(world);
        }

        DefaultHologram hologram = new DefaultHologram(name, location);
        DefaultHologramPage page = (DefaultHologramPage) hologram.getPage(0);
        if (page == null) {
            page = new DefaultHologramPage(hologram);
            hologram.addPage(page);
        }
        page.setLinesFromStrings(empty ? Collections.singletonList("<EMPTY>") : Arrays.asList(
                "&7Use &3&l/dh edit&r &7to edit me!",
                "&7Use &3&l/dh move&r &7to move me!",
                "&7Use &3&l/dh delete&r &7to delete me!",
                "&7Use &3&l/dh wiki&r &7to view the &b&nWiki&7!"
        ));
        hologram.getConfig().save();
        hologram.getVisibilityManager().setVisibleByDefault(true);
        hologram.recalculate();

        registry.registerHologram(hologram);

        Lang.confTell(player, "editor.create.success", hologram.getName());

        if (move && PLUGIN.getEditor().getMoveController().initiate(player, hologram)) {
            Lang.confTell(player, "editor.move.initiate", hologram.getName());
        }
    }

    // ==================== DELETE COMMAND ==================== //

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " delete|del [name]")
    @CommandDescription("Delete a hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void delete(
            @NonNull CommandSender sender,
            @Argument(value = "name", suggestions = "holograms") String name
    ) {
        Optional<DefaultHologram> optionalHologram = CommandCommons.getEditableHologramInViewOrByName(sender, name);
        if (!optionalHologram.isPresent()) {
            return;
        }
        DefaultHologram hologram = optionalHologram.get();
        DefaultHologramRegistry registry = PLUGIN.getHologramRegistry();

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

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " move|mv [name]", requiredSender = Player.class)
    @CommandDescription("Move a hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void move(
            @NonNull Player player,
            @Argument(value = "name", suggestions = "not_moving_holograms") String name
    ) {
        // Cancel move if already moving
        if (PLUGIN.getEditor().getMoveController().cancel(player)) {
            Lang.confTell(player, "editor.move.cancel");
            return;
        }

        Optional<DefaultHologram> optionalHologram = CommandCommons.getEditableHologramInViewOrByName(player, name);
        if (!optionalHologram.isPresent()) {
            return;
        }
        DefaultHologram hologram = optionalHologram.get();

        if (hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder) {
            Lang.confTell(player, "editor.move.already_being_moved");
            return;
        }

        if (PLUGIN.getEditor().getMoveController().initiate(player, hologram)) {
            Lang.confTell(player, "editor.move.initiate", hologram.getName());
        }
    }

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " movehere|mvhr <name>", requiredSender = Player.class)
    @CommandDescription("Move a hologram to yourself")
    @CommandPermission(Config.ADMIN_PERM)
    public void movehere(
            @NonNull Player player,
            @Argument(value = "name", suggestions = "not_moving_holograms") String name
    ) {
        Optional<DefaultHologram> optionalHologram = CommandCommons.getEditableHologramInViewOrByName(player, name);
        if (!optionalHologram.isPresent()) {
            return;
        }
        DefaultHologram hologram = optionalHologram.get();

        if (hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder) {
            Lang.confTell(player, "editor.move.already_being_moved");
            return;
        }

        hologram.getPositionManager().setLocation(player.getLocation());
        hologram.recalculate();
        hologram.getConfig().save();
        Lang.confTell(player, "editor.move.success", hologram.getName());
    }

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " moveto|mvto <name> <world> <x> <y> <z>")
    @CommandDescription("Move a hologram to a location")
    @CommandPermission(Config.ADMIN_PERM)
    public void moveto(
            @NonNull CommandSender sender,
            @Argument(value = "name", suggestions = "not_moving_holograms") String name,
            @Argument(value = "world") World world,
            @Argument(value = "x", suggestions = "coordinate") String xCoordinate,
            @Argument(value = "y", suggestions = "coordinate") String yCoordinate,
            @Argument(value = "z", suggestions = "coordinate") String zCoordinate
    ) {
        Optional<DefaultHologram> optionalHologram = CommandCommons.getEditableHologramInViewOrByName(sender, name);
        if (!optionalHologram.isPresent()) {
            return;
        }
        DefaultHologram hologram = optionalHologram.get();
        PositionManager positionManager = hologram.getPositionManager();
        if (positionManager.getLocationBinder() instanceof MoveLocationBinder) {
            Lang.confTell(sender, "editor.move.already_being_moved");
            return;
        }

        if (world == null) {
            Lang.confTell(sender, "editor.error.invalid_world");
            return;
        }

        Location location;
        try {
            double x = CommandCommons.parseCoordinate(xCoordinate, () -> positionManager.getLocation().getX());
            double y = CommandCommons.parseCoordinate(yCoordinate, () -> positionManager.getLocation().getY());
            double z = CommandCommons.parseCoordinate(zCoordinate, () -> positionManager.getLocation().getZ());
            location = new Location(world, x, y, z);
        } catch (NumberFormatException e) {
            Lang.confTell(sender, "editor.error.invalid_location");
            return;
        }

        positionManager.setLocation(location);
        hologram.recalculate();
        hologram.getConfig().save();
        Lang.confTell(sender, "editor.move.success", hologram.getName());
    }


    // ==================== TELEPORT COMMAND ==================== //

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " teleport|goto|tp <name>", requiredSender = Player.class)
    @CommandDescription("Teleport to a hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void teleport(
            @NonNull Player player,
            @Argument(value = "name", suggestions = "not_moving_holograms") String name
    ) {
        Optional<DefaultHologram> optionalHologram = CommandCommons.getEditableHologramInViewOrByName(player, name);
        if (!optionalHologram.isPresent()) {
            return;
        }
        DefaultHologram hologram = optionalHologram.get();

        Location location = hologram.getPositionManager().getLocation();
        player.teleport(location);

        Lang.confTell(player, "editor.teleported", hologram.getName());
    }

    // ==================== EDIT COMMAND ==================== //

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " edit [name]", requiredSender = Player.class)
    @CommandDescription("Edit a hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void edit(
            @NonNull Player player,
            @Argument(value = "name", suggestions = "holograms") String name
    ) {
        Optional<DefaultHologram> optionalHologram = CommandCommons.getEditableHologramInViewOrByName(player, name);
        if (!optionalHologram.isPresent()) {
            return;
        }
        DefaultHologram hologram = optionalHologram.get();

//        PLUGIN.getEditor().getEditController().initiate(player, hologram); // TODO
        Lang.confTell(player, "editor.edit.initiate", hologram.getName());
    }

    // ==================== WAILA COMMAND ==================== //

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " waila|wtf|what", requiredSender = Player.class)
    @CommandDescription("Tells you what hologram you're looking at")
    @CommandPermission(Config.ADMIN_PERM)
    public void waila(@NonNull Player player) {
        DefaultHologram hologram = CommandCommons.getHologramInView(player);
        if (hologram == null) {
            Lang.confTell(player, "editor.waila.not_found");
            return;
        }

        Lang.confTell(player, "editor.waila.found", hologram.getName());
        if (!hologram.getSettings().isEditable()) {
            Lang.confTell(player, "editor.not_editable", hologram.getName());
        }
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
                .filter(hologram -> hologram.getSettings().isEditable())
                .filter(hologram -> !(hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder))
                .map(DefaultHologram::getName)
                .filter(name -> input == null || input.isEmpty() || Common.startsWithIgnoreCase(name, input))
                .collect(Collectors.toList());
    }

    @Suggestions("coordinate")
    public List<String> suggestCoordinate(CommandContext<CommandSender> context, String input) {
        return Arrays.asList("0", "~");
    }

}

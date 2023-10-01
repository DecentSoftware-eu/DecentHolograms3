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

package eu.decentsoftware.holograms.commands.subcommands.hologram;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.utils.location.Position3D;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class HologramCreateCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramCreateCommand(DecentHolograms plugin) {
        super(
                "create",
                Config.ADMIN_PERM,
                "/dh hologram create <name> [<x> <y> <z>] [world]",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram create <name> [<x> <y> <z>] [world]",
                        "&b> &8∙ &b<name> &8- &7Name of the hologram to create.",
                        "&b> &8∙ &b[<x> <y> <z>] &8- &7Position of the hologram.",
                        "&b> &8∙ &b[world] &8- &7World of the hologram.",
                        "&b> &8∙ &b[--move, -m] &8- &7Start moving the hologram after creation.",
                        "&b> &8∙ &b[--empty, -e] &8- &7Create an empty hologram.",
                        "&b>",
                        "&b> &fThis command creates a new hologram with the given name.",
                        "&b>  &fIf the move flag is specified, you will be able to move the",
                        "&b> &fhologram after creation. (Player only) If the empty flag is",
                        "&b> &fspecified, the hologram will be created empty, this means",
                        "&b> &fthat it will not have the default lines.",
                        "&b>  &fIf you don't specify a position, the hologram will be created",
                        "&b> &fat your current location. (Only if you are a player)",
                        "&b>  &fIf you don't specify a world, the hologram will be created",
                        "&b> &fin the default world.",
                        "&b>",
                        "&b> &7Aliases: &bcreate, make, new, c",
                        " "
                ),
                "make", "new", "c"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 0) {
            return false;
        }

        boolean move = args.extractFlag("--move", "-m");
        boolean empty = args.extractFlag("--empty", "-e");
        String name = args.nextString().orElse(null);
        if (name == null || !name.matches(Config.NAME_REGEX)) {
            Lang.confTell(sender, "editor.error.invalid_hologram_name", name);
            return true;
        }

        if (plugin.getHologramRegistry().isHologramRegistered(name)) {
            Lang.confTell(sender, "editor.error.hologram_already_exists", name);
            return true;
        }

        Location location;
        if (sender instanceof Player) {
            location = ((Player) sender).getLocation();
        } else {
            Position3D position3D = args.next(Position3D.class).orElse(null);
            World world = args.next(World.class).orElse(Bukkit.getWorlds().get(0));
            if (position3D == null || world == null) {
                Lang.confTell(sender, "editor.error.invalid_position");
                return true;
            }
            location = new Location(world, position3D.getX(), position3D.getY(), position3D.getZ());
        }

        DefaultHologram hologram = new DefaultHologram(name, location, true, true);
        HologramPage page = hologram.getPage(0) == null ? hologram.addPage() : hologram.getPage(0);
        if (page == null) {
            // Should never happen
            return true;
        }

        if (empty) {
            page.setLinesFromStrings(Collections.singletonList("<empty>"));
        } else {
            page.setLinesFromStrings(Arrays.asList(
                    "&7Use &3&l/dh edit&r &7to edit me!",
                    "&7Use &3&l/dh move&r &7to move me!",
                    "&7Use &3&l/dh delete&r &7to delete me!",
                    "&7Use &3&l/dh wiki&r &7to view the &b&nWiki&7!"
            ));
        }

        hologram.getConfig().save();
        hologram.getVisibilityManager().setVisibleByDefault(true);
        hologram.recalculate();

        plugin.getHologramRegistry().registerHologram(hologram);
        Lang.confTell(sender, "editor.create.success", hologram.getName());

        // TODO: send some options on next edits

        if (move && sender instanceof Player) {
            if (plugin.getEditor().getMoveController().initiate((Player) sender, hologram)) {
                Lang.confTell(sender, "editor.move.initiate", name);
            }
        }
        return true;
    }

}

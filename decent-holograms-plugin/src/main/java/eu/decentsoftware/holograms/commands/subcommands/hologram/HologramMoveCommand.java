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
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.commands.utils.CommandCommons;
import eu.decentsoftware.holograms.commands.utils.TabCompleteCommons;
import eu.decentsoftware.holograms.editor.move.MoveController;
import eu.decentsoftware.holograms.editor.move.MoveLocationBinder;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.utils.location.Position3D;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HologramMoveCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramMoveCommand(DecentHolograms plugin) {
        super(
                "move",
                Config.ADMIN_PERM,
                "/dh hologram move <hologram> [<x> <y> <z>] [world]",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram move <hologram> [<x> <y> <z>] [world]",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to move.",
                        "&b> &8∙ &b[<x> <y> <z>] &8- &7New location. (Optional)",
                        "&b> &8∙ &b[world] &8- &7New world. (Optional)",
                        "&b>",
                        "&b>  &fIf coordinates are not specified, the hologram will",
                        "&b> &fstart moving with your view direction. (Player only)",
                        "&b>  &fIf coordinates are specified, the hologram will be",
                        "&b> &fmoved to the specified location. (Requires name)",
                        "&b>  &fIf world is not specified, the hologram will be moved",
                        "&b> &fto the same world it is currently in.",
                        "&b>  &fIf hologram name is not specified, the hologram you",
                        "&b> &fare looking at will be moved. (Player only)",
                        "&b>",
                        "&b> &7Aliases: &bmove, mv",
                        " "
                ),
                "mv"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        String name = args.nextString().orElse(null);
        Position3D position = name == null ? null : args.next(Position3D.class).orElse(null);
        World world;
        if (name != null && args.peek().isPresent()) {
            world = args.next(World.class).orElse(null);
            if (world == null) {
                Lang.confTell(sender, "editor.error.invalid_world");
                return true;
            }
        } else {
            world = null;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            DefaultHologram hologram = CommandCommons.getEditableHologramInViewOrByName(plugin.getHologramRegistry(), player, name).orElse(null);
            if (hologram == null) {
                return false;
            }

            if (!hologram.getSettings().isEditable()) {
                Lang.confTell(sender, "editor.error.not_editable");
                return true;
            }

            if (position == null) {
                MoveController moveController = plugin.getEditor().getMoveController();
                if (moveController.cancel(player)) {
                    // Cancel move if already moving
                    Lang.confTell(player, "editor.move.cancel");
                    return true;
                }

                if (hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder) {
                    Lang.confTell(player, "editor.move.already_being_moved");
                    return true;
                }

                moveController.initiate(player, hologram);
                Lang.confTell(player, "editor.move.initiate", hologram.getName());
                return true;
            }

            setHologramLocation(hologram, position, world);

            Lang.confTell(sender, "editor.move.success", hologram.getName());
            return true;
        }

        if (name == null) {
            return false;
        }

        if (position == null) {
            Lang.confTell(sender, "editor.error.invalid_position");
            return true;
        }

        DefaultHologram hologram = plugin.getHologramRegistry().getHologram(name);
        if (hologram == null) {
            Lang.confTell(sender, "editor.error.invalid_hologram_name");
            return true;
        }

        if (!hologram.getSettings().isEditable()) {
            Lang.confTell(sender, "editor.error.not_editable");
            return true;
        }

        setHologramLocation(hologram, position, world);

        Lang.confTell(sender, "editor.move.success", hologram.getName());
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        final int size = args.size();
        if (size == 1) {
            return TabCompleteCommons.getMatchingNotMovingEditableHologramNames(plugin.getHologramRegistry(), args);
        } else if (size == 2) {
            return args.nextString().map(name -> {
                DefaultHologram hologram = plugin.getHologramRegistry().getHologram(name);
                if (hologram != null) {
                    double x = hologram.getPositionManager().getLocation().getX();
                    return Arrays.asList(String.valueOf(x), String.valueOf((int) x), "0", "~");
                }
                return null;
            }).orElse(null);
        } else if (size == 3) {
            return args.nextString().map(name -> {
                DefaultHologram hologram = plugin.getHologramRegistry().getHologram(name);
                if (hologram != null) {
                    double y = hologram.getPositionManager().getLocation().getY();
                    return Arrays.asList(String.valueOf(y), String.valueOf((int) y), "0", "~");
                }
                return null;
            }).orElse(null);
        } else if (size == 4) {
            return args.nextString().map(name -> {
                DefaultHologram hologram = plugin.getHologramRegistry().getHologram(name);
                if (hologram != null) {
                    double z = hologram.getPositionManager().getLocation().getZ();
                    return Arrays.asList(String.valueOf(z), String.valueOf((int) z), "0", "~");
                }
                return null;
            }).orElse(null);
        } else if (size == 5) {
            return Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .filter(name -> name.startsWith(args.peek().orElse("")))
                    .collect(Collectors.toList());
        }
        return super.tabComplete(sender, args);
    }

    private void setHologramLocation(@NonNull DefaultHologram hologram, @NonNull Position3D position, @Nullable World world) {
        Location newLocation = hologram.getPositionManager().getLocation().clone();
        newLocation.setX(position.getX());
        newLocation.setY(position.getY());
        newLocation.setZ(position.getZ());
        if (world != null) {
            newLocation.setWorld(world);
        }

        hologram.getPositionManager().setLocation(newLocation);
        hologram.getConfig().save();
    }

}

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
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.component.DefaultPositionManager;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class HologramCenterCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramCenterCommand(DecentHolograms plugin) {
        super(
                "center",
                Config.ADMIN_PERM,
                "/hologram center <hologram>",
                Arrays.asList(
                        "",
                        "&b> &3&l/hologram center <hologram> [--y]",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to center.",
                        "&b> &8∙ &b[--y] &8- &7If set, the hologram will be centered",
                        "&b>       &7on the Y axis as well.",
                        "&b>",
                        "&b>  &fPositions the given hologram in the center",
                        "&b> &fof the current block.",
                        "&b>  &fIf no hologram is given, the hologram you're",
                        "&b> &flooking at will be centered. (Player only)",
                        "&b> ",
                        "&b> &7Aliases: &bcenter, centre",
                        ""
                ),
                "centre"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        boolean centerOnY = args.extractFlag("y") || args.extractShortFlag("y");
        String name = args.nextString().orElse(null);
        DefaultHologram hologram = CommandCommons.getEditableHologramInViewOrByName(plugin.getHologramRegistry(), sender, name).orElse(null);
        if (hologram == null) {
            Lang.confTell(sender, "editor.error.invalid_hologram_name_or_view", name);
            return true;
        }

        DefaultPositionManager positionManager = hologram.getPositionManager();
        Location newLocation = positionManager.getLocation().clone();
        newLocation.setX(newLocation.getBlockX() + 0.5);
        newLocation.setZ(newLocation.getBlockZ() + 0.5);
        if (centerOnY) {
            newLocation.setY(newLocation.getBlockY() + 0.5);
        }
        positionManager.setLocation(newLocation);
        hologram.getConfig().save();
        Lang.confTell(sender, "editor.hologram.centered", hologram.getName());
        return false;
    }

}

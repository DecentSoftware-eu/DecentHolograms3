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
import eu.decentsoftware.holograms.commands.utils.TabCompleteCommons;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HologramMovehereCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramMovehereCommand(DecentHolograms plugin) {
        super(
                "movehere",
                Config.ADMIN_PERM,
                "/dh hologram movehere <hologram>",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram movehere <hologram>",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to move.",
                        "&b> &8∙ &b[--center, -c] &8- &7Center the hologram to a block.",
                        "&b>",
                        "&b> &fMoves the given hologram to your current location.",
                        "&b>",
                        "&b> &7Aliases:&b movehere, mvhere, mvhr",
                        " "
                ),
                "mvhere", "mvhr"
        );
        this.plugin = plugin;
        setPlayerOnly(true);
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        String name = args.nextString().orElse(null);
        if (name == null) {
            Lang.tell(sender, "editor.error.invalid_arguments");
            return true;
        }

        DefaultHologram hologram = plugin.getHologramRegistry().getHologram(name);
        if (hologram == null) {
            Lang.tell(sender, "editor.error.invalid_hologram_name", name);
            return true;
        }

        if (!hologram.getSettings().isEditable()) {
            Lang.tell(sender, "editor.error.not_editable");
            return true;
        }

        Player player = (Player) sender;

        Location currentLocation = hologram.getPositionManager().getLocation();
        Location newLocation = player.getLocation().clone();
        newLocation.setYaw(currentLocation.getYaw());
        newLocation.setPitch(currentLocation.getPitch());

        hologram.getPositionManager().setLocation(newLocation);
        hologram.getConfig().save();

        Lang.tell(sender, "editor.move.success", name);
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            return TabCompleteCommons.getMatchingEditableHologramNames(plugin.getHologramRegistry(), args);
        }
        return null;
    }
}

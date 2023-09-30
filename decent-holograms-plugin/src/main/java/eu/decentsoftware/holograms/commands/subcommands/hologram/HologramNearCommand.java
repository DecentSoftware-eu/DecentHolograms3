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
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.commands.utils.TabCompleteCommons;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class HologramNearCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramNearCommand(DecentHolograms plugin) {
        super(
                "near",
                Config.ADMIN_PERM,
                "/hologram near <hologram> <radius>",
                Arrays.asList(
                        "",
                        "&b> &3&l/hologram near <hologram> <radius>",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to teleport.",
                        "&b> &8∙ &b<radius> &8- &7Radius in which to search for players.",
                        "&b>",
                        "&b> &fTeleports the given hologram to the nearest player.",
                        "&b> ",
                        "&b> &7Aliases: &bnear, tpnear, teleportnear",
                        ""
                ),
                "near", "tpnear", "teleportnear"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        // TODO: implement near command
        return false;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            return TabCompleteCommons.getMatchingHologramNames(plugin.getHologramRegistry(), args);
        }
        return null;
    }
}

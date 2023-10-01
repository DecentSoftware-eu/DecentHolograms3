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
                "/dh hologram near <radius>",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram near <radius>",
                        "&b> &8∙ &b<radius> &8- &7Radius in which to search.",
                        "&b>",
                        "&b> &fLists all holograms within the specified radius.",
                        "&b> ",
                        "&b> &7Aliases: &bnear, nearest, nearby",
                        " "
                ),
                "nearest", "nearby"
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
            return Arrays.asList("5", "10", "15", "20", "25", "30", "35", "40", "45", "50");
        }
        return null;
    }
}

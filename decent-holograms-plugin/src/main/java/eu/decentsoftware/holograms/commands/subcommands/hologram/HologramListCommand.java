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

public class HologramListCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramListCommand(DecentHolograms plugin) {
        super(
                "list",
                Config.ADMIN_PERM,
                "/dh h list [page] [search]",
                Arrays.asList(
                        "",
                        "&b> &3&l/dh h list [page] [search]",
                        "&b> &8∙ &b[page] &8- &7Page number. (Optional)",
                        "&b> &8∙ &b[search] &8- &7Search term. (Optional)",
                        "&b>",
                        "&b> &fThis command lists all holograms.",
                        "&b> &fYou can also specify a page number and search for holograms.",
                        "&b>",
                        "&b> &7Aliases: &blist, ls, l",
                        ""
                ),
                "ls"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        // TODO: implement list command
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        // page numbers
        return null;
    }

}

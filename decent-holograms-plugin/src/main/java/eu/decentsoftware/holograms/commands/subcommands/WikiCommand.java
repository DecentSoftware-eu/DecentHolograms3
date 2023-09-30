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

package eu.decentsoftware.holograms.commands.subcommands;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class WikiCommand extends DecentCommand {

    public WikiCommand() {
        super(
                "wiki",
                Config.ADMIN_PERM,
                "/dh wiki [search]",
                Arrays.asList(
                        "",
                        "&b> &3&l/dh wiki [search]",
                        "&b> &8∙ &b[search] &8- &7Optional search query.",
                        "&b>",
                        "&b> &fLinks you to the wiki of DecentHolograms",
                        "&b> &fwhere you can find all the information",
                        "&b> &fyou need to use this plugin.",
                        "&b>",
                        "&b> &7Aliases: &bwiki, docs, documentation",
                        ""
                ),
                "docs", "documentation"
        );
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        String link = "https://wiki.decentholograms.eu/";
        String query = args.nextString(true).orElse(null);
        if (query != null) {
            link += "?q=" + query;
        }
        Lang.tell(sender, "{prefix}Here is the link to the wiki: &b" + link);
        return true;
    }

}

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

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.commands.subcommands.*;
import eu.decentsoftware.holograms.commands.subcommands.hologram.HologramCommand;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RootCommand extends DecentCommand {

    public RootCommand(DecentHolograms plugin) {
        super(
                "decentholograms",
                null,
                "/dh",
                Arrays.asList(
                        " ",
                        " &3&lDECENT HOLOGRAMS",
                        " &fGeneral usage of commands.",
                        " ",
                        " &3Arguments:",
                        " &8∙ &b<required> &8- &7This argument is required.",
                        " &8∙ &b[optional] &8- &7This argument is optional.",
                        " ",
                        " &8The parenthesis are not a part of the final command.",
                        " ",
                        " &3Root aliases:",
                        " &8∙ &b/dh, /decentholograms, /holograms, /hologram, /holo",
                        " ",
                        " &7Use &b&n/dh help&7 to view all available commands.",
                        " ",
                        " &6&lTIP! &eYou can hover over any command in the help message to see some more detailed information. Typing the command without arguments also shows you the same information.",
                        " "
                ),
                "dh", "holograms", "hologram", "holo"
        );
        setHidden(true);

        subCommands.add(new HelpCommand());
        subCommands.add(new VersionCommand());
        subCommands.add(new ReloadCommand(plugin));
        subCommands.add(new WikiCommand());
        subCommands.add(new SymbolsCommand());
        subCommands.add(new ViewMessageCommand());

        subCommands.add(new HologramCommand(plugin));

    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (!sender.hasPermission(Config.ADMIN_PERM)) {
            Lang.sendVersionMessage(sender);
            return true;
        }
        sendDescription(sender);
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            return subCommands.stream()
                    .map(DecentCommand::getName)
                    .filter(name -> name.startsWith(args.nextString().orElse("")))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}

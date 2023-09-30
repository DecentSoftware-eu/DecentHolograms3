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

public class ViewMessageCommand extends DecentCommand {

    public ViewMessageCommand() {
        super(
                "viewmessage",
                Config.ADMIN_PERM,
                "/dh viewmessage <path> [arguments]",
                Arrays.asList(
                        "",
                        "&b> &3&l/dh viewmessage <path>",
                        "&b> &8∙ &b<path> &8- &7Path to the message to view.",
                        "&b> &8∙ &b[arguments] &8- &7Arguments to format the message.",
                        "&b>",
                        "&b> &fView the message at the given path",
                        "&b> &fin the lang.yml file.",
                        "&b>",
                        "&b> &7Aliases: &bviewmessage, vm",
                        ""
                ),
                "vm"
        );
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        String path = args.nextString().orElse(null);
        if (path == null) {
            Lang.confTell(sender, "commands.viewmessage.no-path");
            return true;
        }

        String argumentsString = args.nextString(true).orElse(null);
        if (argumentsString != null) {
            String[] arguments = argumentsString.split(" ");
            Lang.confTell(sender, path, (Object[]) arguments);
            return true;
        }

        Lang.confTell(sender, path);
        return true;
    }

}

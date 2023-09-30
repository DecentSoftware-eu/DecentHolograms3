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

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class VersionCommand extends DecentCommand {

    public VersionCommand() {
        super(
                "version",
                null,
                "/dh version",
                Arrays.asList(
                        "",
                        "&b> &3&l/dh version",
                        "&b>",
                        "&b> &fThis command displays some information about the",
                        "&b> &fplugin's version.",
                        "&b>",
                        "&b> &7Aliases: &bversion, ver, about",
                        ""
                ),
                "ver", "about"
        );
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        Lang.sendVersionMessage(sender);
        return true;
    }

}

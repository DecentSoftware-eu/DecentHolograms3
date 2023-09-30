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
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class SymbolsCommand extends DecentCommand {

    public SymbolsCommand() {
        super(
                "symbols",
                Config.ADMIN_PERM,
                "/hologram symbols",
                Arrays.asList(
                        "",
                        "&b> &3&l/hologram symbols",
                        "&b>",
                        "&b> &fView a list of symbols that can be used",
                        "&b> &fin holograms. Clicking on a symbol will",
                        "&b> &fcopy it to your clipboard so you can paste",
                        "&b> &fit into a hologram.",
                        "&b>",
                        "&b> &7Aliases: &bsymbols, emojis, emoji, symbol",
                        ""
                ),
                "emojis", "emoji", "symbol"
        );
        setPlayerOnly(true);
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        // TODO: Implement symbols command
        return true;
    }

}

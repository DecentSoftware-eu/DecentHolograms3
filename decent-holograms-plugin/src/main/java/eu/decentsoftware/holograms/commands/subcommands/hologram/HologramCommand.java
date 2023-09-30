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
import eu.decentsoftware.holograms.utils.ComponentMessage;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class HologramCommand extends DecentCommand {

    public HologramCommand(DecentHolograms plugin) {
        super(
                "hologram",
                Config.ADMIN_PERM,
                "/dh hologram ...",
                Collections.emptyList(),
                "holograms", "holo", "h"
        );
        subCommands.add(new HologramCenterCommand(plugin));
        subCommands.add(new HologramCloneCommand(plugin));
        subCommands.add(new HologramCreateCommand(plugin));
        subCommands.add(new HologramDeleteCommand(plugin));
        subCommands.add(new HologramInfoCommand(plugin));
        subCommands.add(new HologramListCommand(plugin));
        subCommands.add(new HologramMoveCommand(plugin));
        subCommands.add(new HologramMovehereCommand(plugin));
        subCommands.add(new HologramNearCommand(plugin));
        subCommands.add(new HologramRenameCommand(plugin));
        subCommands.add(new HologramSettingsCommand(plugin));
        subCommands.add(new HologramTeleportCommand(plugin));
        subCommands.add(new HologramWailaCommand(plugin));
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        return false;
    }

    @Override
    protected void sendDescription(@NonNull CommandSender sender) {
        ComponentMessage message = new ComponentMessage("");
        message.appendLine("&b> &3&l/dh holograms ...");
        for (DecentCommand subCommand : subCommands) {
            message.appendLine("&b> &b... " + subCommand.getName());
            message.hoverText(Lang.formatString(subCommand.getDescription(), sender));
            message.clickCommand("/dh holograms " + subCommand.getName());
        }
        message.appendLine("&b>");
        message.appendLine("&b> &7Aliases: &bhologram, holograms, holo, h");
        message.send(sender);
    }
}
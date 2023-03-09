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

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.processing.CommandContainer;
import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class contains all commands related to the plugin.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings("unused")
@CommandContainer
public class DecentHologramsCommand {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    // ==================== MAIN COMMAND ==================== //

    @CommandMethod(CommandCommons.ROOT_ALIASES)
    @CommandDescription("The main command of the plugin.")
    public void dh(@NonNull CommandSender sender) {
        if (sender.hasPermission(Config.ADMIN_PERM)) {
            Lang.confTell(sender, "plugin.help");
        } else {
            Lang.sendVersionMessage(sender);
        }
    }

    // ==================== RELOAD COMMAND ==================== //

    @CommandMethod(CommandCommons.ROOT_ALIASES + " reload")
    @CommandDescription("Reload the plugin")
    @CommandPermission(Config.ADMIN_PERM)
    public void reload(@NonNull CommandSender sender) {
        SchedulerUtil.async(() -> {
            long start = System.currentTimeMillis();
            PLUGIN.reload();
            long end = System.currentTimeMillis();
            Lang.confTell(sender, "plugin.reloaded", end - start);
        });
    }

    // ==================== VERSION COMMAND ==================== //

    @CommandMethod(CommandCommons.ROOT_ALIASES + " version|ver")
    @CommandDescription("Show the plugin version")
    @CommandPermission(Config.ADMIN_PERM)
    public void version(@NonNull CommandSender sender) {
        Lang.sendVersionMessage(sender);
    }

    // ==================== WIKI COMMAND ==================== //

    @CommandMethod(CommandCommons.ROOT_ALIASES + " wiki")
    @CommandDescription("Show the plugin wiki")
    @CommandPermission(Config.ADMIN_PERM)
    public void wiki(@NonNull CommandSender sender) {
        Lang.confTell(sender, "plugin.wiki");
    }

    // ==================== EMOJIS COMMAND ==================== //

    @CommandMethod(value = CommandCommons.ROOT_ALIASES + " emojis", requiredSender = Player.class)
    @CommandDescription("Show a list of useful emojis")
    public void emojis(@NonNull Player player) {
        Lang.confTell(player, "emojis");
    }

    // ==================== VIEW MESSAGE COMMAND ==================== //

    @CommandMethod(CommandCommons.ROOT_ALIASES + " viewmessage <path> [args]")
    @CommandDescription("View a message from the lang.yml file.")
    @CommandPermission(Config.ADMIN_PERM)
    public void viewMessage(
            @NonNull CommandSender sender,
            @NonNull @Argument(value = "path") String path,
            @Argument(value = "args") String[] args
    ) {
        if (args != null) {
            Lang.confTell(sender, path, (Object[]) args);
            return;
        }
        Lang.confTell(sender, path);
    }

}

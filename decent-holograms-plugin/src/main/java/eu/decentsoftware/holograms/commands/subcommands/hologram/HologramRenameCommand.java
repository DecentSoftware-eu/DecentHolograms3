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
import eu.decentsoftware.holograms.commands.utils.TabCompleteCommons;
import eu.decentsoftware.holograms.internal.PluginHologram;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class HologramRenameCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramRenameCommand(DecentHolograms plugin) {
        super(
                "rename",
                Config.ADMIN_PERM,
                "/dh hologram rename <hologram> <new name>",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram rename <hologram> <new name>",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to rename.",
                        "&b> &8∙ &b<new name> &8- &7New name of the hologram.",
                        "&b>",
                        "&b> &fRenames the given hologram to the given name.",
                        "&b>",
                        "&b> &7Aliases: &brename, rn",
                        " "
                )
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() < 2) {
            return false;
        }

        PluginHologram hologram = args.next(PluginHologram.class).orElse(null);
        if (hologram == null) {
            Lang.confTell(sender, "editor.error.invalid_hologram_name", args.peek(-1).orElse(""));
            return true;
        }

        String newName = args.nextString().orElse(null);
        if (newName == null || !newName.matches(Config.NAME_REGEX)) {
            Lang.confTell(sender, "editor.error.invalid_new_name", args.peek(-1).orElse(""));
            return true;
        }

        if (plugin.getHologramManager().hasHologram(newName)) {
            Lang.confTell(sender, "editor.error.already_exists", newName);
            return true;
        }

        PluginHologram newHologram = hologram.copy(newName);
        plugin.getHologramManager().removeHologram(hologram.getName());
        hologram.delete();
        plugin.getHologramManager().registerHologram(newHologram);
        Lang.confTell(sender, "editor.rename.success", hologram.getName(), newName);
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            return TabCompleteCommons.getMatchingNotMovingHologramNames(plugin.getHologramManager(), args);
        }
        return super.tabComplete(sender, args);
    }

}

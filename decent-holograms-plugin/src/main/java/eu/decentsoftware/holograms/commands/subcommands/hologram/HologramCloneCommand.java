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
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class HologramCloneCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramCloneCommand(DecentHolograms plugin) {
        super(
                "clone",
                Config.ADMIN_PERM,
                "/hologram clone <hologram> <new name>",
                Arrays.asList(
                        "",
                        "&b> &3&l/hologram clone <hologram> <new name>",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to clone.",
                        "&b> &8∙ &b<new name> &8- &7Name of the new hologram.",
                        "&b>",
                        "&b> &fCreates a clone of the given hologram at your",
                        "&b> &flocation, with the given name.",
                        "&b> ",
                        "&b> &7Aliases: &bclone, copy",
                        ""
                ),
                "copy"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() < 2) {
            return false;
        }

        DefaultHologram hologram = args.next(DefaultHologram.class).orElse(null);
        if (hologram == null) {
            Lang.confTell(sender, "editor.error.invalid_hologram_name", args.peek(-1).orElse(""));
            return true;
        }

        String newName = args.nextString().orElse(null);
        if (newName == null || !newName.matches(Config.NAME_REGEX)) {
            Lang.confTell(sender, "editor.error.invalid_new_name", args.peek(-1).orElse(""));
            return true;
        }

        if (plugin.getHologramRegistry().isHologramRegistered(newName)) {
            Lang.confTell(sender, "editor.error.already_exists", newName);
            return true;
        }

        DefaultHologram newHologram = hologram.copy(newName);
        plugin.getHologramRegistry().registerHologram(newHologram);
        Lang.confTell(sender, "editor.clone.success", hologram.getName(), newHologram.getName());
        return false;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        final int size = args.size();
        if (size == 1) {
            return TabCompleteCommons.getMatchingNotMovingEditableHologramNames(plugin.getHologramRegistry(), args);
        }
        return super.tabComplete(sender, args);
    }

}

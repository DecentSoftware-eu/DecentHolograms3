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
import eu.decentsoftware.holograms.commands.utils.CommandCommons;
import eu.decentsoftware.holograms.commands.utils.TabCompleteCommons;
import eu.decentsoftware.holograms.editor.move.MoveLocationBinder;
import eu.decentsoftware.holograms.internal.PluginHologram;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class HologramDeleteCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramDeleteCommand(@NonNull DecentHolograms plugin) {
        super(
                "delete",
                Config.ADMIN_PERM,
                "/dh hologram delete <hologram>",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram delete <hologram>",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to delete.",
                        "&b>",
                        "&b> &fThis command deletes the hologram with the given name.",
                        "&b> &fIf no hologram is specified, the hologram you are looking",
                        "&b> &fat will be deleted.",
                        "&b>",
                        "&b> &7Aliases:&b delete, del, remove, rm",
                        " "
                ),
                "delete", "del", "remove", "rm"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        String name = args.nextString().orElse(null);
        PluginHologram hologram = CommandCommons.getHologramInViewOrByName(plugin.getHologramManager(), sender, name).orElse(null);
        if (hologram == null) {
            Lang.confTell(sender, "editor.error.invalid_hologram_name_or_view", name);
            return true;
        }

        if (hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder) {
            MoveLocationBinder binder = (MoveLocationBinder) hologram.getPositionManager().getLocationBinder();
            plugin.getEditor().getMoveController().cancel(binder.getPlayer());
            Lang.confTell(binder.getPlayer(), "editor.move.cancel");
        }

        plugin.getHologramManager().removeHologram(hologram.getName());
        hologram.delete();

        Lang.confTell(sender, "editor.delete.success", hologram.getName());
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            return TabCompleteCommons.getMatchingHologramNames(plugin.getHologramManager(), args);
        }
        return super.tabComplete(sender, args);
    }

}

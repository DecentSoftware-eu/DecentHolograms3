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
import eu.decentsoftware.holograms.internal.PluginHologram;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class HologramWailaCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramWailaCommand(@NonNull DecentHolograms plugin) {
        super(
                "waila",
                Config.ADMIN_PERM,
                "/dh hologram waila",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram waila",
                        "&b>",
                        "&b> &fThis command tells you the name of the hologram",
                        "&b> &fthat you are looking at. (What Am I Looking At?)",
                        "&b>",
                        "&b> &7Aliases: &bwaila, what, wtf",
                        " "
                ),
                "what", "wtf"
        );
        this.plugin = plugin;
        setPlayerOnly(true);
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        Player player = (Player) sender;
        PluginHologram hologram = CommandCommons.getHologramInView(plugin.getHologramManager(), player);
        if (hologram == null) {
            Lang.confTell(player, "editor.waila.not_found");
            return true;
        }

        Lang.confTell(player, "editor.waila.found", hologram.getName());
        return true;
    }

}

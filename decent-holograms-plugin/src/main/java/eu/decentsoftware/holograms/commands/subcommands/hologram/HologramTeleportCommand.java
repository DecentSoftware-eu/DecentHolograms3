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
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.commands.utils.TabCompleteCommons;
import eu.decentsoftware.holograms.internal.PluginHologram;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HologramTeleportCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramTeleportCommand(DecentHolograms plugin) {
        super(
                "teleport",
                Config.ADMIN_PERM,
                "/dh hologram teleport <hologram>",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram teleport <hologram>",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to teleport to.",
                        "&b>",
                        "&b> &fTeleports you to the location of the hologram.",
                        "&b>",
                        "&b> &7Aliases: &bteleport, tp, tele",
                        " "
                ),
                "tp", "tele"
        );
        this.plugin = plugin;
        setPlayerOnly(true);
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        String name = args.nextString().orElse(null);
        if (name == null) {
            Lang.confTell(sender, "editor.error.invalid_arguments");
            return true;
        }

        PluginHologram hologram = plugin.getHologramManager().getHologram(name);
        if (hologram == null) {
            Lang.confTell(sender, "editor.error.hologram_not_found", name);
            return true;
        }

        Player player = (Player) sender;
        DecentLocation location = hologram.getPositionManager().getLocation();
        Location bukkitLocation = location.toBukkitLocation();

        if (bukkitLocation != null) {
            player.teleport(bukkitLocation);
            Lang.confTell(sender, "editor.teleport.success", name);
        }

        Lang.confTell(sender, "editor.teleport.fail", name);
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

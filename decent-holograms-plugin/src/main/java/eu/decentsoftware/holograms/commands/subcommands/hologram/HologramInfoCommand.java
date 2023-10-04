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
import eu.decentsoftware.holograms.commands.utils.CommandCommons;
import eu.decentsoftware.holograms.commands.utils.TabCompleteCommons;
import eu.decentsoftware.holograms.internal.PluginHologram;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HologramInfoCommand extends DecentCommand {

    private final DecentHolograms plugin;

    public HologramInfoCommand(@NonNull DecentHolograms plugin) {
        super(
                "info",
                Config.ADMIN_PERM,
                "/dh hologram info <hologram>",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram info <hologram>",
                        "&b> &8∙ &b<hologram> &8- &7Name of the hologram to get info about.",
                        "&b>",
                        "&b> &fShows some information about the given hologram.",
                        "&b> &fIf hologram name is not specified, the hologram you",
                        "&b> &fare looking at will be used. (Player only)",
                        "&b> ",
                        "&b> &7Aliases: &binfo, inspect, about",
                        " "
                ),
                "inspect", "about"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        String name = args.nextString().orElse(null);
        PluginHologram hologram = CommandCommons.getHologramInViewOrByName(plugin.getHologramManager(), sender, name).orElse(null);
        if (hologram == null) {
            return false;
        }

        sender.sendMessage(" ");
        Lang.tell(sender, " &3&lHOLOGRAM INFO");
        sender.sendMessage(" ");
        Lang.tell(sender, " &8∙ &7Name: &b{0}", hologram.getName());
        Lang.tell(sender, " &8∙ &7Enabled: &b{0}", hologram.getSettings().isEnabled());
        Lang.tell(sender, " &8∙ &7Location: &b{0}", formatLocation(hologram.getPositionManager().getLocation()));
        Lang.tell(sender, " &8∙ &7Pages: &b{0} ({1} lines)", hologram.getPages().size(), hologram.getPages().stream().mapToInt(page -> page.getLines().size()).sum());
        sender.sendMessage(" ");
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            return TabCompleteCommons.getMatchingHologramNames(plugin.getHologramManager(), args);
        }
        return super.tabComplete(sender, args);
    }

    private String formatLocation(DecentLocation location) {
        return String.format(
                "%s, %.2f, %.2f, %.2f",
                Objects.requireNonNull(location.getWorld()).getName(),
                location.getX(),
                location.getY(),
                location.getZ()
        );
    }

}

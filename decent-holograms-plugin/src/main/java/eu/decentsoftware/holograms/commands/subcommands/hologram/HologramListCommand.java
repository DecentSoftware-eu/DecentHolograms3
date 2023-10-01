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
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.utils.ComponentMessage;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class HologramListCommand extends DecentCommand {

    private static final int HOLOGRAMS_PER_PAGE = 10;
    private final DecentHolograms plugin;

    public HologramListCommand(DecentHolograms plugin) {
        super(
                "list",
                Config.ADMIN_PERM,
                "/dh hologram list [page] [search]",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram list [page] [search]",
                        "&b> &8∙ &b[page] &8- &7Page number. (Optional)",
                        "&b> &8∙ &b[search] &8- &7Search term. (Optional)",
                        "&b>",
                        "&b> &fThis command lists all holograms.",
                        "&b>",
                        "&b> &7Aliases: &blist, ls, l",
                        " "
                ),
                "ls"
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        Collection<DefaultHologram> holograms = plugin.getHologramRegistry().getHolograms();
        int page = args.nextInteger().orElse(1);
        String search = args.nextString().orElse(null);
        if (search != null) {
            holograms = plugin.getHologramRegistry().getHolograms().stream()
                    .filter(hologram -> hologram.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        int totalPages = (int) Math.ceil((double) holograms.size() / HOLOGRAMS_PER_PAGE);
        page = Math.max(1, Math.min(page, totalPages));
        holograms = holograms.stream()
                .skip((long) (page - 1) * HOLOGRAMS_PER_PAGE)
                .limit(HOLOGRAMS_PER_PAGE)
                .collect(Collectors.toList());

        if (holograms.isEmpty()) {
            if (search != null) {
                Lang.confTell(sender, "editor.list.no_holograms_matching", search);
            } else {
                Lang.confTell(sender, "editor.list.no_holograms");
            }
            return true;
        }

        sender.sendMessage(" ");
        sender.sendMessage(" &3&lHOLOGRAMS LIST &7[" + page + "/" + totalPages + "]");
        if (search != null) {
            sender.sendMessage(" &fList of all " + holograms.size() + " holograms matching \"" + search + "\".");
        } else {
            sender.sendMessage(" &fList of all " + holograms.size() + " holograms.");
        }
        sender.sendMessage(" ");

        for (DefaultHologram hologram : holograms) {
            if (sender instanceof Player) {
                new ComponentMessage(Lang.formatString(" &8∙ "))
                        .append(Lang.formatString("&b" + hologram.getName()))
                        .hoverText(Lang.formatString("&aClick to teleport to this hologram."))
                        .clickCommand("/dh hologram tp " + hologram.getName())
                        .send((Player) sender);
            } else {
                Lang.tell(sender, " &8∙ &b" + hologram.getName());
            }
        }

        sender.sendMessage(" ");
        if (sender instanceof Player) {
            CommandCommons.sendChatPaginationButtons((Player) sender, page, totalPages, "/dh hologram list %d");
            sender.sendMessage(" ");
        }
        return true;
    }

}

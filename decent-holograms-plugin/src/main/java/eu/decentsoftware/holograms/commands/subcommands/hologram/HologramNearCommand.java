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
import eu.decentsoftware.holograms.internal.PluginHologram;
import eu.decentsoftware.holograms.utils.ComponentMessage;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HologramNearCommand extends DecentCommand {

    private static final int HOLOGRAMS_PER_PAGE = 10;
    private final DecentHolograms plugin;

    public HologramNearCommand(@NonNull DecentHolograms plugin) {
        super(
                "near",
                Config.ADMIN_PERM,
                "/dh hologram near <radius> [page]",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh hologram near <radius> [page]",
                        "&b> &8∙ &b<radius> &8- &7Radius in which to search.",
                        "&b> &8∙ &b[page] &8- &7Page of the list to display.",
                        "&b>",
                        "&b> &fLists all holograms within the specified radius.",
                        "&b> ",
                        "&b> &7Aliases: &bnear, nearest, nearby",
                        " "
                ),
                "nearest", "nearby"
        );
        setPlayerOnly(true);

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        double radius = args.nextDouble(1, Double.MAX_VALUE).orElse(0d);
        if (radius == 0d) {
            return false;
        }

        Location playerLocation = ((Player) sender).getLocation();
        List<PluginHologram> nearHolograms = plugin.getHologramManager().getHolograms().stream()
                .filter(hologram -> {
                    DecentLocation location = hologram.getPositionManager().getLocation();
                    return Objects.equals(location.getWorld(), playerLocation.getWorld())
                            && location.distanceSquared(playerLocation) <= radius * radius;
                })
                .collect(Collectors.toList());
        if (nearHolograms.isEmpty()) {
            Lang.confTell(sender, "editor.near.no-holograms");
            return true;
        }

        int totalPages = (int) Math.ceil(nearHolograms.size() / (double) HOLOGRAMS_PER_PAGE);
        int page = args.nextInteger(1, totalPages).orElse(1);
        int fromIndex = (page - 1) * HOLOGRAMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + HOLOGRAMS_PER_PAGE, nearHolograms.size());
        nearHolograms = nearHolograms.subList(fromIndex, toIndex);

        sender.sendMessage(" ");
        Lang.tell(sender, " &3&lHOLOGRAMS NEARBY &7[{0}/{1}]", page, totalPages);
        Lang.tell(sender, String.format(" &fAll holograms (%d) within %.2f blocks.", nearHolograms.size(), radius));
        sender.sendMessage(" ");
        nearHolograms.forEach(hologram -> {
            double distance = hologram.getPositionManager().getLocation().distance(playerLocation);
            new ComponentMessage(Lang.formatString(" &8∙ "))
                    .append(Lang.formatString(String.format("&b%s &8(%.2f blocks away)", hologram.getName(), distance)))
                    .hoverText(Lang.formatString("&aClick to teleport to this hologram."))
                    .clickCommand("/dh hologram tp " + hologram.getName())
                    .send((Player) sender);
        });
        sender.sendMessage(" ");
        CommandCommons.sendChatPaginationButtons((Player) sender, page, totalPages, "/dh hologram near " + radius + " %d");
        sender.sendMessage(" ");
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            return Arrays.asList("5", "10", "15", "20", "25", "30", "35", "40", "45", "50");
        }
        return null;
    }
}

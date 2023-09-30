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

package eu.decentsoftware.holograms.commands.subcommands;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import eu.decentsoftware.holograms.utils.ComponentMessage;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelpCommand extends DecentCommand {

    private static final int COMMANDS_PER_PAGE = 10;

    public HelpCommand() {
        super(
                "help",
                Config.ADMIN_PERM,
                "/dh help",
                Arrays.asList(
                        " ",
                        "&b> &3&l/dh help [page]",
                        "&b> &8∙ &b[page] &8- &7Number of the page to view. &8(Default: 1)",
                        "&b>",
                        "&b> &fThis command displays the help message. You can",
                        "&b> &falso specify a page number to view a specific page.",
                        "&b>",
                        "&b> &7Aliases: &bhelp, ?",
                        " "
                ),
                "?"
        );
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        List<DecentCommand> commands = DecentCommand.COMMANDS.stream()
                .filter(command -> !command.isHidden())
                .collect(Collectors.toList());
        int totalCommands = commands.size();
        int totalPages = totalCommands / COMMANDS_PER_PAGE + (totalCommands % COMMANDS_PER_PAGE == 0 ? 0 : 1);
        int page = args.nextInteger(1, totalPages).orElse(1);
        int pageZeroBased = page - 1;

        sender.sendMessage("");
        Lang.tell(sender, " &3&lDECENT HOLOGRAMS HELP &7[" + page + "/" + totalPages + "]");
        Lang.tell(sender, " &fList of all " + totalCommands + " commands.");
        sender.sendMessage("");

        int start = pageZeroBased * COMMANDS_PER_PAGE;
        int end = Math.min(totalCommands, page * COMMANDS_PER_PAGE);
        List<DecentCommand> commandsOnPage = commands.subList(start, end);
        for (DecentCommand command : commandsOnPage) {
            if (sender instanceof Player) {
                new ComponentMessage(Lang.formatString(" &8∙ &b" + command.getUsage()))
                        .hoverText(Lang.formatString(command.getDescription()))
                        .clickSuggest(command.getUsage())
                        .send((Player) sender);
            } else {
                Lang.tell(sender, " &8∙ &b" + command.getUsage());
            }
        }

        sender.sendMessage("");
        if (sender instanceof Player) {
            sendPaginationButtons((Player) sender, page, totalPages);
            sender.sendMessage("");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            int totalCommands = (int) DecentCommand.COMMANDS.stream()
                    .filter(command -> !command.isHidden())
                    .count();
            return IntStream.range(1, totalCommands / COMMANDS_PER_PAGE + 2)
                    .mapToObj(Integer::toString)
                    .filter(page -> page.startsWith(args.nextString().orElse("")))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void sendPaginationButtons(Player player, int page, int totalPages) {
        ComponentMessage message = new ComponentMessage(" ");
        message.append(Lang.formatString("&3««««« Prev Page"));
        if (page > 1) {
            message.clickCommand("/dh help " + (page - 1));
            message.hoverText(Lang.formatString("&aClick to view the previous page"));
        }
        message.append(Lang.formatString("&3 | "));
        message.reset();
        message.append(Lang.formatString("&3Next Page »»»»»"));
        if (page < totalPages) {
            message.clickCommand("/dh help " + (page + 1));
            message.hoverText(Lang.formatString("&aClick to view the next page"));
        }
        message.send(player);
    }

}
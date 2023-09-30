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

import java.util.ArrayList;
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
                        "",
                        "&b> &3&l/dh help [page]",
                        "&b> &8∙ &b[page] &8- &7Number of the page to view. &8(Default: 1)",
                        "&b>",
                        "&b> &fThis command displays the help message. You can",
                        "&b> &falso specify a page number to view a specific page.",
                        "&b>",
                        "&b> &7Aliases: &bhelp, ?",
                        ""
                ),
                "?"
        );
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull Arguments args) {
        List<DecentCommand> commands = new ArrayList<>(DecentCommand.COMMANDS.values());
        int totalCommands = commands.size();
        int totalPages = totalCommands / COMMANDS_PER_PAGE + 2;
        int page = args.nextInteger(1, totalPages).orElse(1);
        int pageZeroBased = page - 1;

        sender.sendMessage("");
        Lang.tell(sender, " &3&lDECENT HOLOGRAMS &8- &7HELP &8[&b" + page + "&7/&b" + totalPages + "&8]");
        Lang.tell(sender, " &fList of all " + totalCommands + " (sub)commands.");
        sender.sendMessage("");

        List<DecentCommand> commandsOnPage = commands.subList(pageZeroBased * COMMANDS_PER_PAGE, (pageZeroBased + 1) * COMMANDS_PER_PAGE);
        for (DecentCommand command : commandsOnPage) {
            new ComponentMessage(" &8∙ &b" + command.getUsage())
                    .hoverText(command.getDescription())
                    .clickSuggest(command.getUsage())
                    .send(sender);
        }

        sender.sendMessage("");
        Lang.tell(sender, " &6&lTIP! &eYou can hover over any command in the help message to see some more detailed information. Typing the command without arguments also shows you the same information.");
        sender.sendMessage("");
        sendPaginationButtons(sender, page, totalPages);
        sender.sendMessage("");
        return true;
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        if (args.size() == 1) {
            return IntStream.range(1, DecentCommand.COMMANDS.size() / COMMANDS_PER_PAGE + 2)
                    .mapToObj(Integer::toString)
                    .filter(page -> page.startsWith(args.nextString().orElse("")))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void sendPaginationButtons(CommandSender sender, int page, int totalPages) {
        ComponentMessage message = new ComponentMessage(" ");
        message.append("&b««««« Prev Page");
        if (page > 1) {
            message.clickCommand("/dh help " + (page - 1));
            message.hoverText("&bClick to view the previous page");
        }
        message.append("&b | ");
        message.append("&bNext Page »»»»»");
        if (page < totalPages) {
            message.clickCommand("/dh help " + (page + 1));
            message.hoverText("Click to view the next page");
        }
        message.send(sender);
    }

}
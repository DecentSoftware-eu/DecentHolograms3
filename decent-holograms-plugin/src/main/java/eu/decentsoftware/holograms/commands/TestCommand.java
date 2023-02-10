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

package eu.decentsoftware.holograms.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.processing.CommandContainer;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

@CommandContainer
public class TestCommand {

    @CommandMethod("test <price> <message>")
    @CommandDescription("Test command")
    @CommandPermission("decentholograms.test")
    public void test(
            final @NonNull CommandSender sender,
            final @NonNull @Argument("message") String message,
            final @Argument("price") double price
    ) {
        if (price < 0) {
            sender.sendMessage("Price cannot be negative.");
            return;
        }
        sender.sendMessage("Test command: " + message + " " + price);
    }

}

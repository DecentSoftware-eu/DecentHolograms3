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
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collections;

@CommandContainer
public class TestCommand {

    @CommandMethod("test <line>")
    @CommandDescription("Test command")
    @CommandPermission("decentholograms.test")
    public void test(
            final @NonNull Player player,
            final @NonNull @Argument("line") String line
    ) {
        DecentHologramsAPI api = DecentHologramsAPI.getInstance();

        Hologram hologram = api.createHologram(player.getLocation(), Collections.singletonList(line));
        hologram.getVisibilityManager().setVisibleByDefault(false);
        hologram.getVisibilityManager().setVisibility(player, true);

        player.sendMessage("Created hologram with line '" + line + "'! Only you can see it.");
    }

}

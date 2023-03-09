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
import cloud.commandframework.annotations.specifier.Greedy;
import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.line.DefaultHologramLine;
import eu.decentsoftware.holograms.hologram.page.DefaultHologramPage;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * This class contains all commands related to lines.
 *
 * @author d0by
 * @see DecentHologramsCommand
 * @since 3.0.0
 */
@SuppressWarnings("unused")
@CommandContainer
public class LineCommands {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    // ==================== SET LINE COMMAND ==================== //

    @CommandMethod(CommandCommons.ROOT_ALIASES + " setline|setl|sl <name> <page> <line> <text>")
    @CommandDescription("Set a line of a hologram")
    @CommandPermission(Config.ADMIN_PERM)
    public void setLine(
            @NonNull CommandSender sender,
            @Argument(value = "name", suggestions = "holograms") String name,
            @Argument(value = "page") int pageIndex,
            @Argument(value = "line") int lineIndex,
            @Argument(value = "text") @Greedy String text
    ) {
        Optional<DefaultHologram> optionalHologram = CommandCommons.getEditableHologramInViewOrByName(sender, name);
        if (!optionalHologram.isPresent()) {
            return;
        }
        DefaultHologram hologram = optionalHologram.get();

        DefaultHologramPage page = (DefaultHologramPage) hologram.getPage(pageIndex - 1);
        if (page == null) {
            Lang.confTell(sender, "editor.error.invalid_page", pageIndex);
            return;
        }

        DefaultHologramLine line = (DefaultHologramLine) page.getLine(lineIndex - 1);
        if (line == null) {
            Lang.confTell(sender, "editor.error.invalid_line", lineIndex);
            return;
        }

        line.setContent(text);
        hologram.getConfig().save();
        hologram.recalculate();
        hologram.getVisibilityManager().updateContents();

        Lang.confTell(sender, "editor.setline.success", hologram.getName(), page, line, text);
    }

}

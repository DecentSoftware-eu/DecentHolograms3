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

package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.actions.Action;
import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandAction extends Action {

    protected final @NotNull String command;

    public CommandAction(@NotNull String command) {
        this.command = command;
    }

    public CommandAction(long delay, double chance, @NotNull String command) {
        super(delay, chance);
        this.command = command;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            Bukkit.dispatchCommand(player, command);
        }
    }

}

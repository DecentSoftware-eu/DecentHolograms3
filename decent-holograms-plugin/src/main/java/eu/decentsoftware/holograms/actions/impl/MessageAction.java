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

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.actions.Action;
import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends Action {

    protected final @NotNull String message;

    public MessageAction(@NotNull String message) {
        this.message = message;
    }

    public MessageAction(long delay, double chance, @NotNull String message) {
        super(delay, chance);
        this.message = message;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            Lang.tell(player, message);
        }
    }

}

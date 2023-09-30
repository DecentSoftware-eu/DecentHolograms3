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

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.actions.Action;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionBarAction extends Action {

    private final @NotNull String message;

    public ActionBarAction(@NotNull String message) {
        this.message = message;
    }

    public ActionBarAction(long delay, double chance, @NotNull String message) {
        super(delay, chance);
        this.message = message;
    }

    @Override
    public void execute(@NotNull Player player) {
        NMSAdapter nmsAdapter = DecentHolograms.getInstance().getNMSManager().getAdapter();
        nmsAdapter.sendActionBar(player, Lang.formatString(message, player));
    }

}

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
import eu.decentsoftware.holograms.actions.Action;
import eu.decentsoftware.holograms.utils.BungeeUtils;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class ConnectAction extends Action {

    protected final String server;

    public ConnectAction(@NonNull DecentHolograms plugin, @NonNull String server) {
        super(plugin);
        this.server = server;
    }

    public ConnectAction(@NonNull DecentHolograms plugin, long delay, double chance, @NonNull String server) {
        super(plugin, delay, chance);
        this.server = server;
    }

    @Override
    public void execute(@NonNull Player player) {
        BungeeUtils.connect(player, server);
    }

}

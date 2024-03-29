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

package eu.decentsoftware.holograms.internal;

import eu.decentsoftware.holograms.core.CoreHologram;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.CoreHologramView;
import eu.decentsoftware.holograms.core.CoreHologramVisibilityManager;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class PluginHologramVisibilityManager extends CoreHologramVisibilityManager {

    public PluginHologramVisibilityManager(@NonNull CoreHologram<?> parent) {
        super(parent);
    }

    @Override
    protected boolean checkHologramViewConditions(@NonNull Player player) {
        if (this.parent instanceof PluginHologram) {
            PluginHologram hologram = (PluginHologram) this.parent;
            return hologram.getViewConditions().check(player);
        }
        return super.checkHologramViewConditions(player);
    }

    @Override
    protected CoreHologramView createView(@NonNull Player player, @NonNull CoreHologramPage<?> page) {
        return new PluginHologramView(player, this.parent, page);
    }

}

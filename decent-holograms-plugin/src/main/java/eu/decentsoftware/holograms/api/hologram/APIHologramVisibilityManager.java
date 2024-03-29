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

package eu.decentsoftware.holograms.api.hologram;

import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.CoreHologramView;
import eu.decentsoftware.holograms.core.CoreHologramVisibilityManager;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class APIHologramVisibilityManager extends CoreHologramVisibilityManager implements HologramVisibilityManager {

    public APIHologramVisibilityManager(@NonNull APIHologram parent) {
        super(parent);
        this.defaultVisibility = Visibility.VISIBLE;
    }

    @Override
    protected CoreHologramView createView(@NonNull Player player, @NonNull CoreHologramPage<?> page) {
        return new APIHologramView(player, this.parent, page);
    }

}

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

package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.api.component.hologram.HologramSettings;
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.page.HologramPage;
import eu.decentsoftware.holograms.components.common.DefaultPositionManager;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DefaultLinePositionManager extends DefaultPositionManager {

    private final @NotNull HologramLine parent;

    public DefaultLinePositionManager(@NotNull HologramLine parent, @NotNull Location location) {
        super(location);
        this.parent = parent;
    }

    public DefaultLinePositionManager(@NotNull HologramLine parent, @NotNull Location location, Supplier<Location> locationSupplier) {
        super(location, locationSupplier);
        this.parent = parent;
    }

    @NotNull
    @Override
    public Location getActualLocation() {
        Location actualLocation = locationSupplier != null ? locationSupplier.get().clone() : location.clone();
        HologramPage page = parent.getParent();
        HologramSettings settings = page.getParent().getSettings();
        if (!settings.isRotateVertical() && !settings.isRotateHorizontal()) {
            return actualLocation
                    .add(
                            parent.getSettings().getOffsetX(),
                            parent.getSettings().getOffsetY(),
                            parent.getSettings().getOffsetZ()
                    )
                    .add(offsets);
        }
        return actualLocation;
    }
}

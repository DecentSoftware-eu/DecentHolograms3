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

import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.core.CorePositionManager;
import lombok.NonNull;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class APIPositionManager extends CorePositionManager implements HologramPositionManager {

    public APIPositionManager(@NonNull DecentLocation location) {
        super(location);
    }

    public APIPositionManager(@NonNull DecentLocation location, @Nullable Supplier<Location> locationSupplier) {
        super(location, locationSupplier);
    }

}
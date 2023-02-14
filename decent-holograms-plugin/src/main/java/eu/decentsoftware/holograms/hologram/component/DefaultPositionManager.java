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

package eu.decentsoftware.holograms.hologram.component;

import eu.decentsoftware.holograms.api.hologram.component.PositionManager;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DefaultPositionManager implements PositionManager {

    protected @NotNull Location location;
    protected @NotNull Vector offsets;
    protected Supplier<Location> locationSupplier;

    /**
     * Create a new instance of {@link DefaultPositionManager} with the given parent hologram.
     *
     * @param location The location of the hologram.
     */
    public DefaultPositionManager(@NotNull Location location) {
        this(location, null);
    }

    /**
     * Create a new instance of {@link DefaultPositionManager} with the given parent hologram.
     *
     * @param location The location of the hologram.
     * @param locationSupplier The supplier of the location of the hologram.
     */
    public DefaultPositionManager(@NotNull Location location, Supplier<Location> locationSupplier) {
        this.location = location;
        this.locationSupplier = locationSupplier;
        this.offsets = new Vector();
    }

    @NotNull
    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    @NotNull
    @Override
    public Location getActualLocation() {
        if (locationSupplier != null) {
            return locationSupplier.get().clone().add(offsets);
        }
        return location.clone().add(offsets);
    }

    @Override
    public void bindLocation(Supplier<Location> locationSupplier) {
        this.locationSupplier = locationSupplier;
    }

    @Override
    public Supplier<Location> getLocationBinder() {
        return locationSupplier;
    }

    @NotNull
    @Override
    public Vector getOffsets() {
        return offsets;
    }

    @Override
    public void setOffsets(@NotNull Vector offsets) {
        this.offsets = offsets;
    }

}

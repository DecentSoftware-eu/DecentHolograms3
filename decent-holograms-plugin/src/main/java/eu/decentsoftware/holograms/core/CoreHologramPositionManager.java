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

package eu.decentsoftware.holograms.core;

import eu.decentsoftware.holograms.api.util.DecentLocation;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class CoreHologramPositionManager {

    protected DecentLocation location;
    protected Vector offsets;
    protected Supplier<Location> locationSupplier;

    @Contract(pure = true)
    public CoreHologramPositionManager(@NonNull DecentLocation location) {
        this(location, null);
    }

    @Contract(pure = true)
    public CoreHologramPositionManager(@NonNull DecentLocation location, @Nullable Supplier<Location> locationSupplier) {
        this.location = location;
        this.locationSupplier = locationSupplier;
        this.offsets = new Vector();
    }

    @NonNull
    public DecentLocation getLocation() {
        return this.location;
    }

    public void setLocation(@NonNull DecentLocation location) {
        this.location = location;
    }

    @NonNull
    public DecentLocation getActualLocation() {
        if (this.locationSupplier != null) {
            Location suppliedLocation = this.locationSupplier.get();
            World world = suppliedLocation.getWorld();
            if (world == null) {
                throw new IllegalStateException("World " + suppliedLocation.getWorld() + " is not loaded!");
            }
            return new DecentLocation(
                    world,
                    suppliedLocation.getX() + this.offsets.getX(),
                    suppliedLocation.getY() + this.offsets.getY(),
                    suppliedLocation.getZ() + this.offsets.getZ()
            );
        }
        return new DecentLocation(
                this.location.getWorldName(),
                this.location.getX() + this.offsets.getX(),
                this.location.getY() + this.offsets.getY(),
                this.location.getZ() + this.offsets.getZ()
        );
    }

    @NonNull
    public Location getActualBukkitLocation() {
        if (this.locationSupplier != null) {
            return this.locationSupplier.get().clone().add(this.offsets);
        }
        Location location = this.location.toBukkitLocation();
        if (location == null) {
            throw new IllegalStateException("World " + this.location.getWorldName() + " is not loaded!");
        }
        return location.add(this.offsets);
    }

    public void bindLocation(@Nullable Supplier<Location> locationSupplier) {
        this.locationSupplier = locationSupplier;
    }

    public void unbindLocation() {
        this.locationSupplier = null;
    }

    @Nullable
    public Supplier<Location> getLocationBinder() {
        return this.locationSupplier;
    }

    public boolean isLocationBound() {
        return this.locationSupplier != null;
    }

    @NonNull
    public Vector getOffsets() {
        return this.offsets;
    }

    public void setOffsets(@NonNull Vector offsets) {
        this.offsets = offsets;
    }

}

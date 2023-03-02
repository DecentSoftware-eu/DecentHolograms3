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

package eu.decentsoftware.holograms.api.hologram.component;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * This class represents a position manager for holograms. This manager is used
 * for storing absolute or relative location of the hologram.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface PositionManager {

    /**
     * Get the location of this hologram.
     *
     * @return The location of this hologram.
     */
    @NotNull
    Location getLocation();

    /**
     * Set the location of this hologram.
     *
     * @param location The location of this hologram.
     */
    void setLocation(@NotNull Location location);

    /**
     * Get the actual location of this hologram. This method returns the location of the hologram
     * with all offsets and animations applied.
     *
     * @return The actual location of this hologram.
     */
    @NotNull
    Location getActualLocation();

    /**
     * Bind this hologram to a supplier of a location. This supplier will be used to get the location
     * of this hologram.
     *
     * <p>This can be used for holograms, that are moving together with another entity or moving in a
     * certain direction.</p>
     *
     * @param locationSupplier The supplier of the location of this hologram. (null to unbind)
     */
    void bindLocation(@Nullable Supplier<Location> locationSupplier);

    /**
     * Unbind this hologram from a supplier of a location (if bound).
     *
     * @see #bindLocation(Supplier)
     */
    default void unbindLocation() {
        bindLocation(null);
    }

    /**
     * Get the supplier of the location of this hologram.
     *
     * @return The supplier of the location of this hologram. (null if not bound)
     * @see #bindLocation(Supplier)
     */
    @Nullable
    Supplier<Location> getLocationBinder();

    /**
     * Check if this hologram is bound to a supplier of a location.
     *
     * @return True if this hologram is bound to a supplier of a location, false otherwise.
     * @see #bindLocation(Supplier)
     */
    default boolean isLocationBound() {
        return getLocationBinder() != null;
    }

    /**
     * Get the offsets of this hologram. The offsets are used to offset the hologram from its
     * location.
     *
     * <p>This is useful for animations that affect the location of the hologram.</p>
     *
     * @return The offsets of this hologram in a {@link Vector}.
     */
    @NotNull
    Vector getOffsets();

    /**
     * Set the offsets of this hologram. The offsets are used to offset the hologram from its
     * location.
     *
     * <p>This is useful for animations that affect the location of the hologram.</p>
     *
     * @param offsets The offsets of this hologram in a {@link Vector}.
     */
    void setOffsets(@NotNull Vector offsets);

}

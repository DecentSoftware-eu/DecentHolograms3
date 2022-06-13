package eu.decent.holograms.api.component.common;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * This class represents a position manager for holograms. This manager is used
 * for storing absolute or relative location of the hologram.
 *
 * @author d0by
 */
public interface PositionManager {

    /**
     * Get the location of this hologram.
     *
     * @return The location of this hologram.
     */
    Location getLocation();

    /**
     * Set the location of this hologram.
     *
     * @param location The location of this hologram.
     */
    void setLocation(Location location);

    /**
     * Get the actual location of this hologram. This method returns the location of the hologram
     * with all offsets and animations applied.
     *
     * @return The actual location of this hologram.
     */
    Location getActualLocation();

    /**
     * Bind this hologram to a supplier of a location. This supplier will be used to get the location
     * of this hologram.
     *
     * <p>This can be used for holograms, that are moving together with another entity or moving in a
     * certain direction.</p>
     *
     * @param locationSupplier The supplier of the location of this hologram.
     */
    void bindLocation(Supplier<Location> locationSupplier);

    /**
     * Unbind this hologram from its location supplier. This method removes the location supplier
     * from this hologram. Normal location will be used from now on.
     */
    void unbindLocation();

    /**
     * Get the offsets of this hologram. The offsets are used to offset the hologram from its
     * location.
     *
     * <p>This is useful for animations that affect the location of the hologram.</p>
     *
     * @return The offsets of this hologram in a {@link Vector}.
     */
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

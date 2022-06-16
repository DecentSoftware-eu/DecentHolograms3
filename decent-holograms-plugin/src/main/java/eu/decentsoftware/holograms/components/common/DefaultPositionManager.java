package eu.decentsoftware.holograms.components.common;

import eu.decentsoftware.holograms.api.component.common.PositionManager;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DefaultPositionManager implements PositionManager {

    private Location location;
    private Supplier<Location> locationSupplier;
    private Vector offsets;

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

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Location getActualLocation() {
        return locationSupplier != null ? locationSupplier.get() : location.add(offsets);
    }

    @Override
    public void bindLocation(Supplier<Location> locationSupplier) {
        this.locationSupplier = locationSupplier;
    }

    @Override
    public void unbindLocation() {
        this.locationSupplier = null;
    }

    @Override
    public Vector getOffsets() {
        return offsets;
    }

    @Override
    public void setOffsets(@NotNull Vector offsets) {
        this.offsets = offsets;
    }

}

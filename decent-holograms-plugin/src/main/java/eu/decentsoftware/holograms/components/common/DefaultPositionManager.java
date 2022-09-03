package eu.decentsoftware.holograms.components.common;

import eu.decentsoftware.holograms.api.component.common.PositionManager;
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

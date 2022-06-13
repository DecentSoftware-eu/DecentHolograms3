package eu.decent.holograms.api.component.common;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a component has a location and can be moved in the world.
 *
 * @author d0by
 */
public interface IMovable {

    /**
     * Get the location of this component.
     *
     * @return The location of this component.
     */
    @NotNull
    Location getLocation();

    /**
     * Set the location of this component.
     *
     * @param location The location of this component.
     */
    void setLocation(@NotNull Location location);

}

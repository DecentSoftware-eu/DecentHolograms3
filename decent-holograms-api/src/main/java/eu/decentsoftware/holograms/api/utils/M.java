package eu.decentsoftware.holograms.api.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for math operations.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class M {

    /**
     * Checks if the given location is inside the given radius from the given center.
     *
     * @param center The center of the circle.
     * @param location The location to check.
     * @param distance The radius of the circle.
     * @return True if the location is inside the circle, false otherwise.
     */
    public static boolean inDistance(@NotNull Location center, @NotNull Location location, double distance) {
        return center.distance(location) <= distance;
    }

    /**
     * Calculates the location in front of the given location.
     *
     * @param location The location to calculate the location in front of.
     * @param distance The distance of the location in front.
     * @return The location in front of the given location.
     */
    @NotNull
    public static Location getLocationInFront(@NotNull Location location, double distance) {
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double x2 = x + distance * Math.sin(Math.toRadians(yaw));
        double z2 = z + distance * Math.cos(Math.toRadians(yaw));
        double y2 = y + distance * Math.sin(Math.toRadians(pitch));
        return new Location(location.getWorld(), x2, y2, z2);
    }

    /**
     * Add the given offset to the given location. This method considers the yaw and pitch of the location
     * and adds the offset to the location in the direction of the yaw and pitch.
     *
     * @param location The location to add the offset to.
     * @param x The x offset.
     * @param y The y offset.
     * @param z The z offset.
     * @return The location with the offset added.
     */
    @NotNull
    public static Location addOffsetsConsideringRotation(@NotNull Location location, double x, double y, double z) {
        // TESTME
        float yaw = location.getYaw();
        double x2 = location.getX() + x * Math.cos(Math.toRadians(yaw)) + z * Math.sin(Math.toRadians(yaw));
        double z2 = location.getZ() - x * Math.sin(Math.toRadians(yaw)) + z * Math.cos(Math.toRadians(yaw));
        double y2 = location.getY() + y;
        return new Location(location.getWorld(), x2, y2, z2);
    }

}

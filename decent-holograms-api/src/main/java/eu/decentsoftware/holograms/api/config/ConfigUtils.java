package eu.decentsoftware.holograms.api.config;

import eu.decentsoftware.holograms.api.exception.LocationParseException;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Utility class with methods for parsing config values.
 *
 * @author d0by
 * @version 3.0.0
 */
@UtilityClass
public final class ConfigUtils {

    /**
     * It takes a location and returns a string that can be used to recreate the location
     *
     * @param l The location to convert to a string.
     * @return A string with the world name, x, y, z, yaw, and pitch of the location.
     */
    @NotNull
    public static String locToString(@NotNull Location l, boolean includeYawPitch) {
        String location = String.format("%s:%.3f:%.3f:%.3f", l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
        if (includeYawPitch) {
            location += String.format(":%.3f:%.3f", l.getYaw(), l.getPitch());
        }
        return location;
    }

    /**
     * It takes a string and returns a location.
     *
     * @param string The string to convert to a location.
     * @return A location with the world name, x, y, z, yaw, and pitch of the string.
     */
    @NotNull
    public static Location stringToLoc(@NotNull String string) throws LocationParseException {
        String[] spl = string.replace(",", ".").split(":");
        Location location;
        if (spl.length >= 4) {
            World world = getWorld(spl[0]);
            if (world == null) {
                throw new LocationParseException(String.format("World '%s' not found.", spl[0]));
            }

            try {
                location = new Location(world, Double.parseDouble(spl[1]), Double.parseDouble(spl[2]), Double.parseDouble(spl[3]));
                if (spl.length >= 6) {
                    location.setYaw(Float.parseFloat(spl[4]));
                    location.setPitch(Float.parseFloat(spl[5]));
                }
                return location;
            } catch (NumberFormatException ignored) {}
        }
        throw new LocationParseException(String.format("Wrong location format: %s", string));
    }

    /**
     * Get a world from a string. The string can be either a world name or a UUID of a world.
     *
     * @param value The string to convert to a world.
     * @return The world.
     */
    private static World getWorld(@NotNull String value) {
        UUID uuid;
        try {
            uuid = UUID.fromString(value);
        } catch (IllegalArgumentException ignored) {
            uuid = null;
        }
        return uuid == null ? Bukkit.getWorld(value) : Bukkit.getWorld(uuid);
    }

}

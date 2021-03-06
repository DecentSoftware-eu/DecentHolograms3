package eu.decentsoftware.holograms.api.utils.config;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.decentsoftware.holograms.api.exception.LocationParseException;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Utility class with methods for parsing config values.
 *
 * @author d0by
 * @since 3.0.0
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
     * Get a location from the given {@link Section}. If the world is missing
     * or invalid, null will be returned. If any of the coordinates are missing or invalid,
     * default value of 0 will be used, this also applies to the yaw and pitch.
     *
     * @param config The configuration section.
     * @return The location or null if the section is not a valid location.
     */
    @Nullable
    public static Location getLocation(@NotNull Section config) {
        String worldName = config.getString("world");
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        double x = config.getDouble("x", 0D);
        double y = config.getDouble("y", 0D);
        double z = config.getDouble("z", 0D);
        float yaw = config.getFloat("yaw", 0F);
        float pitch = config.getFloat("pitch", 0F);
        return new Location(world, x, y, z, yaw, pitch);
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

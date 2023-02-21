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

package eu.decentsoftware.holograms.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleEffect;

/**
 * Utility class for math operations.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class MathUtil {

    public static final Vector UP_VECTOR = new Vector(0, 1, 0);

    /**
     * Checks if the given location is inside the given radius from the given center.
     *
     * @param center   The center of the circle.
     * @param location The location to check.
     * @param distance The radius of the circle.
     * @return True if the location is inside the circle, false otherwise.
     */
    public static boolean inDistance(@NotNull Location center, @NotNull Location location, double distance) {
        if (center.getWorld() != null && location.getWorld() != null && !center.getWorld().equals(location.getWorld())) {
            return false;
        }
        return center.distanceSquared(location) <= distance * distance;
    }

    /**
     * Draws a line of particles from the given location representing the given vector.
     *
     * @param particle The particle to draw.
     * @param start    The location to start the line from.
     * @param dir      The vector to draw the line.
     * @param length   The length of the line.
     * @param step     The step to draw the line.
     */
    public static void drawVectorParticles(@NotNull ParticleEffect particle, @NotNull Location start, @NotNull Vector dir, double length, double step) {
        for (double i = 0; i < length; i += step) {
            Vector vector = dir.clone().normalize().multiply(i);
            start.add(vector);
            particle.display(start, 0, 0, 0, 0, 1, null);
            start.subtract(vector);
        }
    }

    /**
     * Makes the given location look at the given target location.
     *
     * @param location The location to make look at the target location.
     * @param target   The target location.
     * @return The location, looking at the target location.
     */
    @NotNull
    public static Location makeLocationLookAtAnotherLocation(@NotNull Location location, @NotNull Location target) {
        return target.clone().subtract(location.clone());
    }

    /**
     * Convert the given location's pitch and yaw to an EulerAngle.
     *
     * @param loc The location to convert.
     * @return The EulerAngle.
     */
    @NotNull
    public static EulerAngle directionToEuler(@NotNull Location loc) {
        double xzLength = Math.sqrt(loc.getX() * loc.getX() + loc.getZ() * loc.getZ());
        double pitch = Math.atan2(xzLength, loc.getY()) - Math.PI / 2;
        double yaw = -Math.atan2(loc.getX(), loc.getZ()) + Math.PI / 4;
        return new EulerAngle(pitch, yaw, 0);
    }

}

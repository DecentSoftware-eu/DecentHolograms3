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

package eu.decentsoftware.holograms.utils.math;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;

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
    public static boolean inDistance(@NonNull Location center, @NonNull Location location, double distance) {
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
    public static void drawVectorParticles(@NonNull ParticleEffect particle, @NonNull Location start, @NonNull Vector dir, double length, double step) {
        for (double i = 0; i < length; i += step) {
            Vector vector = dir.clone().normalize().multiply(i);
            start.add(vector);
            particle.display(start, 0, 0, 0, 0, 1, null);
            start.subtract(vector);
        }
    }

    /**
     * Draws a line of particles from the given location representing the given vector.
     *
     * @param color  The color of the particles.
     * @param start  The location to start the line from.
     * @param dir    The vector to draw the line.
     * @param length The length of the line.
     * @param step   The step to draw the line.
     */
    public static void drawVectorRedstoneParticles(@NonNull Color color, @NonNull Location start, @NonNull Vector dir, double length, double step) {
        for (double i = 0; i < length; i += step) {
            Vector vector = dir.clone().normalize().multiply(i);
            start.add(vector);
            ParticleEffect.REDSTONE.display(start, color);
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
    @NonNull
    public static Location makeLocationLookAtAnotherLocation(@NonNull Location location, @NonNull Location target) {
        return target.clone().subtract(location.clone());
    }

    /**
     * Convert the given location's pitch and yaw to an EulerAngle.
     *
     * @param loc The location to convert.
     * @return The EulerAngle.
     */
    @NonNull
    public static EulerAngle directionToEuler(@NonNull Location loc) {
        double xzLength = Math.sqrt(loc.getX() * loc.getX() + loc.getZ() * loc.getZ());
        double pitch = Math.atan2(xzLength, loc.getY()) - Math.PI / 2;
        double yaw = -Math.atan2(loc.getX(), loc.getZ()) + Math.PI / 4;
        return new EulerAngle(pitch, yaw, 0);
    }

    /**
     * Find the intersection point of a vector and a plane in a 3D space. The plane is defined by its center,
     * up and right vectors. The ray is defined by its origin and direction. The intersection point is the
     * point where the ray and the plane intersect. The returned vector is the 3D location of the intersection
     * point in the 3D space.
     *
     * @param planeCenter  The center of the plane in a 3D space.
     * @param planeUp      The up vector of the plane. (Not normalized, used to calculate the plane's height)
     * @param planeRight   The right vector of the plane. (Not normalized, used to calculate the plane's width)
     * @param rayOrigin    The origin of the ray.
     * @param rayDirection The direction of the ray.
     * @param maxDistance  The maximum distance of the intersection point from the ray origin.
     * @return The intersection point of the ray and the plane or null if there is no intersection. The returned
     * vector is the 3D location of the intersection point in the 3D space.
     */
    @Nullable
    public static Vector getIntersectionBetweenPlaneAndVector(
            @NonNull Vector rayOrigin,
            @NonNull Vector rayDirection,
            @NonNull Vector planeCenter,
            @NonNull Vector planeUp,
            @NonNull Vector planeRight,
            double maxDistance
    ) {
        Vector planeNormal = planeUp.clone().crossProduct(planeRight).normalize();

        double numerator = planeNormal.dot(planeCenter.clone().subtract(rayOrigin));
        double denominator = planeNormal.dot(rayDirection.normalize());
        double distance = numerator / denominator;

        // If the intersection point is too far away from ray origin, return null
        if (distance > maxDistance || distance <= 0) {
            return null;
        }

        // Calculate the intersection point in 3D space
        Vector intersection = rayOrigin.add(rayDirection.multiply(distance));

        // Convert to 2D coordinates on the hologram plane
        Vector intersectionPointOnPlane = getPointOnPlane(intersection, planeUp, planeRight, planeCenter);
        double x = intersectionPointOnPlane.getX();
        double y = intersectionPointOnPlane.getY();

        double halfWidth = planeRight.length();
        double halfHeight = planeUp.length();

        if (x < -halfWidth || x > halfWidth || y < -halfHeight || y > halfHeight) {
            return null; // The intersection point is outside the plane
        }

        return intersection;
    }

    /**
     * Translate a 3D location into a 2D position on a plane. The plane is defined by its center,
     * up and right vectors. The returned vector is the 2D position on the plane.
     *
     * @param locationToTranslate The 3D location to translate.
     * @param planeUp             The up vector of the plane. (Not normalized, used to calculate the plane's height)
     * @param planeRight          The right vector of the plane. (Not normalized, used to calculate the plane's width)
     * @param planeCenter         The center of the plane in a 3D space.
     * @return The 2D position on the plane. (The Z coordinate is always 0)
     */
    @NonNull
    public static Vector getPointOnPlane(
            @NonNull Vector locationToTranslate,
            @NonNull Vector planeUp,
            @NonNull Vector planeRight,
            @NonNull Vector planeCenter
    ) {
        // Calculate the normal vector of the plane
        Vector normal = planeUp.clone().crossProduct(planeRight).normalize();

        // Calculate the vector from the plane location to the location to translate
        Vector offset = locationToTranslate.clone().subtract(planeCenter);

        // Project the offset vector onto the plane by subtracting the component that is perpendicular to the normal vector
        Vector projectedOffset = offset.subtract(normal.multiply(offset.dot(normal)));

        // Calculate the 2D position on the plane using the dot product with the pivot vectors
        double x = projectedOffset.dot(planeRight.clone().normalize());
        double y = projectedOffset.dot(planeUp.clone().normalize());

        return new Vector(x, y, 0);
    }

}
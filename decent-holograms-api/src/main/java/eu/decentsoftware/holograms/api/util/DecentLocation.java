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

package eu.decentsoftware.holograms.api.util;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A thread-safe, immutable location without yaw and pitch. This class
 * does not store the {@link World} object, but only the world name,
 * which makes it possible to use this class even if the world is not
 * loaded.
 *
 * @author d0by
 * @since 3.0.0
 */
public class DecentLocation {

    private final String worldName;
    private final double x;
    private final double y;
    private final double z;

    /**
     * Create a new instance of DecentLocation.
     *
     * @param worldName The world name.
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @param z         The z coordinate.
     */
    @Contract(pure = true)
    public DecentLocation(@NonNull String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new instance of DecentLocation.
     *
     * @param world The world.
     * @param x     The x coordinate.
     * @param y     The y coordinate.
     * @param z     The z coordinate.
     */
    @Contract(pure = true)
    public DecentLocation(@NonNull World world, double x, double y, double z) {
        this.worldName = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new instance of DecentLocation from a bukkit location.
     *
     * @param location The bukkit location.
     */
    public DecentLocation(@NonNull Location location) {
        this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }

    /**
     * Get the world name of this location.
     *
     * @return The world name of this location.
     */
    @NonNull
    public String getWorldName() {
        return this.worldName;
    }

    /**
     * Get the x coordinate of this location.
     *
     * @return The x coordinate of this location.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Get the block x coordinate of this location.
     *
     * @return The block x coordinate of this location.
     */
    public int getBlockX() {
        return (int) Math.floor(this.x);
    }

    /**
     * Get the y coordinate of this location.
     *
     * @return The y coordinate of this location.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Get the block y coordinate of this location.
     *
     * @return The block y coordinate of this location.
     */
    public int getBlockY() {
        return (int) Math.floor(this.y);
    }

    /**
     * Get the z coordinate of this location.
     *
     * @return The z coordinate of this location.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Get the block z coordinate of this location.
     *
     * @return The block z coordinate of this location.
     */
    public int getBlockZ() {
        return (int) Math.floor(this.z);
    }

    /**
     * Get the bukkit world of this location.
     *
     * @return The bukkit world of this location. Null if the world is not loaded.
     */
    @Nullable
    public World getWorld() {
        return Bukkit.getWorld(getWorldName());
    }

    /**
     * Get the bukkit location of this location.
     *
     * @return The bukkit location of this location. Null if the world is not loaded.
     */
    @Nullable
    public Location toBukkitLocation() {
        World world = Bukkit.getWorld(getWorldName());
        if (world == null) {
            return null;
        }
        return new Location(world, getX(), getY(), getZ());
    }

    /**
     * Get the distance between this location and another location.
     *
     * @param location The other location.
     * @return The distance between the two locations.
     */
    public double distance(@NonNull DecentLocation location) {
        return Math.sqrt(distanceSquared(location));
    }

    /**
     * Get the distance between this location and another location.
     *
     * @param location The other location.
     * @return The distance between the two locations.
     */
    public double distance(@NonNull Location location) {
        return Math.sqrt(distanceSquared(location));
    }

    /**
     * Get the squared distance between this location and another location.
     *
     * @param location The other location.
     * @return The squared distance between the two locations.
     * @throws IllegalArgumentException If the locations are in different worlds.
     */
    public double distanceSquared(@NonNull DecentLocation location) {
        if (!isSameWorld(location)) {
            throw new IllegalArgumentException("Cannot calculate distance between locations in different worlds");
        }
        double dx = getX() - location.getX();
        double dy = getY() - location.getY();
        double dz = getZ() - location.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Get the squared distance between this location and another location.
     *
     * @param location The other location.
     * @return The squared distance between the two locations.
     * @throws IllegalArgumentException If the locations are in different worlds.
     */
    public double distanceSquared(@NonNull Location location) {
        if (!isSameWorld(location)) {
            throw new IllegalArgumentException("Cannot calculate distance between locations in different worlds");
        }
        double dx = getX() - location.getX();
        double dy = getY() - location.getY();
        double dz = getZ() - location.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Check if this location is in the same world as another location.
     *
     * @param location The other location.
     * @return True if the locations are in the same world.
     */
    public boolean isSameWorld(@NonNull DecentLocation location) {
        return getWorldName().equals(location.getWorldName());
    }

    /**
     * Check if this location is in the same world as another location.
     *
     * @param location The other location.
     * @return True if the locations are in the same world.
     */
    public boolean isSameWorld(@NonNull Location location) {
        return getWorldName().equals(location.getWorld().getName());
    }

    /**
     * Check if this location is in the given world.
     *
     * @param world The world.
     * @return True if the location is in the given world.
     */
    public boolean isInWorld(@NonNull World world) {
        return getWorldName().equals(world.getName());
    }

    /**
     * Make an identical copy of this location.
     *
     * @return The copy of this location.
     */
    @NonNull
    public DecentLocation copy() {
        return new DecentLocation(getWorldName(), getX(), getY(), getZ());
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DecentLocation)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        DecentLocation other = (DecentLocation) obj;
        return getWorldName().equals(other.getWorldName())
                && getX() == other.getX()
                && getY() == other.getY()
                && getZ() == other.getZ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorldName(), getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return "DecentLocation{" +
                "world=" + getWorldName() + "," +
                "x=" + getX() + "," +
                "y=" + getY() + "," +
                "z=" + getZ() +
                "}";
    }

}

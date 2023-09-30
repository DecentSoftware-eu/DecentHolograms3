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
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread-safe location without World object.
 *
 * @author d0by
 * @since 3.0.0
 */
public class DecentLocation {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private @NonNull String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    /**
     * Create a new ThreadSafeLocation.
     *
     * @param worldName The world name.
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @param z         The z coordinate.
     * @param yaw       The yaw.
     * @param pitch     The pitch.
     */
    @Contract(pure = true)
    public DecentLocation(@NonNull String worldName, double x, double y, double z, float yaw, float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Create a new ThreadSafeLocation.
     *
     * @param worldName The world name.
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @param z         The z coordinate.
     */
    @Contract(pure = true)
    public DecentLocation(@NonNull String worldName, double x, double y, double z) {
        this(worldName, x, y, z, 0.0f, 0.0f);
    }

    /**
     * Create a new ThreadSafeLocation from a bukkit location.
     *
     * @param location The bukkit location.
     */
    public DecentLocation(@NonNull Location location) {
        this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Get the world name of this location.
     *
     * @return The world name of this location.
     */
    @NonNull
    public String getWorldName() {
        lock.readLock().lock();
        try {
            return worldName;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Set the world name of this location.
     *
     * @param worldName The new world name.
     * @return This location.
     */
    public DecentLocation setWorldName(@NonNull String worldName) {
        lock.writeLock().lock();
        try {
            this.worldName = worldName;
            return this;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Get the x coordinate of this location.
     *
     * @return The x coordinate of this location.
     */
    public double getX() {
        lock.readLock().lock();
        try {
            return x;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Set the x coordinate of this location.
     *
     * @param x The new x coordinate.
     * @return This location.
     */
    public DecentLocation setX(double x) {
        lock.writeLock().lock();
        try {
            this.x = x;
            return this;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Get the y coordinate of this location.
     *
     * @return The y coordinate of this location.
     */
    public double getY() {
        lock.readLock().lock();
        try {
            return y;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Set the y coordinate of this location.
     *
     * @param y The new y coordinate.
     * @return This location.
     */
    public DecentLocation setY(double y) {
        lock.writeLock().lock();
        try {
            this.y = y;
            return this;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Get the z coordinate of this location.
     *
     * @return The z coordinate of this location.
     */
    public double getZ() {
        lock.readLock().lock();
        try {
            return z;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Set the z coordinate of this location.
     *
     * @param z The new z coordinate.
     * @return This location.
     */
    public DecentLocation setZ(double z) {
        lock.writeLock().lock();
        try {
            this.z = z;
            return this;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Get the yaw of this location.
     *
     * @return The yaw of this location.
     */
    public float getYaw() {
        lock.readLock().lock();
        try {
            return yaw;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Set the yaw of this location.
     *
     * @param yaw The new yaw.
     * @return This location.
     */
    public DecentLocation setYaw(float yaw) {
        lock.writeLock().lock();
        try {
            this.yaw = yaw;
            return this;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Get the pitch of this location.
     *
     * @return The pitch of this location.
     */
    public float getPitch() {
        lock.readLock().lock();
        try {
            return pitch;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Set the pitch of this location.
     *
     * @param pitch The new pitch.
     * @return This location.
     */
    public DecentLocation setPitch(float pitch) {
        lock.writeLock().lock();
        try {
            this.pitch = pitch;
            return this;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public DecentLocation add(double x, double y, double z) {
        lock.writeLock().lock();
        try {
            this.x += x;
            this.y += y;
            this.z += z;
            return this;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public DecentLocation subtract(double x, double y, double z) {
        lock.writeLock().lock();
        try {
            this.x -= x;
            this.y -= y;
            this.z -= z;
            return this;
        } finally {
            lock.writeLock().unlock();
        }
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
        return new Location(world, getX(), getY(), getZ(), getYaw(), getPitch());
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
     * Get the squared distance between this location and another location.
     *
     * @param location The other location.
     * @return The squared distance between the two locations.
     */
    public double distanceSquared(@NonNull DecentLocation location) {
        if (!isSameWorld(location)) {
            throw new IllegalArgumentException("Cannot calculate distance between locations in different worlds");
        }
        final double dx = getX() - location.getX();
        final double dy = getY() - location.getY();
        final double dz = getZ() - location.getZ();
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
     * Check if this location is in the given world.
     *
     * @param world The world.
     * @return True if the location is in the given world.
     */
    public boolean isInWorld(@NonNull World world) {
        return getWorldName().equals(world.getName());
    }

    /**
     * Clone this location.
     *
     * @return A clone of this location.
     */
    @Override
    public DecentLocation clone() {
        return new DecentLocation(getWorldName(), getX(), getY(), getZ(), getYaw(), getPitch());
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
                && getZ() == other.getZ()
                && getYaw() == other.getYaw()
                && getPitch() == other.getPitch();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorldName(), getX(), getY(), getZ(), getYaw(), getPitch());
    }

    @Override
    public String toString() {
        return "DecentLocation{" +
                "world=" + getWorldName() + "," +
                "x=" + getX() + "," +
                "y=" + getY() + "," +
                "z=" + getZ() + "," +
                "yaw=" + getYaw() + "," +
                "pitch=" + getPitch() +
                "}";
    }

}

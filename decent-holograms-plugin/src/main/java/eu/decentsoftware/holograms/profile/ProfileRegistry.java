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

package eu.decentsoftware.holograms.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a registry of profiles.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ProfileRegistry {

    private final Map<String, Profile> profileMap;

    /**
     * Creates a new profile registry.
     */
    public ProfileRegistry() {
        this.profileMap = new ConcurrentHashMap<>();
        this.reload();
    }

    /**
     * Reloads the registry. This will remove all profiles and create new ones for all online players.
     */
    public void reload() {
        this.shutdown();

        // -- Create profiles for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            registerProfile(player.getName());
        }
    }

    /**
     * Shuts down the registry. This will remove all profiles.
     */
    public void shutdown() {
        this.profileMap.clear();
    }

    /**
     * Creates a new profile for the given player.
     *
     * @param name The player's nickname.
     */
    public void registerProfile(@NotNull String name) {
        this.profileMap.put(name, new Profile(name));
    }

    /**
     * Get the profile of the given player.
     *
     * @param name The name of the player.
     * @return The profile or null if the given player doesn't have one.
     */
    public Profile getProfile(@NotNull String name) {
        return this.profileMap.get(name);
    }

    /**
     * Remove the profile of the given player.
     *
     * @param name The name of the player.
     */
    public void removeProfile(@NotNull String name) {
        this.profileMap.remove(name);
    }

}

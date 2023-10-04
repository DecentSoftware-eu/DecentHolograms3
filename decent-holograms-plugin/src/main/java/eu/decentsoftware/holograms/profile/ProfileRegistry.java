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

import eu.decentsoftware.holograms.DecentHolograms;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a registry of profiles.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ProfileRegistry {

    private final DecentHolograms plugin;
    private final Map<UUID, Profile> profileMap = new ConcurrentHashMap<>();
    private final ProfileListener listener;

    public ProfileRegistry(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
        this.listener = new ProfileListener(this);
        this.reload();

        Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
    }

    public synchronized void reload() {
        this.shutdown();

        for (Player player : Bukkit.getOnlinePlayers()) {
            registerProfile(player.getUniqueId());
        }
    }

    public synchronized void shutdown() {
        HandlerList.unregisterAll(this.listener);

        this.profileMap.values().forEach(profile -> {
            Player player = profile.getPlayer();
            if (player != null) {
                profile.getContext().destroyClickableEntity(player);
            }
        });
        this.profileMap.clear();
    }

    /**
     * Creates a new profile for the given player.
     *
     * @param uuid The UUID of the player.
     */
    public void registerProfile(@NonNull UUID uuid) {
        this.profileMap.put(uuid, new Profile(this.plugin, uuid));
    }

    /**
     * Get the profile of the given player.
     *
     * @param uuid The UUID of the player.
     * @return The profile or null if the given player doesn't have one.
     */
    public Profile getProfile(@NonNull UUID uuid) {
        return this.profileMap.get(uuid);
    }

    /**
     * Remove the profile of the given player.
     *
     * @param uuid The UUID of the player.
     */
    public void removeProfile(@NonNull UUID uuid) {
        this.profileMap.remove(uuid);
    }

}

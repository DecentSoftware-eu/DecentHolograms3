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

import eu.decentsoftware.holograms.nms.NMSManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

    private final Map<UUID, Profile> profileMap = new ConcurrentHashMap<>();
    private final NMSManager nmsManager;

    public ProfileRegistry(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
        this.reload();
    }

    public synchronized void reload() {
        this.shutdown();

        // -- Create profiles for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            registerProfile(player.getUniqueId());
            nmsManager.hook(player);
        }
    }

    public synchronized void shutdown() {
        this.profileMap.values().forEach(profile -> {
            Player player = profile.getPlayer();
            if (player != null) {
                nmsManager.unhook(player);

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
    public void registerProfile(@NotNull UUID uuid) {
        this.profileMap.put(uuid, new Profile(uuid));
    }

    /**
     * Get the profile of the given player.
     *
     * @param uuid The UUID of the player.
     * @return The profile or null if the given player doesn't have one.
     */
    public Profile getProfile(@NotNull UUID uuid) {
        return this.profileMap.get(uuid);
    }

    /**
     * Remove the profile of the given player.
     *
     * @param uuid The UUID of the player.
     */
    public void removeProfile(@NotNull UUID uuid) {
        this.profileMap.remove(uuid);
    }

}

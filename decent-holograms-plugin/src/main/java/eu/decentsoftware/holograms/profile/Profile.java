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
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * This class represents a profile of a player.
 *
 * @author d0by
 * @since 3.0.0
 */
public class Profile {

    private final @NotNull UUID uuid;
    private final @NotNull ProfileContext context;

    /**
     * Create a new profile for the given player.
     *
     * @param uuid The player's UUID.
     */
    public Profile(@NotNull UUID uuid) {
        this.uuid = uuid;
        this.context = new ProfileContext();
    }

    /**
     * Get the owner of the profile.
     *
     * @return The owning {@link Player} of the profile or null if the player is not online.
     */
    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Get the nickname of the player owning this profile.
     *
     * @return The nickname of the player owning this profile or null if the player is not online.
     */
    @Nullable
    public String getName() {
        Player player = getPlayer();
        return player != null ? player.getName() : null;
    }

    /**
     * Get the context of this profile.
     *
     * @return The context of this profile.
     * @see ProfileContext
     */
    @NotNull
    public ProfileContext getContext() {
        return context;
    }

}

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

package eu.decentsoftware.holograms.api.hologram;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * This class is responsible for managing the visibility of the hologram
 * for certain players. It is able to show/hide the hologram for specific
 * players and to automatically update the visibility according to the
 * hologram's settings.
 * <p>
 * To modify the visibility of the hologram for a specific player, use
 * {@link #setPlayerVisibility(Player, Visibility)} or {@link #resetPlayerVisibility(Player)}.
 * To get the visibility of the hologram for a specific player,
 * use {@link #getPlayerVisibility(Player)}.
 * <p>
 * Player visibility is always prioritized over the default visibility.
 * <p>
 * To modify the default visibility of the hologram, use {@link #setDefaultVisibility(Visibility)}.
 * To get the default visibility of the hologram, use {@link #getDefaultVisibility()}.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramVisibilityManager {

    /**
     * Set the default visibility of the hologram. This is the visibility used for
     * players that don't have a custom visibility setting.
     *
     * @param visibility The default visibility of the hologram.
     * @see #setDefaultVisibility(Visibility)
     * @see #isVisibleByDefault()
     */
    void setDefaultVisibility(@NonNull Visibility visibility);

    /**
     * Get the default visibility of the hologram. This is the visibility used for
     * players that don't have a custom visibility setting.
     *
     * @return The default visibility of the hologram.
     * @see #setDefaultVisibility(Visibility)
     * @see #isVisibleByDefault()
     */
    @NonNull
    Visibility getDefaultVisibility();

    /**
     * Check if the hologram is visible by default. This is the case if the hologram is not
     * restricted to any players and is automatically visible for all players, that meet any
     * view conditions.
     *
     * @return True if the hologram is visible by default, false otherwise.
     * @see #setDefaultVisibility(Visibility)
     * @see #getDefaultVisibility()
     */
    default boolean isVisibleByDefault() {
        return getDefaultVisibility() == Visibility.VISIBLE;
    }

    /**
     * Set the visibility of the hologram for the given player. Player visibility
     * is always prioritized over the default visibility and the hologram will
     * always be visible for the player if their visibility is set to visible.
     *
     * @param player     The player to set the visibility for.
     * @param visibility The visibility to set.
     * @see #getPlayerVisibility(Player)
     * @see #isVisibleByDefault()
     */
    void setPlayerVisibility(@NonNull Player player, @Nullable Visibility visibility);

    /**
     * Set the visibility of the hologram for the given player to the default
     * visibility.
     *
     * @param player The player to reset the visibility for.
     * @see #setPlayerVisibility(Player, Visibility)
     */
    default void resetPlayerVisibility(@NonNull Player player) {
        setPlayerVisibility(player, null);
    }

    /**
     * Get the visibility of the hologram for the given player. Player visibility
     * is always prioritized over the default visibility, and the hologram will
     * always be visible for the player if their visibility is set to visible.
     *
     * @param player The player to get the visibility for.
     * @return The visibility of the hologram for the given player.
     * @see #setPlayerVisibility(Player, Visibility)
     * @see #isVisibleByDefault()
     */
    @Nullable
    Visibility getPlayerVisibility(@NonNull Player player);

}

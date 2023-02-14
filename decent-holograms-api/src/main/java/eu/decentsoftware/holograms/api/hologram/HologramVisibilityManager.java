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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for managing the visibility of the hologram
 * for certain players. It is able to show/hide the hologram for specific
 * players and to automatically update the visibility according to the
 * holograms view distance setting and view conditions.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramVisibilityManager {

    /**
     * Get the parent hologram of this visibility manager.
     *
     * @return The parent hologram of this visibility manager.
     * @see Hologram
     */
    @NotNull
    Hologram getParent();

    /**
     * Check if the hologram is visible by default. This is the case if the hologram is not
     * restricted to any players and is automatically visible for all players, that meet the
     * view conditions.
     *
     * @return True if the hologram is visible by default, false otherwise.
     */
    boolean isVisibleByDefault();

    /**
     * Set if the hologram is visible by default. This is the case if the hologram is not
     * restricted to any players and is automatically visible for all players, that meet the
     * view conditions.
     *
     * @param visible True if the hologram is visible by default, false otherwise.
     */
    void setVisibleByDefault(boolean visible);

    /**
     * Hides the hologram for all players and clears the visibility cache.
     */
    void destroy();

    /**
     * Get all the players that are currently allowed to see this hologram.
     * This list is only updated using the {@link #show(Player)} and
     * {@link #hide(Player)} methods. The hologram's visibility is automatically
     * updated for all players in this list.
     *
     * @return The set of players that are allowed to see this hologram.
     * @see #updateVisibility()
     */
    @NotNull
    Set<String> getPlayers();

    /**
     * Check if the given player is allowed to see this hologram, i.e. if
     * the player is in the set returned by {@link #getPlayers()}.
     *
     * @param player The player to check.
     * @return True if the player is allowed to see this hologram, false otherwise.
     */
    default boolean canSee(@NotNull Player player) {
        return getPlayers().contains(player.getName());
    }

    /**
     * Get all the players that currently see this hologram according to
     * the view conditions and the view distance setting of this hologram.
     * The hologram's contents are updated for all players in this list.
     *
     * @return The set of nicknames of the player that see this hologram.
     */
    @NotNull
    Set<String> getViewers();

    /**
     * Get all the players that currently see this hologram according to
     * the view conditions and the view distance setting of this hologram.
     * The hologram's contents are updated for all players in this list.
     *
     * @return The set of players that see this hologram.
     */
    default Set<Player> getViewerPlayers() {
        return getViewers().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Get all the players that currently see this hologram according to
     * the view conditions and the view distance setting of this hologram.
     * The hologram's contents are updated for all players in this list.
     *
     * @param page The page of the players to get.
     * @return The set of players that see this hologram at the given page.
     */
    default Set<Player> getViewerPlayers(int page) {
        return getViewers().stream()
                .filter((viewer) -> getPage(viewer) == page)
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Check if the given player is currently seeing this hologram according
     * to the view conditions and the view distance setting of this hologram,
     * i.e. if the player is in the set returned by {@link #getViewers()}.
     *
     * @param player The player to check.
     * @return True if the player is currently seeing this hologram, false otherwise.
     */
    default boolean isViewing(@NotNull Player player) {
        return getViewers().contains(player.getName());
    }

    /**
     * Add the specified player to the set of players that are allowed to see
     * this hologram and set the page index to the given value.
     *
     * @param player The player to add.
     * @param page   The page index to set.
     * @see #getPlayers()
     */
    void show(@NotNull Player player, int page);

    /**
     * Add the specified player to the set of players that are allowed to see
     * this hologram and set the page index to default (0).
     *
     * @param player The player to add.
     * @see #getPlayers()
     */
    default void show(@NotNull Player player) {
        show(player, 0);
    }

    /**
     * Remove the specified player from the set of players that are allowed to
     * see this hologram.
     *
     * @param player The player to remove.
     * @see #getPlayers()
     */
    void hide(@NotNull Player player);

    /**
     * Updates the visibility of the hologram for the specified player. This
     * method checks the holograms view distance setting and view conditions
     * and updates the visibility accordingly.
     *
     * @param player The player to update the visibility for.
     */
    void updateVisibility(@NotNull Player player);

    /**
     * Updates the visibility of the hologram for the specified player. This
     * method displays or hides the hologram according to the specified boolean.
     * This method does not check the holograms view distance setting and view
     * conditions. This method does not update the list of players that are allowed
     * to see this hologram, it only updates the list of players that are currently
     * viewing this hologram.
     *
     * <p>If the specified player is in the allowed players list, the visibility
     * is going to be automatically updated for them on the next update. If not,
     * the visibility will stay at the given value.</p>
     *
     * <p>Keep in mind, that if you only display the hologram to the player using
     * this method, the visibility will not be updated according to the view distance
     * setting and view conditions which may cause visibility problems like the player
     * not seeing the hologram when they go farther away from it and then come back.</p>
     *
     * <p>This method is mainly used internally by the {@link HologramVisibilityManager}
     * and should not be used anywhere else as it may cause unexpected behavior.</p>
     *
     * @param player  The player to update the visibility for.
     * @param visible True to show the hologram, false to hide it.
     * @see #getPlayers()
     * @see #getViewers()
     */
    void updateVisibility(@NotNull Player player, boolean visible);

    /**
     * Updates the visibility of the hologram for all players from the view list.
     *
     * @see #updateVisibility(Player)
     * @see #getPlayers();
     */
    void updateVisibility();

    /**
     * Update the hologram's contents for the specified player. This method
     * does not update the hologram's visibility.
     *
     * @param player The player to update the contents for.
     */
    void updateContents(@NotNull Player player);

    /**
     * Update the holograms contents for all players. This method does not
     * update the holograms' visibility.
     */
    void updateContents();

    /**
     * Get the pages that are currently selected by each player.
     *
     * @return The pages that are currently selected by each player in the form of a map.
     */
    @NotNull
    Map<String, Integer> getPlayerPages();

    /**
     * Get the page that is currently selected by the given player.
     *
     * @param player The player to get the page for.
     * @return The page that is currently selected by the given player.
     */
    default int getPage(@NotNull Player player) {
        return getPlayerPages().getOrDefault(player.getName(), 0);
    }

    /**
     * Get the page that is currently selected by the given player.
     *
     * @param name Nickname of the player to get the page for.
     * @return The page that is currently selected by the given player.
     */
    default int getPage(@NotNull String name) {
        return getPlayerPages().getOrDefault(name, 0);
    }

    /**
     * Set the page that is currently selected by the given player.
     *
     * @param player The player to set the page for.
     * @param page   The page to set.
     */
    void setPage(@NotNull Player player, int page);

}

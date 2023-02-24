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
import java.util.UUID;
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
     * Completely remove the given player from the visibility cache. This means that the player
     * will only be able to see the hologram again, if the hologram is visible by default and
     * the player meets the view conditions.
     * <p>
     * But all custom visibility settings for the player will be removed and all holograms
     * that the player is currently viewing will be hidden. (Not permanently, but until the
     * next update of the hologram's visibility.)
     *
     * @param player The player to remove.
     * @see #updateVisibility(Player, boolean)
     */
    void removePlayer(@NotNull Player player);

    /**
     * Get the visibility settings of players. The key is the UUID of the player and the value
     * is the visibility setting of the player. If the player is not in the map, the default
     * visibility setting is used. (See {@link #isVisibleByDefault()})
     *
     * @return The visibility settings of players.
     * @see Visibility
     */
    @NotNull
    Map<UUID, Visibility> getPlayerVisibility();

    /**
     * Check if the given player is allowed to see this hologram.
     *
     * @param player The player to check.
     * @return True if the player is allowed to see this hologram, false otherwise.
     */
    default boolean canSee(@NotNull Player player) {
        if (getPlayerVisibility().containsKey(player.getUniqueId())) {
            return isVisibleByDefault() || getPlayerVisibility().get(player.getUniqueId()) == Visibility.VISIBLE;
        }
        return isVisibleByDefault();
    }

    /**
     * Get all the players that currently see this hologram according to
     * the view conditions and the view distance setting of this hologram.
     * The hologram's contents are updated for all players in this list.
     *
     * @return The set of nicknames of the player that see this hologram.
     */
    @NotNull
    Set<UUID> getViewers();

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
     * Get the players that are allowed to see the hologram.
     *
     * @return Set of allowed players.
     * @see #canSee(Player)
     */
    default Set<Player> getAllowedPlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(this::canSee)
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
        return getViewers().contains(player.getUniqueId());
    }

    /**
     * Set the visibility setting of the given player. Changing this setting
     * makes the player allowed or disallowed to see the hologram. In case
     * this setting is not set for a player, the default value will be used.
     * The default value being {@link #isVisibleByDefault()}.
     *
     * @param player     The player to set the visibility setting for.
     * @param visibility The visibility setting to set.
     * @see #getPlayerVisibility()
     * @see #isVisibleByDefault()
     */
    void setVisibility(@NotNull Player player, @NotNull Visibility visibility);

    /**
     * Set the visibility setting of the given player. Changing this setting
     * makes the player allowed or disallowed to see the hologram. In case
     * this setting is not set for a player, the default value will be used.
     * The default value being {@link #isVisibleByDefault()}.
     *
     * @param player  The player to set the visibility setting for.
     * @param visible True if the player is allowed to see the hologram, false otherwise.
     * @see #getPlayerVisibility()
     * @see #isVisibleByDefault()
     */
    default void setVisibility(@NotNull Player player, boolean visible) {
        setVisibility(player, visible ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    /**
     * Set the page that is currently selected by the given player.
     *
     * @param player The player to set the page for.
     * @param page   The page to set.
     */
    void setPage(@NotNull Player player, int page);

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
     * @see #getViewers()
     * @see #getAllowedPlayers()
     */
    void updateVisibility(@NotNull Player player, boolean visible);

    /**
     * Updates the visibility of the hologram for all players that are allowed
     * to see it. This method checks the holograms view distance setting and view
     * conditions and updates the visibility accordingly. This method does not
     * update the list of players that are allowed to see this hologram, it only
     * updates the list of players that are currently viewing this hologram.
     *
     * @see #updateVisibility(Player)
     * @see #getAllowedPlayers()
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
    Map<UUID, Integer> getPlayerPages();

    /**
     * Get the page that is currently selected by the given player.
     *
     * @param player The player to get the page for.
     * @return The page that is currently selected by the given player.
     */
    default int getPage(@NotNull Player player) {
        return getPlayerPages().getOrDefault(player.getUniqueId(), 0);
    }

    /**
     * Get the page that is currently selected by the given player.
     *
     * @param uuid UUID of the player to get the page for.
     * @return The page that is currently selected by the given player.
     */
    default int getPage(@NotNull UUID uuid) {
        return getPlayerPages().getOrDefault(uuid, 0);
    }

}

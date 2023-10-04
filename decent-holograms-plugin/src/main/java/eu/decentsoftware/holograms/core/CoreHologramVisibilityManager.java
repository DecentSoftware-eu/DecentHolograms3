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

package eu.decentsoftware.holograms.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import eu.decentsoftware.holograms.api.hologram.HologramVisibilityManager;
import eu.decentsoftware.holograms.api.hologram.Visibility;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.utils.math.MathUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CoreHologramVisibilityManager {

    protected final CoreHologram<?> parent;
    protected final Map<UUID, Visibility> playerVisibilityMap = new ConcurrentHashMap<>();
    protected final Map<UUID, Integer> playerPages = new ConcurrentHashMap<>();
    protected final Set<UUID> currentViewers = new HashSet<>();
    protected Visibility defaultVisibility;

    @Contract(pure = true)
    public CoreHologramVisibilityManager(@NonNull CoreHologram<?> parent) {
        this.parent = parent;
        this.defaultVisibility = Visibility.VISIBLE;
    }

    public void setDefaultVisibility(@NonNull Visibility visibility) {
        this.defaultVisibility = visibility;
    }

    @NonNull
    public Visibility getDefaultVisibility() {
        return defaultVisibility;
    }

    boolean isVisibleByDefault() {
        return defaultVisibility == Visibility.VISIBLE;
    }

    public void setPlayerVisibility(@NonNull Player player, @Nullable Visibility visibility) {
        if (visibility == null) {
            playerVisibilityMap.remove(player.getUniqueId());
            return;
        }
        playerVisibilityMap.put(player.getUniqueId(), visibility);
    }

    @Nullable
    public Visibility getPlayerVisibility(@NonNull Player player) {
        return playerVisibilityMap.get(player.getUniqueId());
    }

    /**
     * Hides the hologram for all players and clears the visibility cache.
     */
    public void destroy() {
        this.defaultVisibility = Visibility.HIDDEN;
        this.playerVisibilityMap.clear();

        getViewersAsPlayers().forEach(player -> updateVisibility(player, false));

        this.playerPages.clear();
        this.currentViewers.clear();
    }

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
    public void removePlayer(@NonNull Player player) {
        this.playerVisibilityMap.remove(player.getUniqueId());

        if (isViewing(player)) {
            updateVisibility(player, false);
        }

        this.playerPages.remove(player.getUniqueId());
    }

    /**
     * Updates the visibility of the hologram for the specified player. This
     * method checks the holograms view distance setting and view conditions
     * and updates the visibility accordingly.
     *
     * @param player The player to update the visibility for.
     */
    public void updateVisibility(@NonNull Player player) {
        if (!canSee(player)) {
            if (isViewing(player)) {
                updateVisibility(player, false);
            }
            return;
        }

        boolean inViewDistance = MathUtil.inDistance(
                parent.getPositionManager().getActualBukkitLocation(),
                player.getLocation(),
                parent.getSettings().getViewDistance()
        );
        boolean visibleByDefault = !isVisibleByDefault();
        if (isViewing(player) && (!inViewDistance || !visibleByDefault)) {
            updateVisibility(player, false);
        } else if (!isViewing(player) && inViewDistance && visibleByDefault) {
            updateVisibility(player, true);
        }
    }

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
    public void updateVisibility(@NonNull Player player, boolean visible) {
        Optional<CoreHologramPage<?>> pageOpt = getPageObject(player);
        pageOpt.ifPresent(page -> {
            if (visible) {
                page.display(player);
                currentViewers.add(player.getUniqueId());
            } else {
                page.hide(player);
                currentViewers.remove(player.getUniqueId());
            }
        });
    }

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
    public void updateVisibility() {
        getAllowedPlayers().forEach(this::updateVisibility);
    }

    /**
     * Update the hologram's contents for the specified player. This method
     * does not update the hologram's visibility.
     *
     * @param player The player to update the contents for.
     */
    public void updateContents(@NonNull Player player) {
        Optional<CoreHologramPage<?>> pageOpt = getPageObject(player);
        pageOpt.ifPresent(page -> page.updateContent(player));
    }

    /**
     * Update the holograms contents for all players. This method does not
     * update the holograms' visibility.
     */
    public void updateContents() {
        getViewersAsPlayers().forEach(this::updateContents);
    }

    /**
     * Set the page that is currently selected by the given player.
     *
     * @param player The player to set the page for.
     * @param page   The page to set.
     */
    public void setPage(@NonNull Player player, int page) {
        CoreHologramPage<?> oldPage = getPage(player);

        playerPages.put(player.getUniqueId(), page);

        if (oldPage != null && isViewing(player)) {
            CoreHologramPage<?> newPage = getPage(player);
            if (newPage != null) {
                int i = 0;
                for (; i < newPage.getLines().size(); i++) {
                    CoreHologramLine newLine = newPage.getLine(i);
                    CoreHologramLine oldLine = oldPage.getLine(i);
                    if (newLine == null || oldLine == null) {
                        continue;
                    }

                    if (newLine.getType() == oldLine.getType()) {
                        newLine.updateContent(player);
                    } else {
                        newLine.display(player);
                        oldLine.hide(player);
                    }
                }
                for (; i < oldPage.getLines().size(); i++) {
                    CoreHologramLine oldLine = oldPage.getLine(i);
                    if (oldLine == null) {
                        continue;
                    }
                    oldLine.hide(player);
                }
            }
        }

        parent.recalculate(player);
        updateVisibility(player);
    }

    /**
     * Check if the given player is currently seeing this hologram according
     * to the view conditions and the view distance setting of this hologram,
     * i.e. if the player is in the set returned by {@link #getViewers()}.
     *
     * @param player The player to check.
     * @return True if the player is currently seeing this hologram, false otherwise.
     */
    private boolean isViewing(@NonNull Player player) {
        return getViewers().contains(player.getUniqueId());
    }

    /**
     * Check if the given player is allowed to see this hologram.
     *
     * @param player The player to check.
     * @return True if the player is allowed to see this hologram, false otherwise.
     */
    private boolean canSee(@NonNull Player player) {
        if (getPlayerVisibilityMap().containsKey(player.getUniqueId())) {
            return isVisibleByDefault() || getPlayerVisibilityMap().get(player.getUniqueId()) == Visibility.VISIBLE;
        }
        return isVisibleByDefault();
    }

    /**
     * Get all the players that currently see this hologram according to
     * the view conditions and the view distance setting of this hologram.
     * The hologram's contents are updated for all players in this list.
     *
     * @return The set of players that see this hologram.
     */
    @NonNull
    public Set<Player> getViewersAsPlayers() {
        return getViewers().stream()
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
    @NonNull
    public Set<Player> getAllowedPlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(this::canSee)
                .collect(Collectors.toSet());
    }
    
    /**
     * Get the page that is currently selected by the given player.
     *
     * @param player The player to get the page for.
     * @return The page that is currently selected by the given player.
     */
    @Nullable
    public CoreHologramPage<?> getPage(@NonNull Player player) {
        return getPageObject(player).orElse(null);
    }

    /**
     * Get the page object for the given player by the page index.
     *
     * @param player The player.
     * @return The page as an optional.
     */
    @NonNull
    private Optional<CoreHologramPage<?>> getPageObject(@NonNull Player player) {
        int pageIndex = getPageIndex(player.getUniqueId());
        CoreHologramPage<?> page = parent.getPage(pageIndex);
        return Optional.ofNullable(page);
    }

    /**
     * Get the page that is currently selected by the given player.
     *
     * @param playerId UUID of the player to get the page for.
     * @return The page that is currently selected by the given player.
     */
    private int getPageIndex(@NonNull UUID playerId) {
        return getPlayerPages().getOrDefault(playerId, 0);
    }

    /**
     * Get the parent hologram of this visibility manager.
     *
     * @return The parent hologram.
     */
    @NonNull
    public CoreHologram<?> getParent() {
        return parent;
    }

    /**
     * Get all the players that currently see this hologram according to
     * the view conditions and the view distance setting of this hologram.
     * The hologram's contents are updated for all players in this list.
     *
     * @return The set of nicknames of the player that see this hologram.
     */
    @NonNull
    public Set<UUID> getViewers() {
        return ImmutableSet.copyOf(currentViewers);
    }

    /**
     * Get the pages that are currently selected by each player.
     *
     * @return The pages that are currently selected by each player in the form of a map.
     */
    @NonNull
    public Map<UUID, Integer> getPlayerPages() {
        return ImmutableMap.copyOf(playerPages);
    }

    /**
     * Get the visibility settings of players. The key is the UUID of the player and the value
     * is the visibility setting of the player. If the player is not in the map, the default
     * visibility setting is used. (See {@link #isVisibleByDefault()})
     *
     * @return The visibility settings of players.
     * @see Visibility
     */
    @NonNull
    public Map<UUID, Visibility> getPlayerVisibilityMap() {
        return ImmutableMap.copyOf(playerVisibilityMap);
    }

}

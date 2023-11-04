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
import eu.decentsoftware.holograms.utils.math.MathUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class CoreHologramVisibilityManager {

    protected final CoreHologram<?> parent;
    protected final Map<UUID, Visibility> playerVisibilityMap = new ConcurrentHashMap<>();
    protected final Map<UUID, CoreHologramView> currentViewers = new ConcurrentHashMap<>();
    protected Visibility defaultVisibility;

    @Contract(pure = true)
    public CoreHologramVisibilityManager(@NonNull CoreHologram<?> parent) {
        this.parent = parent;
        this.defaultVisibility = Visibility.VISIBLE;
    }

    public void setDefaultVisibility(@NonNull Visibility visibility) {
        this.defaultVisibility = visibility;
        updateVisibility();
    }

    @NonNull
    public Visibility getDefaultVisibility() {
        return this.defaultVisibility;
    }

    boolean isVisibleByDefault() {
        return this.defaultVisibility == Visibility.VISIBLE;
    }

    public void setPlayerVisibility(@NonNull Player player, @Nullable Visibility visibility) {
        if (visibility == null) {
            this.playerVisibilityMap.remove(player.getUniqueId());
            return;
        }
        this.playerVisibilityMap.put(player.getUniqueId(), visibility);
        updateVisibility(player);
    }

    @Nullable
    public Visibility getPlayerVisibility(@NonNull Player player) {
        return this.playerVisibilityMap.get(player.getUniqueId());
    }

    protected abstract CoreHologramView createView(@NonNull Player player, @NonNull CoreHologramPage<?> page);

    /**
     * Hides the hologram for all players and clears the visibility cache.
     */
    public void destroy() {
        this.defaultVisibility = Visibility.HIDDEN;
        this.playerVisibilityMap.clear();

        getViewersAsPlayers().forEach(player -> updateVisibility(player, false));

        this.currentViewers.clear();
    }

    /**
     * Completely remove the given player from the visibility cache. This means that the player
     * will only be able to see the hologram again, if the hologram is visible by default and
     * the player meets the view conditions.
     * <p>
     * But all custom visibility settings for the player will be removed and all hologram lines
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

        boolean inViewDistance = isInViewDistance(player);
        boolean isAllowed = checkHologramViewConditions(player);
        boolean canSee = canSee(player);
        if (isViewing(player) && (!inViewDistance || !canSee || !isAllowed)) {
            updateVisibility(player, false);
        } else if (!isViewing(player) && inViewDistance && canSee && isAllowed) {
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
                this.currentViewers.put(player.getUniqueId(), createView(player, page));
            } else {
                page.hide(player);
                this.currentViewers.remove(player.getUniqueId()).destroy();
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
     * @throws IllegalArgumentException If the page is not part of this hologram.
     */
    public void setPage(@NonNull Player player, int page) {
        CoreHologramView view = this.currentViewers.get(player.getUniqueId());
        if (view == null) {
            return;
        }
        view.setCurrentPage(page);
    }

    /**
     * Reset all the pages that are currently selected by each player.
     */
    public void resetPlayerPages() {
        CoreHologramPage<?> firstPage = this.parent.getPage(0);
        for (CoreHologramView view : this.currentViewers.values()) {
            if (firstPage == null || firstPage.isDestroyed()) {
                view.destroy();
                continue;
            }
            view.setCurrentPage(firstPage);
        }
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
     * Check if the given player is within the view distance of this hologram. Meaning
     * that the player is close enough to the hologram to be able to see it.
     *
     * @param player The player to check.
     * @return True if the player is within the view distance of this hologram, false otherwise.
     */
    private boolean isInViewDistance(@NonNull Player player) {
        return MathUtil.inDistance(
                this.parent.getPositionManager().getActualBukkitLocation(),
                player.getLocation(),
                this.parent.getSettings().getViewDistance()
        );
    }

    /**
     * Check if the given player is allowed to see this hologram.
     *
     * @param player The player to check.
     * @return True if the player is allowed to see this hologram, false otherwise.
     */
    protected boolean checkHologramViewConditions(@SuppressWarnings("unused") @NonNull Player player) {
        // Can be overridden by subclasses to add additional checks
        return true;
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
        int pageIndex = getPageIndex(player);
        CoreHologramPage<?> page = this.parent.getPage(pageIndex);
        return Optional.ofNullable(page);
    }

    /**
     * Get the page that is currently selected by the given player.
     *
     * @param player The player to get the page for.
     * @return The page that is currently selected by the given player. If the player
     * is not viewing this hologram, -1 is returned.
     */
    private int getPageIndex(@NonNull Player player) {
        CoreHologramView view = this.currentViewers.get(player.getUniqueId());
        if (view == null) {
            return -1;
        }
        return view.getCurrentPageIndex();
    }

    /**
     * Get the parent hologram of this visibility manager.
     *
     * @return The parent hologram.
     */
    @NonNull
    public CoreHologram<?> getParent() {
        return this.parent;
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
        return ImmutableSet.copyOf(this.currentViewers.keySet());
    }

    public Optional<CoreHologramView> getView(@NonNull Player player) {
        return Optional.ofNullable(this.currentViewers.get(player.getUniqueId()));
    }

    @NonNull
    public Collection<CoreHologramView> getViews() {
        return ImmutableSet.copyOf(this.currentViewers.values());
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
        return ImmutableMap.copyOf(this.playerVisibilityMap);
    }

}

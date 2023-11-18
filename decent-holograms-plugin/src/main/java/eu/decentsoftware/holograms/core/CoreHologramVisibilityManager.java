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
import eu.decentsoftware.holograms.api.hologram.Visibility;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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

    public void destroy() {
        this.defaultVisibility = Visibility.HIDDEN;
        this.playerVisibilityMap.clear();

        getViewersAsPlayers().forEach(this::removePlayer);

        this.currentViewers.clear();
    }

    public void removePlayer(@NonNull Player player) {
        CoreHologramView view = getView(player).orElse(null);
        if (view == null) {
            return;
        }

        view.destroy();

        this.currentViewers.remove(player.getUniqueId());
    }

    public void updateVisibility(@NonNull Player player) {
        CoreHologramView view = getView(player).orElse(null);
        if (view == null) {
            CoreHologramPage<?> page = parent.getPage(0);
            if (page == null) {
                return;
            }

            view = createView(player, page);
            this.currentViewers.put(player.getUniqueId(), view);
        }

        view.updateVisibleLines();
    }

    public void updateVisibility() {
        getAllowedPlayers().forEach(this::updateVisibility);
    }

    public void updateVisibility(CoreHologramPage<?> page) {
        getViews().stream()
                .filter(view -> view.getCurrentPage().equals(page))
                .forEach(CoreHologramView::updateVisibleLines);
    }

    public void updateVisibility(CoreHologramLine line) {
        updateVisibility(line.getParent());
    }

    public void updateContents() {
        getViews().forEach(CoreHologramView::updateVisibleLinesContents);
    }

    public void updateLocations(@NonNull Player player) {
        getView(player).ifPresent(CoreHologramView::updateVisibleLinesLocations);
    }

    /**
     * Set the page currently selected by the given player.
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
     * Get all the players that currently see the given line according to
     * the view conditions and the view distance setting of this hologram.
     * The hologram line's contents are updated for all players in this list.
     *
     * @param line The line to get the viewers for.
     * @return The set of players that see the given line.
     */
    @NonNull
    public Set<Player> getViewersAsPlayers(@NonNull CoreHologramLine line) {
        return getViewers().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(player -> getView(player).map(view -> view.canSeeLine(line)).orElse(false))
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
     * Get the page currently selected by the given player.
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
     * Get the page currently selected by the given player.
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
     * Get the visibility settings of players.
     * The key is the UUID of the player, and the value
     * is the visibility setting of the player.
     * If the player is not in the map, the default
     * visibility setting is used.
     * (See {@link #isVisibleByDefault()})
     *
     * @return The visibility settings of players.
     * @see Visibility
     */
    @NonNull
    public Map<UUID, Visibility> getPlayerVisibilityMap() {
        return ImmutableMap.copyOf(this.playerVisibilityMap);
    }

}

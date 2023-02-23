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

package eu.decentsoftware.holograms.hologram;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.HologramVisibilityManager;
import eu.decentsoftware.holograms.api.hologram.Visibility;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.utils.MathUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DefaultHologramVisibilityManager implements HologramVisibilityManager {

    private final @NotNull DefaultHologram parent;
    private final @NotNull Map<UUID, Visibility> playerVisibility;
    private final @NotNull Map<UUID, Integer> playerPages;
    private final @NotNull Set<UUID> currentViewers;
    private boolean visibleByDefault;

    /**
     * Creates a new instance of {@link DefaultHologramVisibilityManager} with the given parent.
     *
     * @param parent The parent hologram.
     */
    @Contract(pure = true)
    public DefaultHologramVisibilityManager(@NotNull DefaultHologram parent) {
        this.parent = parent;
        this.playerVisibility = new HashMap<>();
        this.playerPages = new HashMap<>();
        this.currentViewers = new HashSet<>();
        this.visibleByDefault = true;
    }

    @Override
    public void destroy() {
        // Reset the visibility so that it doesn't show up for any players
        this.visibleByDefault = false;
        this.playerVisibility.clear();

        // Hide the hologram for all players that are currently viewing it
        getViewerPlayers().forEach((player) -> updateVisibility(player, false));

        // Clear the cache
        this.playerPages.clear();
        this.currentViewers.clear();
    }

    @Override
    public void setVisibility(@NotNull Player player, @NotNull Visibility visibility) {
        this.playerVisibility.put(player.getUniqueId(), visibility);
        updateVisibility(player);
    }

    @Override
    public void updateVisibility(@NotNull Player player) {
        // Check if the player is allowed to see this hologram.
        if (!canSee(player)) {
            // Hide the hologram for the player if they are not allowed
            // to see it, and they are currently viewing it.
            if (isViewing(player)) {
                updateVisibility(player, false);
            }
            return;
        }

        // Get the player's profile.
        Profile profile = DecentHolograms.getInstance().getProfileRegistry().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        // Check if the player is in the view distance.
        boolean inViewDistance = MathUtil.inDistance(
                parent.getPositionManager().getActualLocation(),
                player.getLocation(),
                parent.getSettings().getViewDistance()
        );

        // Check if the player satisfies the view conditions.
        boolean meetsConditions = !isVisibleByDefault() || parent.getViewConditions().check(profile);

        if (isViewing(player) && (!inViewDistance || !meetsConditions)) {
            // If the player is currently viewing the hologram but is no
            // longer in the view distance or does not satisfy the view
            // conditions, hide the hologram.
            updateVisibility(player, false);
        } else if (!isViewing(player) && inViewDistance && meetsConditions) {
            // If the player is not currently viewing the hologram but is in
            // the view distance and satisfies the view conditions, show the
            // hologram.
            updateVisibility(player, true);
        }
    }

    @Override
    public void updateVisibility(@NotNull Player player, boolean visible) {
        Optional<HologramPage> pageOpt = getPageObject(player);
        pageOpt.ifPresent((page) -> {
            if (visible) {
                page.display(player);
                currentViewers.add(player.getUniqueId());
            } else {
                page.hide(player);
                currentViewers.remove(player.getUniqueId());
            }
        });
    }

    @Override
    public void updateVisibility() {
        getAllowedPlayers().forEach(this::updateVisibility);
    }

    @Override
    public void updateContents(@NotNull Player player) {
        Optional<HologramPage> pageOpt = getPageObject(player);
        pageOpt.ifPresent((page) -> page.update(player));
    }

    @Override
    public void updateContents() {
        getViewerPlayers().forEach(this::updateContents);
    }

    @Override
    public void setPage(@NotNull Player player, int page) {
        // Get the old page.
        Optional<HologramPage> oldPageOpt = getPageObject(player);

        // Update the page index in the map.
        playerPages.put(player.getUniqueId(), page);

        // Hide the old page.
        if (isViewing(player) && oldPageOpt.isPresent()) {
            oldPageOpt.get().hide(player);
        }

        // Update the visibility for the player.
        updateVisibility(player);
    }

    @NotNull
    @Override
    public Hologram getParent() {
        return parent;
    }

    @Override
    public boolean isVisibleByDefault() {
        return visibleByDefault;
    }

    @Override
    public void setVisibleByDefault(boolean visible) {
        this.visibleByDefault = visible;
    }

    @NotNull
    @Override
    public Set<UUID> getViewers() {
        return ImmutableSet.copyOf(currentViewers);
    }

    @NotNull
    @Override
    public Map<UUID, Integer> getPlayerPages() {
        return ImmutableMap.copyOf(playerPages);
    }

    @NotNull
    @Override
    public Map<UUID, Visibility> getPlayerVisibility() {
        return ImmutableMap.copyOf(playerVisibility);
    }

    /**
     * Get the page object for the given player by the page index.
     *
     * @param player The player.
     * @return The page as an optional.
     */
    @NotNull
    private Optional<HologramPage> getPageObject(@NotNull Player player) {
        int pageIndex = getPage(player);
        HologramPage page = parent.getPage(pageIndex);
        return Optional.ofNullable(page);
    }

}

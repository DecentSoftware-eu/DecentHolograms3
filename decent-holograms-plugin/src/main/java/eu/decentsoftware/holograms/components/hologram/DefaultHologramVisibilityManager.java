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

package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.DecentHologramsPlugin;
import eu.decentsoftware.holograms.api.component.hologram.HologramVisibilityManager;
import eu.decentsoftware.holograms.api.component.page.HologramPage;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultHologramVisibilityManager implements HologramVisibilityManager {

    private final @NotNull DefaultHologram parent;
    private final @NotNull Set<String> players;
    private final @NotNull Set<String> viewers;
    private final @NotNull Map<String, Integer> playerPages;
    private boolean visibleByDefault;

    /**
     * Creates a new instance of {@link DefaultHologramVisibilityManager} with the given parent.
     *
     * @param parent The parent hologram.
     */
    public DefaultHologramVisibilityManager(@NotNull DefaultHologram parent) {
        this.parent = parent;
        this.players = new HashSet<>();
        this.viewers = new HashSet<>();
        this.playerPages = new ConcurrentHashMap<>();
        this.visibleByDefault = true;
    }

    @Override
    public void destroy() {
        // Hide the hologram for all players
        players.clear();
        for (String player : getViewers()) {
            Player playerObj = Bukkit.getPlayer(player);
            if (playerObj != null) {
                updateVisibility(playerObj, false);
            }
        }
        // Clear the visibility cache
        viewers.clear();
        playerPages.clear();
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
        Profile profile = DecentHologramsPlugin.getInstance().getProfileRegistry().getProfile(player.getName());
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
                viewers.add(player.getName());
            } else {
                page.hide(player);
                viewers.remove(player.getName());
            }
        });
    }

    @Override
    public void updateVisibility() {
        for (String player : getPlayers()) {
            Player playerObj = Bukkit.getPlayer(player);
            if (playerObj != null) {
                updateVisibility(playerObj);
            }
        }
    }

    @Override
    public void updateContents(@NotNull Player player) {
        Optional<HologramPage> pageOpt = getPageObject(player);
        pageOpt.ifPresent((page) -> page.update(player));
    }

    @Override
    public void updateContents() {
        for (String player : getPlayers()) {
            Player playerObj = Bukkit.getPlayer(player);
            if (playerObj != null) {
                updateContents(playerObj);
            }
        }
    }

    @Override
    public void setPage(@NotNull Player player, int page) {
        // Get the old page.
        Optional<HologramPage> oldPageOpt = getPageObject(player);

        // Update the page index in the map.
        playerPages.put(player.getName(), page);

        // Hide the old page.
        if (isViewing(player) && oldPageOpt.isPresent()) {
            oldPageOpt.get().hide(player);
        }

        // Update the visibility for the player.
        updateVisibility(player);
    }

    @Override
    public void show(@NotNull Player player, int page) {
        players.add(player.getName());
        playerPages.put(player.getName(), page);
        updateVisibility(player);
    }

    @Override
    public void hide(@NotNull Player player) {
        players.remove(player.getName());
        playerPages.remove(player.getName());
        updateVisibility(player);
    }

    @NotNull
    @Override
    public DefaultHologram getParent() {
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
    public Set<String> getPlayers() {
        // If the hologram is visible by default, return all online players.
        if (isVisibleByDefault()) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toSet());
        }
        // Otherwise, return the manually added players.
        return players;
    }

    @NotNull
    @Override
    public Set<String> getViewers() {
        return viewers;
    }

    @NotNull
    @Override
    public Map<String, Integer> getPlayerPages() {
        return playerPages;
    }

    /**
     * Get the page object for the given player by the page index.
     *
     * @param player The player.
     * @return The page as an optional.
     */
    private Optional<HologramPage> getPageObject(@NotNull Player player) {
        int pageIndex = getPage(player);
        HologramPage page = parent.getPage(pageIndex);
        return Optional.ofNullable(page);
    }

}

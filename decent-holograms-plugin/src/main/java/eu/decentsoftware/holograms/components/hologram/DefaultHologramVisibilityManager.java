package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramVisibilityManager;
import eu.decentsoftware.holograms.api.component.page.Page;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultHologramVisibilityManager implements HologramVisibilityManager {

    private final @NotNull Hologram parent;
    private final @NotNull Set<String> players;
    private final @NotNull Set<String> viewers;
    private final @NotNull Map<String, Integer> playerPages;
    private boolean visibleByDefault;

    /**
     * Creates a new instance of {@link DefaultHologramVisibilityManager} with the given parent.
     *
     * @param parent The parent hologram.
     */
    public DefaultHologramVisibilityManager(@NotNull Hologram parent) {
        this.parent = parent;
        this.players = new HashSet<>();
        this.viewers = new HashSet<>();
        this.playerPages = new HashMap<>();
        this.visibleByDefault = true;
    }

    @Override
    public void updateVisibility(@NotNull Player player, boolean visible) {
        Optional<Page> pageOpt = getPageObject(player);
        pageOpt.ifPresent((page) -> {
            // Update the visibility of the page.
            if (visible) {
                page.display(player);
            } else {
                page.hide(player);
            }
            // Update the viewers set.
            HologramVisibilityManager.super.updateVisibility(player, visible);
        });
    }

    @Override
    public void updateContents(@NotNull Player player) {
        Optional<Page> pageOpt = getPageObject(player);
        pageOpt.ifPresent((page) -> page.update(player));
    }

    @Override
    public void setPage(@NotNull Player player, int page) {
        // Get the old page.
        Optional<Page> oldPageOpt = getPageObject(player);

        // Update the page index in the map.
        HologramVisibilityManager.super.setPage(player, page);

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
    private Optional<Page> getPageObject(@NotNull Player player) {
        int pageIndex = getPage(player);
        Page page = getParent().getPageHolder().getPage(pageIndex);
        return Optional.ofNullable(page);
    }

}

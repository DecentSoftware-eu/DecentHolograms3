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

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.HologramLineSettings;
import eu.decentsoftware.holograms.api.hologram.ClickType;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.core.line.renderer.HologramLineRenderer;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.ticker.Ticked;
import eu.decentsoftware.holograms.utils.math.MathUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public abstract class CoreHologram<PAGE extends CoreHologramPage<?>> extends CoreHologramComponent implements Ticked {

    protected final DecentHolograms plugin;
    protected final List<PAGE> pages = new ArrayList<>();
    protected CorePositionManager positionManager;
    protected CoreHologramSettings settings;
    protected CoreHologramVisibilityManager visibilityManager;
    protected CoreHologramEntityIDManager entityIDManager;
    private final AtomicLong lastVisibilityUpdate = new AtomicLong(0);
    private final AtomicLong lastContentUpdate = new AtomicLong(0);

    protected CoreHologram(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
    }

    public CoreHologram(@NonNull DecentHolograms plugin, @NonNull DecentLocation location) {
        this.plugin = plugin;
        this.visibilityManager = new CoreHologramVisibilityManager(this);
        this.positionManager = new CorePositionManager(location);
        this.settings = new CoreHologramSettings(true);
        this.entityIDManager = new CoreHologramEntityIDManager(plugin.getNMSManager().getAdapter());

        startTicking();
    }

    public void destroy() {
        stopTicking();

        this.visibilityManager.destroy();

        clearPages();

        super.destroy();
    }

    @Override
    public void tick() {
        if (!this.settings.isEnabled()) {
            return;
        }

        if (this.positionManager.isLocationBound()
                || this.settings.isRotateHorizontal()
                || this.settings.isRotateVertical()
        ) {
            recalculate();
        }

        final long currentTime = System.currentTimeMillis();
        if (500L < (currentTime - this.lastVisibilityUpdate.get())) {
            this.visibilityManager.updateVisibility();
            this.lastVisibilityUpdate.set(currentTime);
        }

        if (this.settings.getUpdateInterval() * 50L < (currentTime - this.lastContentUpdate.get())) {
            this.visibilityManager.updateContents();
            this.lastContentUpdate.set(currentTime);
        }
    }

    /**
     * Called when a player clicks on the hologram.
     *
     * @param player    The player that clicked.
     * @param clickType The type of click.
     * @param page      The page that was clicked.
     * @param line      The line that was clicked.
     */
    public abstract void onClick(
            @NonNull Player player,
            @NonNull ClickType clickType,
            @NonNull CoreHologramPage<?> page,
            @NonNull CoreHologramLine line
    );

    /**
     * Switches the page of the hologram for the specified player.
     *
     * @param player The player to switch the page for.
     * @param index  The index of the page to switch to.
     * @throws IllegalArgumentException If the index is out of bounds.
     * @throws IllegalStateException    If the hologram is destroyed.
     */
    public void switchPage(@NonNull Player player, int index) {
        checkDestroyed();

        if (index < 0 || index >= this.pages.size()) {
            throw new IllegalArgumentException("Invalid page index: " + index);
        }

        this.visibilityManager.setPage(player, index);
    }

    /**
     * For each player, this method recalculates the positions of the lines
     * that are visible to the player. Making sure they are aligned correctly. This
     * takes into account the 'rotate' settings of the hologram.
     * <p>
     * This method will update the global locations of the lines if necessary.
     */
    public void recalculate() {
        for (Player player : this.visibilityManager.getViewersAsPlayers()) {
            recalculate(player);
        }
    }

    /**
     * For the specified player, this method recalculates the positions of the lines
     * that are visible to the player. Making sure they are aligned correctly. This
     * takes into account the 'rotate' settings of the hologram.
     * <p>
     * This method will update the global locations of the lines if necessary.
     *
     * @param player The player to recalculate the positions for.
     */
    public void recalculate(@NonNull Player player) {
        checkDestroyed();

        CoreHologramPage<?> page = this.visibilityManager.getPage(player);
        if (page == null) {
            return;
        }

        Location hologramLocation = this.positionManager.getActualBukkitLocation();
        double totalHeight = page.getHeight();

        if (this.settings.isDownOrigin()) {
            hologramLocation.add(0, totalHeight, 0);
        }

        // TODO: hologram must be text-only for vertical rotation
        if (!this.settings.isRotateHorizontal() && !this.settings.isRotateVertical()) {
            page.updateLocation(player);
            return;
        }

        complexRecalculate(player, page, hologramLocation, totalHeight);
    }

    private void complexRecalculate(
            @NonNull Player player,
            @NonNull CoreHologramPage<?> page,
            @NonNull Location hologramLocation,
            double totalHeight
    ) {
        // TODO: don't send packets if the difference is insignificant

        Location playerEyeLocation = player.getEyeLocation();
        Vector playerLookDirection = playerEyeLocation.getDirection().clone().normalize();

        // Calculate the required vectors.
        Vector horizontalPerpendicular = playerLookDirection.clone().crossProduct(MathUtil.UP_VECTOR).normalize();
        Vector verticalPerpendicular = horizontalPerpendicular.clone().crossProduct(playerLookDirection).normalize();

        // Calculate the pivot point. (The center of the hologram)
        Location pivot = hologramLocation.clone().subtract(0, totalHeight / 2, 0);

        if (this.settings.isInteractive()) {
            updateClickableEntityAndWatchedLine(
                    player,
                    page,
                    horizontalPerpendicular,
                    verticalPerpendicular,
                    pivot.toVector(),
                    totalHeight
            );
        } else {
            Profile profile = this.plugin.getProfileRegistry().getProfile(player.getUniqueId());
            profile.getContext().destroyClickableEntity(player);
            profile.getContext().setWatchedLine(null);
        }

        double height = 0.0d;
        for (CoreHologramLine line : page.getLines()) {
            HologramLineRenderer renderer = line.getRenderer();
            HologramLineSettings settings = line.getSettings();
            double totalOffsetY = line.getTypeYOffset() + settings.getOffsetY();

            Location location;
            if (this.settings.isRotateVertical()) {
                // If we rotate vertically, we put the lines along the relative vertical vector.
                double angle = Math.toRadians(playerEyeLocation.getPitch());
                double totalOffsetYAdjusted = totalOffsetY * Math.cos(angle);
                Vector vector = verticalPerpendicular.clone().multiply(-((height - totalOffsetYAdjusted) - totalHeight / 2));
                location = pivot.clone().add(vector);
            } else {
                // If we don't rotate vertically, we put the lines above each other.
                location = hologramLocation.clone().subtract(0, height - totalOffsetY, 0);
            }

            height += settings.getHeight();

            double totalOffsetX = settings.getOffsetX();
            double totalOffsetZ = settings.getOffsetZ();
            if (this.settings.isRotateHorizontal()) {
                if (totalOffsetX != 0) {
                    location.add(horizontalPerpendicular.clone().multiply(totalOffsetX));
                }
                if (totalOffsetZ != 0) {
                    location.add(playerLookDirection.clone().multiply(totalOffsetZ));
                }
            } else {
                location.add(totalOffsetX, 0, totalOffsetZ);
            }

            if (renderer != null) {
                renderer.updateLocation(player);
            }
        }
    }

    private void updateClickableEntityAndWatchedLine(
            @NonNull Player player,
            @NonNull CoreHologramPage<?> page,
            @NonNull Vector horizontalPerpendicular,
            @NonNull Vector verticalPerpendicular,
            @NonNull Vector pivot,
            double totalHeight
    ) {
        Location playerEyeLocation = player.getEyeLocation();
        Vector playerLookDirection = playerEyeLocation.getDirection().clone().normalize();
        Vector pivotToTop = verticalPerpendicular.clone().multiply(totalHeight / 2);
        Vector pivotToRight = horizontalPerpendicular.clone().multiply(page.getWidth(player) / 2);
        Vector intersection = MathUtil.getIntersectionBetweenPlaneAndVector(
                playerEyeLocation.toVector(),
                playerLookDirection,
                pivot,
                pivotToTop,
                pivotToRight,
                5.0d
        );

        CoreHologramLine intersectingLine = null;
        if (intersection != null) {
            Vector intersectionPointOnPlane = MathUtil.getPointOnPlane(
                    intersection,
                    pivotToTop,
                    pivotToRight,
                    pivot
            );

            double y = totalHeight - (intersectionPointOnPlane.getY() + totalHeight / 2);
            double height = 0.0d;

            for (CoreHologramLine line : page.getLines()) {
                double lineHeight = line.getSettings().getHeight();
                if (y >= height && y <= height + lineHeight) {
                    intersectingLine = line;
                    break;
                }
                height += lineHeight;
            }
        }

        Profile profile = this.plugin.getProfileRegistry().getProfile(player.getUniqueId());
        CoreHologramLine currentWatchedLine = profile.getContext().getWatchedLine();
        if (intersectingLine != null && (currentWatchedLine == null || page.equals(currentWatchedLine.getParent()))) {
            profile.getContext().setWatchedLine(intersectingLine);

            Location clickableEntityLocation = new Location(
                    playerEyeLocation.getWorld(),
                    intersection.getX(),
                    intersection.getY() - 0.25d,
                    intersection.getZ(),
                    playerEyeLocation.getYaw(),
                    playerEyeLocation.getPitch()
            );
            clickableEntityLocation.add(playerLookDirection.clone().normalize().multiply(-0.5d));

            profile.getContext().moveOrCreateClickableEntity(player, clickableEntityLocation);
        } else if (currentWatchedLine != null && page.equals(currentWatchedLine.getParent())) {
            profile.getContext().setWatchedLine(null);
            profile.getContext().destroyClickableEntity(player);
        }
    }

    @NonNull
    protected abstract PAGE createPage();

    @NonNull
    public PAGE appendPage() {
        checkDestroyed();

        PAGE page = createPage();
        this.pages.add(page);
        return page;
    }

    @NonNull
    public PAGE insertPage(int index) {
        checkDestroyed();

        PAGE page = createPage();
        this.pages.add(index, page);

        shiftPlayerPages(index, 1);

        return page;
    }

    @Nullable
    public PAGE getPage(int index) {
        checkDestroyed();

        return this.pages.get(index);
    }

    public void removePage(int index) {
        checkDestroyed();

        this.pages.remove(index);

        shiftPlayerPages(index, -1);
    }

    public void clearPages() {
        checkDestroyed();

        for (PAGE page : this.pages) {
            page.destroy();
        }
        this.pages.clear();

        this.visibilityManager.resetPlayerPages();
    }

    /**
     * Shift the player page indexes in visibility manager by the given amount at the given index.
     *
     * @param index The index to start shifting from.
     * @param shift The amount to shift by.
     */
    private void shiftPlayerPages(int index, int shift) {
        for (Map.Entry<UUID, Integer> entry : this.visibilityManager.getPlayerPages().entrySet()) {
            if (entry.getValue() >= index) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null) {
                    switchPage(player, entry.getValue() + shift);
                }
            }
        }
    }

    @NonNull
    public List<PAGE> getPages() {
        return ImmutableList.copyOf(this.pages);
    }

    @NonNull
    public CoreHologramSettings getSettings() {
        return this.settings;
    }

    @NonNull
    public CorePositionManager getPositionManager() {
        return this.positionManager;
    }

    @NonNull
    public CoreHologramVisibilityManager getVisibilityManager() {
        return this.visibilityManager;
    }

    @NonNull
    public CoreHologramEntityIDManager getEntityIDManager() {
        return this.entityIDManager;
    }

}

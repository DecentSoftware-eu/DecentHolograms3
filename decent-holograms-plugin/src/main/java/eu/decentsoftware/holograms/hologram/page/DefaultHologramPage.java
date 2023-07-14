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

package eu.decentsoftware.holograms.hologram.page;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.component.ClickHandler;
import eu.decentsoftware.holograms.api.hologram.component.PositionManager;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineRenderer;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineSettings;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineType;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;
import eu.decentsoftware.holograms.hologram.line.DefaultHologramLine;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.utils.math.MathUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefaultHologramPage implements HologramPage {

    private final @NotNull Hologram parent;
    private final @NotNull List<HologramLine> lines;
    private final @NotNull ClickConditionHolder clickConditions;
    private final @NotNull ClickActionHolder clickActions;
    private ClickHandler clickHandler;

    public DefaultHologramPage(@NotNull Hologram parent) {
        this(parent, new ClickConditionHolder(), new ClickActionHolder());
    }

    @Contract(pure = true)
    public DefaultHologramPage(@NotNull Hologram parent, @NotNull ClickConditionHolder clickConditions, @NotNull ClickActionHolder clickActions) {
        this.parent = parent;
        this.lines = new ArrayList<>();
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
        this.setClickHandler((player, clickType) -> {
            if (!getClickActions().isEmpty(clickType) && getClickConditions().check(clickType, player)) {
                getClickActions().execute(clickType, player);
                return true;
            }

            return false;
        });
    }

    @Override
    public void display(@NotNull Player player) {
        forEachLineRendererSafe(renderer -> renderer.display(player));
    }

    @Override
    public void hide(@NotNull Player player) {
        forEachLineRendererSafe(renderer -> renderer.hide(player));
    }

    @Override
    public void update(@NotNull Player player) {
        forEachLineRendererSafe(renderer -> renderer.update(player));
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        forEachLineRendererSafe(renderer -> renderer.teleport(player, location));
    }

    @Override
    public void recalculate() {
        int pageIndex = parent.getIndex(this);
        Set<Player> viewers = parent.getVisibilityManager().getViewerPlayers(pageIndex);

        final boolean horizontal = parent.getSettings().isRotateHorizontal();
        final boolean vertical = parent.getSettings().isRotateVertical();
        final boolean heads = parent.getSettings().isRotateHeads();
        final boolean isTextOnly = lines.stream().noneMatch(line -> line.getType() != HologramLineType.TEXT);

        for (Player viewer : viewers) {
            recalculate(viewer, horizontal, vertical && isTextOnly, heads);
        }
    }

    @Override
    public void recalculate(@NotNull Player player, boolean horizontal, boolean vertical, boolean heads) {
        final Location hologramLocation = parent.getPositionManager().getActualLocation();
        final boolean downOrigin = parent.getSettings().isDownOrigin();
        final double totalHeight = getHeight();

        // If the hologram location is originating from the bottom,
        // we need to move the start location up by the total height
        // of the hologram.
        if (downOrigin) {
            hologramLocation.add(0, totalHeight, 0);
        }

        // If we don't rotate, we just align the lines properly.
        if (!horizontal && !vertical) {
            simpleRecalculate(player, hologramLocation);
            return;
        }

        Location playerEyeLocation = player.getEyeLocation();
        Vector playerLookDirection = playerEyeLocation.getDirection().clone().normalize();

        // Calculate the required vectors.
        Vector horizontalPerpendicular = playerLookDirection.clone().crossProduct(MathUtil.UP_VECTOR).normalize();
        Vector verticalPerpendicular = horizontalPerpendicular.clone().crossProduct(playerLookDirection).normalize();

        // Calculate the pivot point. (The center of the hologram)
        Location pivot = hologramLocation.clone().subtract(0, totalHeight / 2, 0);

        if (parent.getSettings().isInteractive()) {
            updateClickableEntityAndWatchedLine(
                    player,
                    horizontalPerpendicular,
                    verticalPerpendicular,
                    pivot.toVector(),
                    totalHeight
            );
        }
        // TODO: Remove clickable entity if not interactive anymore?

        // Calculate new location for each line.
        double height = 0.0d;
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            PositionManager positionManager = line.getPositionManager();
            HologramLineSettings settings = line.getSettings();
            Vector offsets = positionManager.getOffsets();
            double totalOffsetY = offsets.getY() + settings.getOffsetY();

            // Calculate the new location.
            Location location;
            if (vertical) {
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

            // Add the line offsets.
            double totalOffsetX = offsets.getX() + settings.getOffsetX();
            double totalOffsetZ = offsets.getZ() + settings.getOffsetZ();
            if (horizontal) {
                if (totalOffsetX != 0) {
                    location.add(horizontalPerpendicular.clone().multiply(totalOffsetX));
                }
                if (totalOffsetZ != 0) {
                    location.add(playerLookDirection.clone().multiply(totalOffsetZ));
                }
            } else {
                location.add(totalOffsetX, 0, totalOffsetZ);
            }

            positionManager.setLocation(location);

            // Update the line location for the current viewer.
            if (renderer != null) {
                renderer.teleport(player, location);
            }
        }
    }

    private void simpleRecalculate(@NotNull Player player, @NotNull Location hologramLocation) {
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            PositionManager positionManager = line.getPositionManager();
            positionManager.setLocation(hologramLocation.clone());
            if (renderer != null) {
                renderer.teleport(player, positionManager.getActualLocation());
            }
            hologramLocation.subtract(0, line.getSettings().getHeight(), 0);
        }
    }

    private void updateClickableEntityAndWatchedLine(
            @NotNull Player player,
            @NotNull Vector horizontalPerpendicular,
            @NotNull Vector verticalPerpendicular,
            @NotNull Vector pivot,
            double totalHeight
    ) {
        Location playerEyeLocation = player.getEyeLocation();
        Vector playerLookDirection = playerEyeLocation.getDirection().clone().normalize();

        // Calculate the intersection point of the player's look direction
        // and the hologram plane.
        Vector pivotToTop = verticalPerpendicular.clone().multiply(totalHeight / 2);
        Vector pivotToRight = horizontalPerpendicular.clone().multiply(2.5);
        Vector intersection = MathUtil.getIntersectionBetweenPlaneAndVector(
                playerEyeLocation.toVector(),
                playerLookDirection,
                pivot,
                pivotToTop,
                pivotToRight,
                5.0d
        );

        // Find the line we are intersecting with.
        HologramLine intersectingLine = null;
        if (intersection != null) {
            Vector intersectionPointOnPlane = MathUtil.getPointOnPlane(
                    intersection,
                    pivotToTop,
                    pivotToRight,
                    pivot
            );

            double y = totalHeight - (intersectionPointOnPlane.getY() + totalHeight / 2);
            double height = 0.0d;

            for (HologramLine line : lines) {
                double lineHeight = line.getSettings().getHeight();
                if (y >= height && y <= height + lineHeight) {
                    intersectingLine = line;
                    break;
                }
                height += lineHeight;
            }
        }

        // Update player context (watched line).
        Profile profile = DecentHolograms.getInstance().getProfileRegistry().getProfile(player.getUniqueId());
        HologramLine currentWatchedLine = profile.getContext().getWatchedLine();
        if (intersectingLine != null && (currentWatchedLine == null || this.equals(currentWatchedLine.getParent()))) {
            profile.getContext().setWatchedLine(intersectingLine);

            // Update clickable entity location.
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
        } else if (currentWatchedLine != null && this.equals(currentWatchedLine.getParent())) {
            profile.getContext().setWatchedLine(null);
            profile.getContext().destroyClickableEntity(player);
        }
    }

    @NotNull
    @Override
    public Location getNextLineLocation() {
        return parent.getPositionManager().getActualLocation().subtract(0, getHeight(), 0);
    }

    @NotNull
    @Override
    public Hologram getParent() {
        return parent;
    }

    @NotNull
    public ClickConditionHolder getClickConditions() {
        return clickConditions;
    }

    @NotNull
    public ClickActionHolder getClickActions() {
        return clickActions;
    }

    public double getHeight() {
        return lines.stream()
                .mapToDouble(l -> l.getSettings().getHeight())
                .sum();
    }

    @Override
    public HologramLine getLine(int index) {
        return lines.get(index);
    }

    @Override
    public int getIndex(@NotNull HologramLine line) {
        return lines.contains(line) ? lines.indexOf(line) : -1;
    }

    @Override
    public HologramLine removeLine(int index) {
        HologramLine line = lines.remove(index);

        // Hide the line to all viewers
        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::hide);

        recalculate();
        return line;
    }

    @NotNull
    @Override
    public HologramPage addLine(@NotNull String line) {
        return addLine(new DefaultHologramLine(this, getNextLineLocation(), line));
    }

    @NotNull
    @Override
    public HologramPage addLine(@NotNull HologramLine line) {
        lines.add(line);

        // Show the line to all viewers
        // TODO: check view conditions
        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::display);

        recalculate();
        return this;
    }

    @NotNull
    @Override
    public HologramPage addLine(int index, @NotNull String line) {
        return addLine(index, new DefaultHologramLine(this, getNextLineLocation(), line));
    }

    @NotNull
    @Override
    public HologramPage addLine(int index, @NotNull HologramLine line) {
        lines.add(index, line);

        // Show the line to all viewers
        // TODO: check view conditions
        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::display);

        recalculate();
        return this;
    }

    @NotNull
    @Override
    public HologramPage setLine(int index, @NotNull HologramLine line) {
        HologramLine previousLine = lines.remove(index);

        // Hide the previous line to all viewers
        forEachViewerUseLineRendererSafe(previousLine, HologramLineRenderer::hide);

        addLine(index, line);
        return this;
    }

    @NotNull
    @Override
    public HologramPage clearLines() {
        // Hide all lines from all viewers
        forEachLineRendererAndViewerSafe(HologramLineRenderer::hide);

        lines.clear();
        return this;
    }

    @NotNull
    @Override
    public HologramPage setLinesFromStrings(@NotNull List<String> lines) {
        return setLines(lines.stream()
                .map(s -> new DefaultHologramLine(this, getNextLineLocation(), s))
                .collect(Collectors.toList()));
    }

    @NotNull
    @Override
    public HologramPage setLines(@NotNull List<HologramLine> lines) {
        // Hide all lines from all viewers
        forEachLineRendererAndViewerSafe(HologramLineRenderer::hide);

        this.lines.clear();
        this.lines.addAll(lines);

        // Show all lines to all viewers
        forEachLineRendererAndViewerSafe(HologramLineRenderer::display);
        return this;
    }

    @NotNull
    @Override
    public List<HologramLine> getLines() {
        return ImmutableList.copyOf(lines);
    }

    @Nullable
    @Override
    public ClickHandler getClickHandler() {
        return clickHandler;
    }

    @Override
    public void setClickHandler(@Nullable ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    private void forEachLineRendererSafe(@NotNull Consumer<HologramLineRenderer> consumer) {
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                consumer.accept(renderer);
            }
        }
    }

    private void forEachLineRendererAndViewerSafe(@NotNull BiConsumer<HologramLineRenderer, Player> consumer) {
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
                    consumer.accept(renderer, viewerPlayer);
                }
            }
        }
    }

    private void forEachViewerUseLineRendererSafe(@NotNull HologramLine line, @NotNull BiConsumer<HologramLineRenderer, Player> consumer) {
        HologramLineRenderer renderer = line.getRenderer();
        if (renderer != null) {
            for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
                consumer.accept(renderer, viewerPlayer);
            }
        }
    }

}

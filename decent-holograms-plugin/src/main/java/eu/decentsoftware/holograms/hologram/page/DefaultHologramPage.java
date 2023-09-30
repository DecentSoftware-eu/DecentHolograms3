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
import eu.decentsoftware.holograms.hologram.DefaultHologramEntityIDProvider;
import eu.decentsoftware.holograms.hologram.line.DefaultHologramLine;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.utils.math.MathUtil;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefaultHologramPage implements HologramPage {

    private final Hologram parent;
    private final DefaultHologramEntityIDProvider entityIDProvider;
    private final List<HologramLine> lines;
    private final ClickConditionHolder clickConditions;
    private final ClickActionHolder clickActions;
    private ClickHandler clickHandler;

    public DefaultHologramPage(
            @NonNull Hologram parent,
            @NonNull DefaultHologramEntityIDProvider entityIDProvider
    ) {
        this(parent, entityIDProvider, new ClickConditionHolder(), new ClickActionHolder());
    }

    @Contract(pure = true)
    public DefaultHologramPage(
            @NonNull Hologram parent,
            @NonNull DefaultHologramEntityIDProvider entityIDProvider,
            @NonNull ClickConditionHolder clickConditions,
            @NonNull ClickActionHolder clickActions
    ) {
        this.parent = parent;
        this.entityIDProvider = entityIDProvider;
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
    public void display(@NonNull Player player) {
        forEachLineRendererSafe(renderer -> renderer.display(player));
    }

    @Override
    public void hide(@NonNull Player player) {
        forEachLineRendererSafe(renderer -> renderer.hide(player));
    }

    @Override
    public void update(@NonNull Player player) {
        forEachLineRendererSafe(renderer -> renderer.update(player));
    }

    @Override
    public void teleport(@NonNull Player player, @NonNull Location location) {
        forEachLineRendererSafe(renderer -> renderer.teleport(player, location));
    }

    @Override
    public void recalculate() {
        int pageIndex = parent.getIndex(this);
        Set<Player> viewers = parent.getVisibilityManager().getViewersAsPlayers(pageIndex);

        final boolean horizontal = parent.getSettings().isRotateHorizontal();
        final boolean vertical = parent.getSettings().isRotateVertical();
        final boolean heads = parent.getSettings().isRotateHeads();
        final boolean isTextOnly = lines.stream().noneMatch(line -> line.getType() != HologramLineType.TEXT);

        for (Player viewer : viewers) {
            recalculate(viewer, horizontal, vertical && isTextOnly, heads);
        }
    }

    @Override
    public void recalculate(@NonNull Player player, boolean horizontal, boolean vertical, boolean heads) {
        // TODO: don't send packets if the difference is insignificant

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
        } else {
            Profile profile = DecentHolograms.getInstance().getProfileRegistry().getProfile(player.getUniqueId());
            profile.getContext().destroyClickableEntity(player);
            profile.getContext().setWatchedLine(null);
        }

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

    private void simpleRecalculate(@NonNull Player player, @NonNull Location hologramLocation) {
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
            @NonNull Player player,
            @NonNull Vector horizontalPerpendicular,
            @NonNull Vector verticalPerpendicular,
            @NonNull Vector pivot,
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

    @NonNull
    @Override
    public Location getNextLineLocation() {
        return parent.getPositionManager().getActualLocation().subtract(0, getHeight(), 0);
    }

    @NonNull
    @Override
    public Hologram getParent() {
        return parent;
    }

    @NonNull
    public ClickConditionHolder getClickConditions() {
        return clickConditions;
    }

    @NonNull
    public ClickActionHolder getClickActions() {
        return clickActions;
    }

    @Override
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
    public int getIndex(@NonNull HologramLine line) {
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

    @NonNull
    @Override
    public HologramPage addLine(@NonNull String line) {
        return addLine(new DefaultHologramLine(this, getNextLineLocation(), line));
    }

    @NonNull
    @Override
    public HologramPage addLine(@NonNull HologramLine line) {
        lines.add(line);

        // Show the line to all viewers
        // TODO: check view conditions
        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::display);

        recalculate();
        return this;
    }

    @NonNull
    @Override
    public HologramPage addLine(int index, @NonNull String line) {
        return addLine(index, new DefaultHologramLine(this, getNextLineLocation(), line));
    }

    @NonNull
    @Override
    public HologramPage addLine(int index, @NonNull HologramLine line) {
        lines.add(index, line);

        // Show the line to all viewers
        // TODO: check view conditions
        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::display);

        recalculate();
        return this;
    }

    @NonNull
    @Override
    public HologramPage setLine(int index, @NonNull HologramLine line) {
        HologramLine previousLine = lines.remove(index);

        // Hide the previous line to all viewers
        forEachViewerUseLineRendererSafe(previousLine, HologramLineRenderer::hide);

        addLine(index, line);
        return this;
    }

    @NonNull
    @Override
    public HologramPage clearLines() {
        // Hide all lines from all viewers
        forEachLineRendererAndViewerSafe(HologramLineRenderer::hide);

        lines.clear();
        return this;
    }

    @NonNull
    @Override
    public HologramPage setLinesFromStrings(@NonNull List<String> lines) {
        return setLines(lines.stream()
                .map(s -> new DefaultHologramLine(this, getNextLineLocation(), s))
                .collect(Collectors.toList()));
    }

    @NonNull
    @Override
    public HologramPage setLines(@NonNull List<HologramLine> lines) {
        // Hide all lines from all viewers
        forEachLineRendererAndViewerSafe(HologramLineRenderer::hide);

        this.lines.clear();
        this.lines.addAll(lines);

        // Show all lines to all viewers
        forEachLineRendererAndViewerSafe(HologramLineRenderer::display);
        return this;
    }

    @NonNull
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

    private void forEachLineRendererSafe(@NonNull Consumer<HologramLineRenderer> consumer) {
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                consumer.accept(renderer);
            }
        }
    }

    private void forEachLineRendererAndViewerSafe(@NonNull BiConsumer<HologramLineRenderer, Player> consumer) {
        for (HologramLine line : lines) {
            forEachViewerUseLineRendererSafe(line, consumer);
        }
    }

    private void forEachViewerUseLineRendererSafe(@NonNull HologramLine line, @NonNull BiConsumer<HologramLineRenderer, Player> consumer) {
        HologramLineRenderer renderer = line.getRenderer();
        if (renderer != null) {
            for (Player viewerPlayer : parent.getVisibilityManager().getViewersAsPlayers()) {
                consumer.accept(renderer, viewerPlayer);
            }
        }
    }

}

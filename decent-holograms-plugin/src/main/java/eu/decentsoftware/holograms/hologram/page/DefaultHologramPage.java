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
import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.component.PositionManager;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineRenderer;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineSettings;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineType;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.hologram.line.DefaultHologramLine;
import eu.decentsoftware.holograms.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefaultHologramPage implements HologramPage {

    private final @NotNull Hologram parent;
    private final @NotNull List<HologramLine> lines;
    private final @NotNull ConditionHolder clickConditions;
    private final @NotNull ActionHolder clickActions;

    public DefaultHologramPage(@NotNull Hologram parent) {
        this(parent, new ConditionHolder(), new ActionHolder());
    }

    @Contract(pure = true)
    public DefaultHologramPage(@NotNull Hologram parent, @NotNull ConditionHolder clickConditions, @NotNull ActionHolder clickActions) {
        this.parent = parent;
        this.lines = new ArrayList<>();
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
    }

    @Override
    public void display(@NotNull Player player) {
        forEachLineRendererSafe((renderer) -> renderer.display(player));
    }

    @Override
    public void hide(@NotNull Player player) {
        forEachLineRendererSafe((renderer) -> renderer.hide(player));
    }

    @Override
    public void update(@NotNull Player player) {
        forEachLineRendererSafe((renderer) -> renderer.update(player));
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        forEachLineRendererSafe((renderer) -> renderer.teleport(player, location));
    }

    @Override
    public void recalculate() {
        final int pageIndex = getParent().getIndex(this);
        final Set<Player> viewers = getParent().getVisibilityManager().getViewerPlayers(pageIndex);

        final boolean horizontal = getParent().getSettings().isRotateHorizontal();
        final boolean vertical = getParent().getSettings().isRotateVertical(); // && isTextOnly(); TODO
        final boolean heads = getParent().getSettings().isRotateHeads();

        for (Player viewer : viewers) {
            recalculate(viewer, horizontal, vertical, heads);
        }
    }

    @Override
    public void recalculate(@NotNull Player player, boolean horizontal, boolean vertical, boolean heads) {
        final Location hologramLocation = getParent().getPositionManager().getActualLocation();
        final boolean downOrigin = getParent().getSettings().isDownOrigin();
        final double totalHeight = getHeight();

        // If the hologram location is originating from the bottom,
        // we need to move the start location up by the total height
        // of the hologram.
        if (downOrigin) {
            hologramLocation.add(0, totalHeight, 0);
        }

        // If we don't rotate, we just align the lines properly.
        if (!horizontal && !vertical) {
            for (HologramLine line : lines) {
                final HologramLineRenderer renderer = line.getRenderer();
                final PositionManager positionManager = line.getPositionManager();
                positionManager.setLocation(hologramLocation.clone());
                if (renderer != null) {
                    renderer.teleport(player, positionManager.getActualLocation());
                }
                hologramLocation.subtract(0, line.getSettings().getHeight(), 0);
            }
            return;
        }

        final Location playerEyeLocation = player.getEyeLocation();
        final Vector playerLookDirection = playerEyeLocation.getDirection().clone().normalize();

        // Calculate the required vectors.
        final Vector horizontalPerpendicular = playerLookDirection.clone()
                .crossProduct(MathUtil.UP_VECTOR)
                .normalize();
        final Vector verticalPerpendicular = horizontalPerpendicular.clone()
                .crossProduct(playerLookDirection)
                .multiply(-1d)
                .normalize();

        // Calculate the pivot point. (The center of the hologram)
        final Location pivot = hologramLocation.clone().subtract(0, totalHeight / 2, 0);

        // Calculate new location for each line.
        double height = 0.0d;
        for (HologramLine line : lines) {
            final HologramLineRenderer renderer = line.getRenderer();
            if (renderer == null) {
                // Line is not visible.
                continue;
            }

            final PositionManager positionManager = line.getPositionManager();
            final HologramLineSettings settings = line.getSettings();
            final Vector offsets = positionManager.getOffsets();

            // Calculate the new location.
            // NOTE: Vertical alignment is only supported for text lines.
            final Location location;
            if (vertical && line.getType() == HologramLineType.TEXT) {
                // If we rotate vertically, we put the lines along the relative vertical vector.
                final Vector vector = verticalPerpendicular.clone().multiply(height - totalHeight / 2);
                location = pivot.clone().add(vector);
            } else {
                // If we don't rotate vertically, we put the lines above each other.
                final double totalOffsetY = offsets.getY() + settings.getOffsetY();
                location = hologramLocation.clone().add(0, height + totalOffsetY, 0);
            }

            height += settings.getHeight();

            // Add the line offsets.
            if (horizontal) {
                final double totalOffsetX = offsets.getX() + settings.getOffsetX();
                final double totalOffsetZ = offsets.getZ() + settings.getOffsetZ();
                if (totalOffsetX != 0) {
                    location.add(horizontalPerpendicular.clone().multiply(totalOffsetX));
                }
                if (totalOffsetZ != 0) {
                    location.add(playerLookDirection.clone().multiply(totalOffsetZ));
                }
            }

            // Update the line location for the current viewer.
            renderer.teleport(player, location);
        }
    }

    @NotNull
    @Override
    public Location getNextLineLocation() {
        final double height = getHeight();
        final Location location = getParent().getPositionManager().getActualLocation();
        return location.subtract(0, height, 0);
    }

    @NotNull
    @Override
    public Hologram getParent() {
        return parent;
    }

    @NotNull
    public ConditionHolder getClickConditions() {
        return clickConditions;
    }

    @NotNull
    public ActionHolder getClickActions() {
        return clickActions;
    }

    public double getHeight() {
        return lines.stream()
                .mapToDouble((l) -> l.getSettings().getHeight())
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
        // Remove the line from the list
        HologramLine line = lines.remove(index);

        // Hide the line to all viewers
        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::hide);

        // Realign other lines
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
        // Add the line to the list
        lines.add(line);

        // Show the line to all viewers
        // TODO: check view conditions
        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::display);

        // Realign other lines
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
        // Add the line to the list
        lines.add(index, line);

        // Show the line to all viewers
        // TODO: check view conditions
        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::display);

        // Realign other lines
        recalculate();

        return this;
    }

    @NotNull
    @Override
    public HologramPage setLine(int index, @NotNull HologramLine line) {
        // Remove the previous line from the list
        HologramLine previousLine = lines.remove(index);

        // Hide the previous line to all viewers
        forEachViewerUseLineRendererSafe(previousLine, HologramLineRenderer::hide);

        // Add the new line
        addLine(index, line);

        return this;
    }

    @NotNull
    @Override
    public HologramPage clearLines() {
        // Hide all lines from all viewers
        forEachLineRendererAndViewerSafe(HologramLineRenderer::hide);

        // Clear the list
        lines.clear();

        return this;
    }

    @NotNull
    @Override
    public HologramPage setLinesFromStrings(@NotNull List<String> lines) {
        return setLines(lines.stream()
                .map((s) -> new DefaultHologramLine(this, getNextLineLocation(), s))
                .collect(Collectors.toList()));
    }

    @NotNull
    @Override
    public HologramPage setLines(@NotNull List<HologramLine> lines) {
        // Hide all lines from all viewers
        forEachLineRendererAndViewerSafe(HologramLineRenderer::hide);

        // Clear the list
        this.lines.clear();

        // Add all lines
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

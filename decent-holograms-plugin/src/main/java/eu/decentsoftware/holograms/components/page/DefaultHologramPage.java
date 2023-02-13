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

package eu.decentsoftware.holograms.components.page;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.PositionManager;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.line.HologramLineRenderer;
import eu.decentsoftware.holograms.api.component.line.HologramLineType;
import eu.decentsoftware.holograms.api.component.page.HologramPage;
import eu.decentsoftware.holograms.utils.MathUtil;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultHologramPage implements HologramPage {

    private final @NotNull Hologram parent;
    private final @NotNull List<HologramLine> lines;
    private final @NotNull ConditionHolder clickConditions;
    private final @NotNull ActionHolder clickActions;

    public DefaultHologramPage(@NotNull Hologram parent) {
        this(parent, new ConditionHolder(), new ActionHolder());
    }

    protected DefaultHologramPage(@NotNull Hologram parent, @NotNull ConditionHolder clickConditions, @NotNull ActionHolder clickActions) {
        this.parent = parent;
        this.lines = new ArrayList<>();
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
    }

    @Override
    public void display(@NotNull Player player) {
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                renderer.display(player);
            }
        }
    }

    @Override
    public void hide(@NotNull Player player) {
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().hide(player);
            }
        }
    }

    @Override
    public void update(@NotNull Player player) {
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().update(player);
            }
        }
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        for (HologramLine line : lines) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().teleport(player, location);
            }
        }
    }

    @Override
    public void recalculate() {
        int pageIndex = getParent().getIndex(this);
        Set<Player> viewers = getParent().getVisibilityManager().getViewerPlayers(pageIndex);

        boolean horizontal = getParent().getSettings().isRotateHorizontal();
        boolean vertical = getParent().getSettings().isRotateVertical(); // && isTextOnly(); TODO
        boolean heads = getParent().getSettings().isRotateHeads();

        for (Player viewer : viewers) {
            recalculate(viewer, horizontal, vertical, heads);
        }
    }

    public void recalculate(@NotNull Player player, boolean horizontal, boolean vertical, boolean heads) {
        Location location = getParent().getPositionManager().getActualLocation();

        if (horizontal || vertical) {
            double totalHeight = getHeight();
            Location pLoc = player.getEyeLocation();
            Vector pDir = pLoc.getDirection().clone().normalize();

            // Calculate the required vectors.
            Vector horizontalPerpendicular = pLoc.getDirection().clone()
                    .crossProduct(MathUtil.UP_VECTOR)
                    .normalize();
            Vector verticalPerpendicular = horizontalPerpendicular.clone()
                    .crossProduct(pDir)
                    .multiply(-1d)
                    .normalize();

            // Calculate new location for each line.
            double height = 0.0d;
            Location pivot = location.clone().subtract(0, totalHeight / 2, 0);
            for (HologramLine line : lines) {
                if (line.getRenderer() == null) {
                    // Line is not visible.
                    continue;
                }
                PositionManager positionManager = line.getPositionManager();
                Vector offsets = positionManager.getOffsets();
                double offsetY = offsets.getY() + line.getSettings().getOffsetY();

                // Calculate the new location.
                Location loc;
                if (vertical || line.getType() != HologramLineType.TEXT) {
                    // If we rotate vertically, we put the lines along the relative vertical vector.
                    Vector vector = verticalPerpendicular.clone().multiply(height - totalHeight / 2);
                    loc = pivot.clone().add(vector);
                } else {
                    // If we don't rotate vertically, we put the lines above each other.
                    loc = location.clone().add(0, height + offsetY, 0);
                }

                height += line.getSettings().getHeight();

                // Adapt the lines offsets.
                if (horizontal) {
                    double offsetX = offsets.getX() + line.getSettings().getOffsetX();
                    double offsetZ = offsets.getZ() + line.getSettings().getOffsetZ();
                    if (offsetX != 0) {
                        loc.add(horizontalPerpendicular.clone().multiply(offsetX));
                    }
                    if (offsetZ != 0) {
                        loc.add(pDir.clone().multiply(offsetZ));
                    }
                }

                // Update head rotation.
                if (heads) {
                    // TODO: implement head rotation
                    if (line.getType() == HologramLineType.HEAD || line.getType() == HologramLineType.SMALL_HEAD) {

                    } else if (line.getType() == HologramLineType.ENTITY) {

                    }
                }

                // Set the new location.
                positionManager.setLocation(loc);

                // Update the line location for the current viewer.
                line.getRenderer().teleport(player, loc);
            }
        } else {
            for (HologramLine line : lines) {
                PositionManager positionManager = line.getPositionManager();
                Location actualLocation = positionManager.getActualLocation().clone();
                HologramLineRenderer renderer = line.getRenderer();
                if (renderer != null) {

                    // Update head rotation.
                    if (heads) {
                        // TODO: implement head rotation
                        if (line.getType() == HologramLineType.HEAD || line.getType() == HologramLineType.SMALL_HEAD) {

                        } else if (line.getType() == HologramLineType.ENTITY) {

                        }
                    }

                    line.getRenderer().teleport(player, actualLocation);
                }
                location.add(0, line.getSettings().getHeight(), 0);
            }
        }
    }

    @Override
    public Location getNextLineLocation() {
        double height = getHeight();
        Location location = getParent().getPositionManager().getActualLocation();
        double yOffset = getParent().getSettings().isDownOrigin() ? -height : height;
        return location.add(0, yOffset, 0);
    }

    @NotNull
    @Override
    public Hologram getParent() {
        return parent;
    }

    @NotNull
    public ConditionHolder getClickConditionHolder() {
        return clickConditions;
    }

    @NotNull
    public ActionHolder getClickActionHolder() {
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
        for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
            line.getRenderer().hide(viewerPlayer);
        }

        // Realign other lines
        recalculate();
        return line;
    }

    @Override
    public void addLine(@NotNull HologramLine line) {
        // Add the line to the list
        lines.add(line);

        // Show the line to all viewers
        for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
            // TODO: check view conditions
            line.getRenderer().display(viewerPlayer);
        }

        // Realign other lines
        recalculate();
    }

    @Override
    public void addLine(int index, @NotNull HologramLine line) {
        // Add the line to the list
        lines.add(index, line);

        // Show the line to all viewers
        for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
            // TODO: check view conditions
            line.getRenderer().display(viewerPlayer);
        }

        // Realign other lines
        recalculate();
    }

    @Override
    public void setLine(int index, @NotNull HologramLine line) {
        // Remove the previous line from the list
        HologramLine previousLine = lines.remove(index);

        // Hide the previous line to all viewers
        for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
            previousLine.getRenderer().hide(viewerPlayer);
        }

        // Add the new line
        addLine(index, line);
    }

    @Override
    public void clearLines() {
        // Hide all lines from all viewers
        for (HologramLine line : lines) {
            for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
                line.getRenderer().hide(viewerPlayer);
            }
        }

        // Clear the list
        lines.clear();
    }

    @Override
    public void setLines(@NotNull List<HologramLine> lines) {
        // Hide all lines from all viewers
        for (HologramLine line : this.lines) {
            for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
                line.getRenderer().hide(viewerPlayer);
            }
        }

        // Clear the list
        this.lines.clear();

        // Add all lines
        this.lines.addAll(lines);

        // Show all lines to all viewers
        for (HologramLine line : this.lines) {
            for (Player viewerPlayer : parent.getVisibilityManager().getViewerPlayers()) {
                line.getRenderer().display(viewerPlayer);
            }
        }
    }

    @NotNull
    @Override
    public List<HologramLine> getLines() {
        return ImmutableList.copyOf(lines);
    }

}

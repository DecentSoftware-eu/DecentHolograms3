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

import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.PositionManager;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.line.HologramLineRenderer;
import eu.decentsoftware.holograms.api.component.line.HologramLineType;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.component.page.PageLineHolder;
import eu.decentsoftware.holograms.utils.MathUtil;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DefaultPage implements Page {

    private final @NotNull Hologram parent;
    private final @NotNull PageLineHolder lineHolder;
    private final @NotNull ConditionHolder clickConditions;
    private final @NotNull ActionHolder clickActions;

    public DefaultPage(@NotNull Hologram parent) {
        this.parent = parent;
        this.lineHolder = new DefaultPageLineHolder(this);
        this.clickConditions = new ConditionHolder();
        this.clickActions = new ActionHolder();
    }

    protected DefaultPage(@NotNull Hologram parent, @NotNull ConditionHolder clickConditions, @NotNull ActionHolder clickActions) {
        this.parent = parent;
        this.lineHolder = new DefaultPageLineHolder(this);
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
    }

    @Override
    public void display(@NotNull Player player) {
        for (HologramLine line : lineHolder.getLines()) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                renderer.display(player);
            }
        }
    }

    @Override
    public void hide(@NotNull Player player) {
        for (HologramLine line : lineHolder.getLines()) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().hide(player);
            }
        }
    }

    @Override
    public void update(@NotNull Player player) {
        for (HologramLine line : lineHolder.getLines()) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().update(player);
            }
        }
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        for (HologramLine line : lineHolder.getLines()) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().teleport(player, location);
            }
        }
    }

    @Override
    public void recalculate() {
        int pageIndex = getParent().getPageHolder().getIndex(this);
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
            for (HologramLine line : getLineHolder().getLines()) {
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
            for (HologramLine line : getLineHolder().getLines()) {
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
    @Override
    public PageLineHolder getLineHolder() {
        return lineHolder;
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
        return lineHolder.getLines().stream()
                .mapToDouble((l) -> l.getSettings().getHeight())
                .sum();
    }

}

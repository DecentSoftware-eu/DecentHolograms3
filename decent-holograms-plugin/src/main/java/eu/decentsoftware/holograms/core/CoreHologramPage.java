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
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.core.line.renderer.LineRenderer;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class CoreHologramPage<LINE extends CoreHologramLine> extends CoreHologramComponent {

    protected final DecentHolograms plugin;
    protected final CoreHologram<? extends CoreHologramPage<?>> parent;
    protected final List<LINE> lines = new ArrayList<>();

    public CoreHologramPage(@NonNull DecentHolograms plugin, @NonNull CoreHologram<? extends CoreHologramPage<?>> parent) {
        this.plugin = plugin;
        this.parent = parent;
    }

    public void destroy() {
        clearLines();

        super.destroy();
    }

    /**
     * Update the positions of all lines in this page. This method
     * does not update the locations for all viewers of this page.
     * <p>
     * This method properly positions the lines above each other
     * starting from the origin of the hologram and going down.
     * Lines are positioned in the center of the hologram.
     */
    private void updateLinePositions() {
        checkDestroyed();

        DecentLocation hologramLocation = this.parent.getPositionManager().getLocation();
        if (this.parent.getSettings().isDownOrigin()) {
            hologramLocation = new DecentLocation(
                    hologramLocation.getWorldName(),
                    hologramLocation.getX(),
                    hologramLocation.getY() - getHeight(),
                    hologramLocation.getZ()
            );
        }

        for (LINE line : this.lines) {
            line.setLocation(hologramLocation);
            hologramLocation = new DecentLocation(
                    hologramLocation.getWorldName(),
                    hologramLocation.getX(),
                    hologramLocation.getY() - line.getSettings().getHeight(),
                    hologramLocation.getZ()
            );
        }
    }

    @NonNull
    protected abstract LINE createLine(@NonNull DecentLocation location, @NonNull String content);

    @Nullable
    public LINE getLine(int index) {
        return this.lines.get(index);
    }

    @SuppressWarnings("unchecked")
    public int getIndex(@NonNull CoreHologramLine line) {
        return this.lines.indexOf((LINE) line);
    }

    public void removeLine(int index) {
        LINE line = this.lines.remove(index);

        updateLinePositions();

        this.parent.getVisibilityManager().updateVisibility(line);
        this.parent.recalculate();
    }

    public void appendLine(@NonNull String content) {
        appendLine(createLine(getNextLineLocation(), content));
    }

    public void appendLine(@NonNull LINE line) {
        checkDestroyed();

        this.lines.add(line);

        updateLinePositions();

        this.parent.getVisibilityManager().updateVisibility(line);
        this.parent.recalculate();
    }

    public void insertLine(int index, @NonNull String content) {
        checkDestroyed();

        LINE line = createLine(getNextLineLocation(), content);
        this.lines.add(index, line);

        updateLinePositions();

        this.parent.getVisibilityManager().updateVisibility(line);
        this.parent.recalculate();
    }

    public void setLine(int index, @NonNull String content) {
        checkDestroyed();

        LINE line = this.lines.remove(index);
        line.setContent(content);

        updateLinePositions();

        this.parent.recalculate();
    }

    public void clearLines() {
        checkDestroyed();

        for (LINE line : this.lines) {
            line.destroy();
        }
        this.lines.clear();
    }

    public void setLinesFromStrings(@NonNull List<String> contents) {
        for (int i = 0; i < contents.size(); i++) {
            setLine(i, contents.get(i));
        }
    }

    @NonNull
    public List<LINE> getLines() {
        return ImmutableList.copyOf(this.lines);
    }

    @NonNull
    public CoreHologram<? extends CoreHologramPage<?>> getParent() {
        return this.parent;
    }

    public double getHeight() {
        double height = 0d;
        for (LINE line : this.lines) {
            height += line.getSettings().getHeight();
        }
        return height;
    }

    public double getWidth(@NonNull Player player) {
        double width = 0d;
        for (LINE line : this.lines) {
            LineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                width = Math.max(width, renderer.getWidth(player));
            }
        }
        return width;
    }

    @NonNull
    public DecentLocation getNextLineLocation() {
        Location location = this.parent.getPositionManager().getActualBukkitLocation().clone();
        if (this.parent.getSettings().isDownOrigin()) {
            location.add(0, getHeight(), 0);
        }
        return new DecentLocation(location.subtract(0, getHeight(), 0));
    }

}

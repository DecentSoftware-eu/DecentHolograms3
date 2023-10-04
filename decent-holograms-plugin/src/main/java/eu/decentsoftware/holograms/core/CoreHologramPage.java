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
import eu.decentsoftware.holograms.core.line.renderer.HologramLineRenderer;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
     * Show this page to the specified player. This method displays all the lines
     * of this page to the player.
     *
     * @param player The player to show this page to.
     */
    public void display(@NonNull Player player) {
        checkDestroyed();

        this.lines.forEach(line -> line.display(player));
    }

    /**
     * Hide this page from the specified player. This method hides all the lines
     * of this page from the player.
     *
     * @param player The player to hide this page from.
     */
    public void hide(@NonNull Player player) {
        checkDestroyed();

        this.lines.forEach(line -> line.hide(player));
    }

    /**
     * Update this page for the specified player. This method updates all the lines
     * of this page for the player.
     *
     * @param player The player to update this page for.
     */
    public void updateContent(@NonNull Player player) {
        checkDestroyed();

        this.lines.forEach(line -> line.updateContent(player));
    }

    /**
     * Update the location of this page for the given player. This method updates
     * the location of all lines in this page for the player.
     *
     * @param player The player to teleport this page for.
     */
    public void updateLocation(@NonNull Player player) {
        checkDestroyed();

        this.lines.forEach(line -> line.updateLocation(player));
    }

    /**
     * Update the positions of all lines in this page. This method
     * does not update the locations for all viewers of this page.
     * <p>
     * This method properly positions the lines above each other
     * starting from the origin of the hologram and going down.
     * Lines are positioned in the center of the hologram.
     */
    public void updateLinePositions() {
        checkDestroyed();

        DecentLocation hologramLocation = this.parent.getPositionManager().getLocation().clone();
        if (this.parent.getSettings().isDownOrigin()) {
            hologramLocation.add(0, getHeight(), 0);
        }

        for (LINE line : this.lines) {
            line.setLocation(hologramLocation.clone());
            hologramLocation.subtract(0, line.getSettings().getHeight(), 0);
        }
    }

    @NonNull
    protected abstract LINE createLine(@NonNull DecentLocation location, @NonNull String content);

    @Nullable
    public LINE getLine(int index) {
        return this.lines.get(index);
    }

    public int getIndex(@NonNull CoreHologramLine line) {
        return this.lines.indexOf(line);
    }

    public void removeLine(int index) {
        LINE line = this.lines.remove(index);

        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::hide);
        updateLinePositions();
        parent.recalculate();
    }

    public void appendLine(@NonNull String content) {
        appendLine(createLine(getNextLineLocation(), content));
    }

    public void appendLine(@NonNull LINE line) {
        checkDestroyed();

        this.lines.add(line);

        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::display);
        updateLinePositions();
        parent.recalculate();
    }

    public void insertLine(int index, @NonNull String content) {
        checkDestroyed();

        LINE line = createLine(getNextLineLocation(), content);
        this.lines.add(index, line);

        forEachViewerUseLineRendererSafe(line, HologramLineRenderer::display);
        updateLinePositions();
        parent.recalculate();
    }

    public void setLine(int index, @NonNull String content) {
        checkDestroyed();

        LINE line = this.lines.remove(index);
        line.setContent(content);

        updateLinePositions();
        parent.recalculate();
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
        return this.lines.stream()
                .mapToDouble(LINE::getBlockHeight)
                .sum();
    }

    @NonNull
    public DecentLocation getNextLineLocation() {
        Location location = this.parent.getPositionManager().getActualBukkitLocation().clone();
        if (this.parent.getSettings().isDownOrigin()) {
            location.add(0, getHeight(), 0);
        }
        return new DecentLocation(location.subtract(0, getHeight(), 0));
    }

    private void forEachLineRendererSafe(@NonNull Consumer<HologramLineRenderer> consumer) {
        for (CoreHologramLine line : this.lines) {
            HologramLineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                consumer.accept(renderer);
            }
        }
    }

    private void forEachLineRendererAndViewerSafe(@NonNull BiConsumer<HologramLineRenderer, Player> consumer) {
        for (CoreHologramLine line : this.lines) {
            forEachViewerUseLineRendererSafe(line, consumer);
        }
    }

    private void forEachViewerUseLineRendererSafe(@NonNull CoreHologramLine line, @NonNull BiConsumer<HologramLineRenderer, Player> consumer) {
        HologramLineRenderer renderer = line.getRenderer();
        if (renderer != null) {
            for (Player viewerPlayer : this.parent.getVisibilityManager().getViewersAsPlayers()) {
                consumer.accept(renderer, viewerPlayer);
            }
        }
    }

}

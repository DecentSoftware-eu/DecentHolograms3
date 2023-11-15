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

import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a view of a hologram for a specific player.
 * <p>
 * This class is responsible for displaying the hologram to the player.
 * <p>
 * This class is also responsible for updating the visible lines
 * based on the current page and any other conditions.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class CoreHologramView {

    protected final Player player;
    /**
     * The parent hologram of this view.
     */
    protected final CoreHologram<?> hologram;
    /**
     * The current page that is being displayed to the player.
     * <p>
     * This page is always part of the hologram.
     */
    protected CoreHologramPage<?> currentPage;
    protected int currentPageIndex;
    /**
     * The lines that are currently visible to the player based
     * on the current page and any other conditions.
     * <p>
     * The lines in this list have to be ordered by their
     * order in the page. (Top to bottom)
     * <p>
     * This list should be updated every time the page is changed.
     */
    protected final List<CoreHologramLine> visibleLines = new ArrayList<>();

    public CoreHologramView(
            @NonNull Player player,
            @NonNull CoreHologram<?> hologram,
            @NonNull CoreHologramPage<?> currentPage
    ) {
        this.player = player;
        this.hologram = hologram;
        this.setCurrentPage(currentPage);
    }

    /**
     * Check if the specified line should be displayed to the player.
     *
     * @param line The line to check.
     * @return True if the line should be displayed, false otherwise.
     */
    protected abstract boolean checkLineViewConditions(@NonNull CoreHologramLine line);

    public synchronized void destroy() {
        this.hideVisibleLines();
        this.visibleLines.clear();
    }

    /**
     * Update the visible lines based on the current page and any other conditions.
     * <p>
     * This method decides which lines should be displayed to the player.
     */
    public synchronized void updateVisibleLines() {
        List<? extends CoreHologramLine> pageLines = this.currentPage.getLines();
        int visibleLinesIndexOffset = 0;
        int newVisibleLinesCount = pageLines.size();
        for (int i = 0; i < pageLines.size(); i++) {
            int visibleLinesIndex = i + visibleLinesIndexOffset;
            CoreHologramLine line = pageLines.get(i);

            if (!this.checkLineViewConditions(line)) {
                visibleLinesIndexOffset--;
                newVisibleLinesCount--;
                continue;
            }

            if (this.visibleLines.size() <= visibleLinesIndex) {
                line.display(this.player);
                this.visibleLines.add(line);
                continue;
            }

            CoreHologramLine currentLine = this.visibleLines.get(visibleLinesIndex);
            if (line.equals(currentLine)) {
                continue;
            }

            this.visibleLines.set(visibleLinesIndex, line);
            if (line.getType() == currentLine.getType() && visibleLinesIndexOffset == 0) {
                /*
                 * If the line type is the same, we can just update the content. This is
                 * more efficient than hiding the old line and displaying the new one. It
                 * also prevents flickering when the line is updated.
                 */
                line.updateContent(this.player);
            } else {
                currentLine.hide(this.player);
                line.display(this.player);
            }
        }

        if (newVisibleLinesCount < this.visibleLines.size()) {
            for (int i = newVisibleLinesCount; i < this.visibleLines.size(); i++) {
                CoreHologramLine currentLine = this.visibleLines.remove(i);
                currentLine.hide(this.player);
            }
        }
    }

    protected synchronized void hideVisibleLines() {
        for (CoreHologramLine line : this.visibleLines) {
            line.hide(this.player);
        }
    }

    protected synchronized void displayVisibleLines() {
        for (CoreHologramLine line : this.visibleLines) {
            line.display(this.player);
        }
    }

    protected synchronized void updateVisibleLinesContents() {
        for (CoreHologramLine line : this.visibleLines) {
            line.updateContent(this.player);
        }
    }

    protected synchronized void updateVisibleLinesLocations() {
        for (CoreHologramLine line : this.visibleLines) {
            line.updateLocation(this.player);
        }
    }

    private synchronized void setCurrentPage(@NonNull CoreHologramPage<?> page, int index) {
        if (!this.hologram.equals(page.getParent())) {
            throw new IllegalArgumentException("Page is not part of this hologram.");
        }
        if (page.equals(this.currentPage)) {
            return;
        }
        this.currentPage = page;
        this.currentPageIndex = index;
        this.updateVisibleLines();
    }

    /**
     * Set the current page that is being displayed to the player.
     * <p>
     * This method will update the visible lines accordingly.
     *
     * @param page The new page.
     * @throws IllegalArgumentException If the page is not part of this hologram.
     */
    public synchronized void setCurrentPage(@NonNull CoreHologramPage<?> page) {
        int index = this.hologram.getPages().indexOf(page);
        if (index == -1) {
            throw new IllegalArgumentException("Page is not part of this hologram.");
        }
        this.setCurrentPage(page, index);
    }

    /**
     * Set the current page that is being displayed to the player.
     * <p>
     * This method will update the visible lines accordingly.
     *
     * @param index The index of the new page.
     * @throws IllegalArgumentException If the page is not part of this hologram.
     */
    public synchronized void setCurrentPage(int index) {
        CoreHologramPage<?> page = this.hologram.getPage(index);
        if (page == null) {
            throw new IllegalArgumentException("Page index is out of bounds.");
        }
        this.setCurrentPage(page, index);
    }

    /**
     * Check if the specified line is currently visible to the player.
     *
     * @param line The line to check.
     * @return True if the line is visible, false otherwise.
     */
    public boolean canSeeLine(@NonNull CoreHologramLine line) {
        return this.visibleLines.contains(line);
    }

    @NonNull
    public CoreHologramPage<?> getCurrentPage() {
        return this.currentPage;
    }

    public int getCurrentPageIndex() {
        return this.currentPageIndex;
    }

    @NonNull
    public Player getPlayer() {
        return this.player;
    }

}

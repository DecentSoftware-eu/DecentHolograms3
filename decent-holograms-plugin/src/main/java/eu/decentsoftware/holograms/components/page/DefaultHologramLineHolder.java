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
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.page.HologramPage;
import eu.decentsoftware.holograms.api.component.page.HologramLineHolder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DefaultHologramLineHolder implements HologramLineHolder {

    private final @NotNull HologramPage parent;
    private final @NotNull List<HologramLine> lines;

    /**
     * Create a new instance of {@link DefaultHologramLineHolder}.
     *
     * @param parent The parent page of this page line holder.
     * @see HologramPage
     */
    public DefaultHologramLineHolder(@NotNull HologramPage parent) {
        this.parent = parent;
        this.lines = new ArrayList<>();
    }

    @NotNull
    @Override
    public HologramPage getParent() {
        return parent;
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
        for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
            line.getRenderer().hide(viewerPlayer);
        }

        // Realign other lines
        parent.recalculate();
        return line;
    }

    @Override
    public void addLine(@NotNull HologramLine line) {
        // Add the line to the list
        lines.add(line);

        // Show the line to all viewers
        for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
            // TODO: check view conditions
            line.getRenderer().display(viewerPlayer);
        }

        // Realign other lines
        parent.recalculate();
    }

    @Override
    public void addLine(int index, @NotNull HologramLine line) {
        // Add the line to the list
        lines.add(index, line);

        // Show the line to all viewers
        for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
            // TODO: check view conditions
            line.getRenderer().display(viewerPlayer);
        }

        // Realign other lines
        parent.recalculate();
    }

    @Override
    public void setLine(int index, @NotNull HologramLine line) {
        // Remove the previous line from the list
        HologramLine previousLine = lines.remove(index);

        // Hide the previous line to all viewers
        for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
            previousLine.getRenderer().hide(viewerPlayer);
        }

        // Add the new line
        addLine(index, line);
    }

    @Override
    public void clearLines() {
        // Hide all lines from all viewers
        for (HologramLine line : lines) {
            for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
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
            for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
                line.getRenderer().hide(viewerPlayer);
            }
        }

        // Clear the list
        this.lines.clear();

        // Add all lines
        this.lines.addAll(lines);

        // Show all lines to all viewers
        for (HologramLine line : this.lines) {
            for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
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

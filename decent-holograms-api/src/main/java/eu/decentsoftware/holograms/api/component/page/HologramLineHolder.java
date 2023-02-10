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

package eu.decentsoftware.holograms.api.component.page;

import eu.decentsoftware.holograms.api.component.line.HologramLine;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class represents a page line holder. It's responsible for holding all the lines of a page
 * and providing methods for manipulating them.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramLineHolder {

    /**
     * Get the parent page of this page line holder.
     *
     * @return The parent page of this page line holder.
     */
    @NotNull
    HologramPage getParent();

    /**
     * Get the line at the specified index.
     *
     * @param index The index of the line to get.
     * @return The line at the specified index.
     */
    HologramLine getLine(int index);

    /**
     * Get the index of the given line.
     *
     * @param line The line to get the index of.
     * @return The index or -1 if the given line in not present in this line holder.
     */
    int getIndex(@NotNull HologramLine line);

    /**
     * Remove the line at the specified index.
     *
     * @param index The index of the line to remove.
     * @return The removed line.
     */
    HologramLine removeLine(int index);

    /**
     * Add a line to the end of this page.
     *
     * @param line The line to add.
     */
    void addLine(@NotNull HologramLine line);

    /**
     * Add a line at the specified index.
     *
     * @param index The index to add the line at.
     * @param line The line to add.
     */
    void addLine(int index, @NotNull HologramLine line);

    /**
     * Set the line at the specified index.
     *
     * @param index The index of the line to set.
     * @param line The line to set.
     */
    void setLine(int index, @NotNull HologramLine line);

    /**
     * Remove all lines from this page.
     */
    void clearLines();

    /**
     * Set the lines of this page.
     *
     * @param lines The lines to set.
     */
    void setLines(@NotNull List<HologramLine> lines);

    /**
     * Get the lines of this page. The returned list is immutable.
     *
     * @return Immutable list of lines.
     */
    @NotNull
    List<HologramLine> getLines();

}

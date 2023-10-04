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

package eu.decentsoftware.holograms.api.hologram;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class represents a hologram page. A page is a collection of lines.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface HologramPage {

    /**
     * Destroy this page. This method removes all the lines of this page and
     * makes it unusable.
     * <p>
     * This page should not be referenced after this method is called.
     */
    void destroy();

    /**
     * Get the line at the specified index.
     *
     * @param index The index of the line to get.
     * @return The line at the specified index or null if the index is out of bounds.
     */
    @Nullable
    HologramLine getLine(int index);

    /**
     * Get the index of the given line.
     *
     * @param line The line to get the index of.
     * @return The index or -1 if the given line in not present in this line holder.
     */
    int getIndex(@NonNull HologramLine line);

    /**
     * Remove the line at the specified index.
     *
     * @param index The index of the line to remove.
     */
    void removeLine(int index);

    /**
     * Add a line to the end of this page.
     *
     * @param content The content of the line to add.
     */
    void appendLine(@NonNull String content);

    /**
     * Add a line to the end of this page.
     *
     * @param index   The index to add the line at.
     * @param content The content of the line to add.
     */
    void insertLine(int index, @NonNull String content);

    /**
     * Set the line at the specified index.
     *
     * @param index   The index of the line to set.
     * @param content The content of the line to set.
     */
    void setLine(int index, @NonNull String content);

    /**
     * Remove all lines from this page.
     */
    void clearLines();

    /**
     * Set the lines of this page.
     *
     * @param contents The contents of the lines to set.
     */
    void setLinesFromStrings(@NonNull List<String> contents);

    /**
     * Get the parent {@link Hologram} of this page.
     *
     * @return The parent {@link Hologram} of this page.
     */
    @NonNull
    Hologram getParent();

    /**
     * Get the height of this page. The height is the sum of the heights of all the lines.
     *
     * @return The height of this page.
     */
    double getHeight();

}

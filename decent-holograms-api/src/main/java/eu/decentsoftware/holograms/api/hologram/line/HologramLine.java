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

package eu.decentsoftware.holograms.api.hologram.line;

import eu.decentsoftware.holograms.api.hologram.component.ClickHandler;
import eu.decentsoftware.holograms.api.hologram.component.PositionManager;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a hologram line. A line is a collection of components
 * that can be moved in the world. A line can be added to a {@link HologramPage}.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface HologramLine {

    /**
     * Get the parent {@link HologramPage} of this line.
     *
     * @return The parent {@link HologramPage} of this line.
     */
    @NotNull
    HologramPage getParent();

    /**
     * Get the type of this line.
     *
     * @return The type of this line.
     * @see HologramLineType
     */
    @NotNull
    HologramLineType getType();

    /**
     * Get the settings of this line.
     *
     * @return The settings of this line.
     * @see HologramLineSettings
     */
    @NotNull
    HologramLineSettings getSettings();

    /**
     * Get the position manager of this line.
     *
     * @return The position manager of this line.
     * @see PositionManager
     */
    @NotNull
    PositionManager getPositionManager();

    /**
     * Get the line renderer of this line.
     *
     * @return The line renderer of this line.
     * @see HologramLineRenderer
     */
    @Nullable
    HologramLineRenderer getRenderer();

    /**
     * Set the line renderer of this line.
     *
     * @param renderer The line renderer of this line.
     * @see HologramLineRenderer
     */
    void setRenderer(@NotNull HologramLineRenderer renderer);

    /**
     * Get the raw content of this line.
     *
     * @return The raw content of this line.
     */
    @Nullable
    String getContent();

    /**
     * Set the raw content of this line. This method also parses the content and
     * updates the line accordingly.
     *
     * @param content The raw content of this line.
     */
    void setContent(@NotNull String content);

    /**
     * Get the click handler of this hologram line.
     *
     * @return The click handler of this hologram line.
     * @see ClickHandler
     */
    @Nullable
    ClickHandler getClickHandler();

    /**
     * Set the click handler of this hologram line.
     *
     * @param clickHandler The click handler of this hologram line.
     * @return Instance of this hologram line.
     * @see ClickHandler
     */
    @NotNull
    HologramLine setClickHandler(@Nullable ClickHandler clickHandler);

}

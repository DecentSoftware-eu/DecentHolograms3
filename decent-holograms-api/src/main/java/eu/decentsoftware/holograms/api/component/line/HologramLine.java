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

package eu.decentsoftware.holograms.api.component.line;

import eu.decentsoftware.holograms.api.component.PositionManager;
import eu.decentsoftware.holograms.api.component.page.Page;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * This class represents a hologram line. A line is a collection of components
 * that can be moved in the world. A line can be added to a {@link Page}.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramLine {

    /*
     * TODO:
     *  - Hover content for text lines
     *  - Click handling
     */

    /**
     * Get the UUID of the line.
     *
     * @return The UUID of the line.
     */
    @NotNull
    UUID getUid();

    /**
     * Get the parent {@link Page} of this line.
     *
     * @return The parent {@link Page} of this line.
     */
    @NotNull
    Page getParent();

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

    @Override
    boolean equals(@Nullable Object o);

}

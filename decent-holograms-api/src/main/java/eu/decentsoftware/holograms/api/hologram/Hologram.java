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

import eu.decentsoftware.holograms.api.hologram.click.Clickable;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a hologram. A hologram is a collection of components
 * that can be moved in the world. These components all together form a hologram
 * that's visible to specified players and has multiple pages.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Hologram extends Clickable {

    /**
     * Destroy this hologram. This method hides the hologram and destroys all components.
     * <p>
     * After calling this method, the hologram can't be used anymore and should not be
     * referenced.
     */
    void destroy();

    /**
     * Get the page at the given index.
     *
     * @param index The index of the page to get.
     * @return The page at the given index or null if the index is out of bounds.
     */
    @Nullable
    HologramPage getPage(int index);

    /**
     * Append a new page to this hologram.
     *
     * @return Instance of this hologram.
     */
    @NonNull
    HologramPage appendPage();

    /**
     * Insert a page to this hologram at the specified index.
     *
     * @param index The index to insert the page at.
     * @return Instance of this hologram.
     */
    @NonNull
    HologramPage insertPage(int index);

    /**
     * Remove a page from this hologram by its index.
     *
     * @param index The index of the page to remove.
     */
    void removePage(int index);

    /**
     * Remove all pages from this hologram.
     */
    void clearPages();

    /**
     * The settings manager of the hologram, storing various settings.
     *
     * @return The settings manager of the hologram.
     * @see HologramSettings
     */
    @NonNull
    HologramSettings getSettings();

    /**
     * The position manager of the hologram, used for editing
     * the location of the hologram.
     *
     * @return The position manager of the hologram.
     * @see HologramPositionManager
     */
    @NonNull
    HologramPositionManager getPositionManager();

    /**
     * The visibility manager of the hologram, used for managing
     * the visibility of the hologram.
     *
     * @return The visibility manager of the hologram.
     * @see HologramVisibilityManager
     */
    @NonNull
    HologramVisibilityManager getVisibilityManager();

}

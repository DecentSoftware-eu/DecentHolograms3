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

import eu.decentsoftware.holograms.api.hologram.component.PositionManager;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class represents a hologram. A hologram is a collection of components
 * that can be moved in the world. These components all together form a hologram
 * that's visible to specified players and has multiple pages.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Hologram {

    /**
     * Destroy this hologram. This method hides the hologram and disables all components.
     */
    void destroy();

    /**
     * Delete this hologram. This method destroys the hologram and removes it from the files.
     */
    void delete();

    /**
     * Recalculate all pages of this hologram. This method should be called
     * when the hologram is moved.
     *
     * @see HologramPage#recalculate()
     */
    void recalculate();

    /**
     * The configuration manager of the hologram, used for storing
     * and retrieving data to/from files.
     *
     * @return The configuration manager of the hologram.
     * @see HologramConfig
     */
    @NotNull
    HologramConfig getConfig();

    /**
     * The settings manager of the hologram, storing various settings.
     *
     * @return The settings manager of the hologram.
     * @see HologramSettings
     */
    @NotNull
    HologramSettings getSettings();

    /**
     * The position manager of the hologram, used for editing
     * the location of the hologram.
     *
     * @return The position manager of the hologram.
     * @see PositionManager
     */
    @NotNull
    PositionManager getPositionManager();

    /**
     * The visibility manager of the hologram, used for managing
     * the visibility of the hologram.
     *
     * @return The visibility manager of the hologram.
     * @see HologramVisibilityManager
     */
    @NotNull
    HologramVisibilityManager getVisibilityManager();

    /**
     * Get the page at the given index.
     *
     * @param index The index of the page to get.
     * @return The page at the given index or null if the index is out of bounds.
     */
    @Nullable
    HologramPage getPage(int index);

    /**
     * Get the index of the given page.
     *
     * @param page The page to get the index of.
     * @return The index or -1 if the given page in not present in this page holder.
     */
    int getIndex(@NotNull HologramPage page);

    /**
     * Append a new page to this hologram.
     *
     * @return Instance of this hologram.
     */
    @NotNull
    Hologram addPage();

    /**
     * Add a page to this hologram.
     *
     * @param page The page to add.
     * @return Instance of this hologram.
     */
    @NotNull
    Hologram addPage(@NotNull HologramPage page);

    /**
     * Insert a page to this hologram at the specified index.
     *
     * @param index The index to insert the page at.
     * @param page The page to insert.
     * @return Instance of this hologram.
     */
    @NotNull
    Hologram addPage(int index, @NotNull HologramPage page);

    /**
     * Remove a page from this hologram by its index.
     *
     * @param index The index of the page to remove.
     * @return Instance of this hologram.
     */
    @NotNull
    Hologram removePage(int index);

    /**
     * Remove all pages from this hologram.
     *
     * @return Instance of this hologram.
     */
    @NotNull
    Hologram clearPages();

    /**
     * Set the pages of this hologram.
     *
     * @param pages The pages to set.
     * @return Instance of this hologram.
     */
    @NotNull
    Hologram setPages(@NotNull List<HologramPage> pages);

    /**
     * Get the list of pages in this hologram. The returned list is immutable.
     *
     * @return Immutable list of pages in this hologram.
     */
    @NotNull
    List<HologramPage> getPages();

}

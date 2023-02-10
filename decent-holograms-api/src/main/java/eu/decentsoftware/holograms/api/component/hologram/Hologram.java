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

package eu.decentsoftware.holograms.api.component.hologram;

import eu.decentsoftware.holograms.api.component.PositionManager;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a hologram. A hologram is a collection of components
 * that can be moved in the world. These components all together form a hologram
 * that's visible to specified players and has multiple pages.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface Hologram {

    /**
     * Destroy this hologram. This method hides the hologram and disables all components.
     */
    void destroy();

    /**
     * Get the name of the hologram, used for identification.
     *
     * @return The name of the hologram, used for identification.
     */
    @NotNull
    String getName();

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
     * The page holder of the hologram, used for managing the pages of the hologram.
     *
     * @return The page holder of the hologram.
     * @see HologramPageHolder
     */
    @NotNull
    HologramPageHolder getPageHolder();

}

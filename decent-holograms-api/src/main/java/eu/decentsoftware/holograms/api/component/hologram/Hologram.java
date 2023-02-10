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

    // TODO:
    //  animations
    //  lighting

    /**
     * Destroy this hologram. This method hides the hologram and disables all components.
     */
    void destroy();

    /**
     * Get the name of this hologram.
     *
     * @return The name of this hologram.
     */
    @NotNull
    String getName();

    /**
     * Get the {@link HologramConfig} of this hologram.
     *
     * @return The {@link HologramConfig} of this hologram.
     * @see HologramConfig
     */
    @NotNull
    HologramConfig getConfig();

    /**
     * Get the {@link HologramSettings} of this hologram.
     *
     * @return The {@link HologramSettings} of this hologram.
     * @see HologramSettings
     */
    @NotNull
    HologramSettings getSettings();

    /**
     * Get the {@link HologramVisibilityManager} of this hologram.
     *
     * @return The {@link HologramVisibilityManager} of this hologram.
     * @see HologramVisibilityManager
     */
    @NotNull
    HologramVisibilityManager getVisibilityManager();

    /**
     * Get the {@link HologramPageHolder} of this hologram.
     *
     * @return The {@link HologramPageHolder} of this hologram.
     * @see HologramPageHolder
     */
    @NotNull
    HologramPageHolder getPageHolder();

    /**
     * Get the {@link PositionManager} of this hologram.
     *
     * @return The {@link PositionManager} of this hologram.
     * @see PositionManager
     */
    @NotNull
    PositionManager getPositionManager();

}

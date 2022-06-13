package eu.decent.holograms.api.component.hologram;

import eu.decent.holograms.api.component.common.IActionable;
import eu.decent.holograms.api.component.common.IConditional;
import eu.decent.holograms.api.component.common.INameable;
import eu.decent.holograms.api.component.common.PositionManager;

/**
 * This class represents a hologram. A hologram is a collection of components
 * that can be moved in the world. These components all together form a hologram
 * that's visible to specified players and has multiple pages.
 *
 * @author d0by
 */
public interface Hologram extends INameable, IActionable, IConditional {

    // TODO:
    //  animations
    //  emission

    /**
     * Get the {@link HologramSettings} of this hologram.
     *
     * @return The {@link HologramSettings} of this hologram.
     * @see HologramSettings
     */
    HologramSettings getSettings();

    /**
     * Get the {@link HologramVisibilityManager} of this hologram.
     *
     * @return The {@link HologramVisibilityManager} of this hologram.
     * @see HologramVisibilityManager
     */
    HologramVisibilityManager getVisibilityManager();

    /**
     * Get the {@link HologramPageHolder} of this hologram.
     *
     * @return The {@link HologramPageHolder} of this hologram.
     * @see HologramPageHolder
     */
    HologramPageHolder getPageHolder();

    /**
     * Get the position manager of this hologram.
     *
     * @return The position manager of this hologram.
     * @see PositionManager
     */
    PositionManager getPositionManager();

}

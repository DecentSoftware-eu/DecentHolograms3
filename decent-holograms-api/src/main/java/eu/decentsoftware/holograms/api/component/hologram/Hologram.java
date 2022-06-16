package eu.decentsoftware.holograms.api.component.hologram;

import eu.decentsoftware.holograms.api.component.common.IActionable;
import eu.decentsoftware.holograms.api.component.common.IConditional;
import eu.decentsoftware.holograms.api.component.common.INameable;
import eu.decentsoftware.holograms.api.component.common.PositionManager;
import org.jetbrains.annotations.NotNull;

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
    //  lighting

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
     * Get the position manager of this hologram.
     *
     * @return The position manager of this hologram.
     * @see PositionManager
     */
    @NotNull
    PositionManager getPositionManager();

}

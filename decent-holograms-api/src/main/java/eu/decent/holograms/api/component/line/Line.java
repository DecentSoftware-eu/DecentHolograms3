package eu.decent.holograms.api.component.line;

import eu.decent.holograms.api.component.common.IActionable;
import eu.decent.holograms.api.component.common.IConditional;
import eu.decent.holograms.api.component.common.PositionManager;
import eu.decent.holograms.api.component.page.Page;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a hologram line. A line is a collection of components
 * that can be moved in the world. A line can be added to a {@link Page}.
 *
 * @author d0by
 */
public interface Line extends IActionable, IConditional {

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
     * @see LineType
     */
    @NotNull
    LineType getType();

    /**
     * Get the settings of this line.
     *
     * @return The settings of this line.
     * @see LineSettings
     */
    @NotNull
    LineSettings getSettings();

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
     * @see LineRenderer
     */
    @NotNull
    LineRenderer getRenderer();

    /**
     * Get the raw content of this line.
     *
     * @return The raw content of this line.
     */
    @NotNull
    String getContent();

    /**
     * Set the raw content of this line. This method also parses the content and
     * updates the line accordingly.
     *
     * @param content The raw content of this line.
     */
    void setContent(@NotNull String content);

}

package eu.decent.holograms.api.component.line;

import eu.decent.holograms.api.component.common.IActionable;
import eu.decent.holograms.api.component.common.IConditional;
import eu.decent.holograms.api.component.common.IMovable;
import eu.decent.holograms.api.component.page.Page;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a hologram line. A line is a collection of components
 * that can be moved in the world. A line can be added to a {@link Page}.
 *
 * @author d0by
 */
public interface Line extends IMovable, IActionable, IConditional {

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
     * Get the raw content of this line.
     *
     * @return The raw content of this line.
     */
    @NotNull
    String getContent();

    /**
     * Set the content of this line.
     *
     * @param content The new content of this line.
     */
    void setContent(@NotNull String content);

}

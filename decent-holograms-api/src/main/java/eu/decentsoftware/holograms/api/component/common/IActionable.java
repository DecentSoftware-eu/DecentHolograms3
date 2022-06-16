package eu.decentsoftware.holograms.api.component.common;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for components that can have actions.
 *
 * @author d0by
 */
public interface IActionable {

    /**
     * Get the action holder for this component.
     *
     * @return The action holder for this component.
     */
    @NotNull
    ActionHolder getActions();

}

package eu.decent.holograms.api.component.common;

import eu.decent.holograms.api.actions.ActionHolder;

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
    ActionHolder getActions();

}

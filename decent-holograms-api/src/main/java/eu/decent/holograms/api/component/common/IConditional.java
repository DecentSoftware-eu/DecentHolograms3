package eu.decent.holograms.api.component.common;

import eu.decent.holograms.api.conditions.ConditionHolder;

/**
 * Interface for components that can have conditions.
 *
 * @author d0by
 */
public interface IConditional {

    /**
     * Get the condition holder for this component.
     *
     * @return The condition holder for this component.
     */
    ConditionHolder getConditions();

}

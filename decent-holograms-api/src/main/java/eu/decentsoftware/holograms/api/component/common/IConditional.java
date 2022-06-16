package eu.decentsoftware.holograms.api.component.common;

import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    ConditionHolder getConditions();

}

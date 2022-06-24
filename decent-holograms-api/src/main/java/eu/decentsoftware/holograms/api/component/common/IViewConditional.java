package eu.decentsoftware.holograms.api.component.common;

import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for components that can have view conditions.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface IViewConditional {

    /**
     * Get the view condition holder for this component.
     *
     * @return The view condition holder for this component.
     */
    @NotNull
    ConditionHolder getViewConditionHolder();

}

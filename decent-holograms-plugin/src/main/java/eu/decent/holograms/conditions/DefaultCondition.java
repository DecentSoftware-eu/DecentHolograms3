package eu.decent.holograms.conditions;

import eu.decent.holograms.api.actions.ActionHolder;
import eu.decent.holograms.api.conditions.Condition;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Condition. We can then check whether the Condition is met.
 */
@Getter
@Setter
public abstract class DefaultCondition implements Condition {

    protected boolean inverted;
    protected boolean required;
    protected ActionHolder metActions;
    protected ActionHolder notMetActions;

    /**
     * Create a new instance of {@link DefaultCondition}.
     */
    public DefaultCondition() {
        this(false);
    }

    /**
     * Create a new instance of {@link DefaultCondition}.
     *
     * @param inverted true if the condition should be inverted when checked, false otherwise.
     */
    public DefaultCondition(boolean inverted) {
        this.inverted = inverted;
        this.required = true;
    }

}

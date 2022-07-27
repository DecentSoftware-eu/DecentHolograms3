package eu.decentsoftware.holograms.conditions;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.conditions.Condition;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a Condition. We can then check whether the Condition is met.
 */
@Getter
@Setter
public abstract class DefaultCondition implements Condition {

    protected boolean inverted;
    protected boolean required;
    protected @Nullable ActionHolder metActions;
    protected @Nullable ActionHolder notMetActions;

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

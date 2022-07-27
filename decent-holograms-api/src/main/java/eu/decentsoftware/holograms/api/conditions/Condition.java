package eu.decentsoftware.holograms.api.conditions;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface represents a condition.
 *
 * @author d0by
 */
public interface Condition {

    /**
     * Check whether this {@link Condition} is met.
     *
     * @param profile The profile to check this condition for.
     * @return true if the condition is met, false otherwise.
     */
    boolean check(@NotNull Profile profile);

    /**
     * Check whether this {@link Condition} is inverted.
     *
     * @return true if this condition is inverted, false otherwise.
     */
    boolean isInverted();

    /**
     * Set the {@link Condition} to be inverted.
     *
     * @param inverted true if the condition should be inverted, false otherwise.
     */
    void setInverted(boolean inverted);

    /**
     * Check whether this {@link Condition} is required.
     *
     * @return true if this condition is required, false otherwise.
     */
    boolean isRequired();

    /**
     * Set the {@link Condition} to be required.
     *
     * @param required true if the condition should be required, false otherwise.
     */
    void setRequired(boolean required);

    /**
     * Get the actions that should be executed if this {@link Condition} is met.
     *
     * @return The actions.
     */
    @Nullable
    ActionHolder getMetActions();

    /**
     * Set the actions that should be executed if this {@link Condition} is met.
     *
     * @param actions The actions.
     */
    void setMetActions(@Nullable ActionHolder actions);

    /**
     * Get the actions that should be executed if this {@link Condition} is not met.
     *
     * @return The actions.
     */
    @Nullable
    ActionHolder getNotMetActions();

    /**
     * Set the actions that should be executed if this {@link Condition} is not met.
     *
     * @param actions The actions.
     */
    void setNotMetActions(@Nullable ActionHolder actions);

}

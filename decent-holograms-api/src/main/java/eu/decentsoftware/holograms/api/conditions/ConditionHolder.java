package eu.decentsoftware.holograms.api.conditions;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class represents a holder for conditions.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface ConditionHolder {

    /**
     * Checks all Conditions stored in this holder. This method also executes
     * all 'met' or 'not met' actions of the checked conditions.
     *
     * @param profile Profile of the player for whom we want to check the conditions.
     * @return true if all the conditions are fulfilled, false otherwise.
     */
    default boolean check(@NotNull Profile profile) {
        boolean success = true;
        for (Condition condition : getConditions()) {
            // Check and flip if inverted.
            boolean fulfilled = condition.isInverted() != condition.check(profile);
            ActionHolder actions;
            if (!fulfilled) {
                // Not met
                if ((actions = condition.getNotMetActions()) != null) {
                    // Execute 'not met' actions if any.
                    actions.execute(profile);
                }
                if (condition.isRequired()) {
                    // Not all required conditions are fulfilled.
                    success = false;
                }
            } else if ((actions = condition.getMetActions()) != null) {
                // Execute 'met' actions if any.
                actions.execute(profile);
            }
        }
        // All conditions are fulfilled.
        return success;
    }

    /**
     * Add the given condition to this holder.
     *
     * @param condition The condition.
     */
    void addCondition(@NotNull Condition condition);

    /**
     * Remove the given condition from this holder.
     *
     * @param condition The condition.
     */
    void removeCondition(@NotNull Condition condition);

    /**
     * Remove the condition at the given index from this holder.
     *
     * @param index The index.
     */
    void removeCondition(int index);

    /**
     * Remove all conditions from this holder.
     */
    void clearConditions();

    /**
     * Get all conditions in this holder.
     *
     * @return All conditions.
     */
    @NotNull
    List<Condition> getConditions();

}

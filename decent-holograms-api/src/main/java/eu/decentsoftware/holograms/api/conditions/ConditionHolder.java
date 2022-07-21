package eu.decentsoftware.holograms.api.conditions;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a holder for conditions.
 *
 * @author d0by
 */
public abstract class ConditionHolder extends DList<Condition> {

    /**
     * Checks all Conditions stored in this holder. This method also executes
     * all 'met' or 'not met' actions of the checked conditions.
     *
     * @param profile Profile of the player for whom we want to check the conditions.
     * @return true if all the conditions are fulfilled, false otherwise.
     */
    public boolean check(@NotNull Profile profile) {
        boolean success = true;
        for (Condition condition : this) {
            // Check and flip if inverted.
            boolean fulfilled = condition.isInverted() != condition.check(profile);
            ActionHolder actions;
            if (!fulfilled) {
                // Not met
                if ((actions = condition.getNotMetActions()) != null && !actions.getActions().isEmpty()) {
                    // Execute 'not met' actions if any.
                    actions.execute(profile);
                }
                if (condition.isRequired()) {
                    // Not all required conditions are fulfilled.
                    success = false;
                }
            } else if ((actions = condition.getMetActions()) != null && !actions.getActions().isEmpty()) {
                // Execute 'met' actions if any.
                actions.execute(profile);
            }
        }
        // All conditions are fulfilled.
        return success;
    }

    /**
     * Load all conditions from a configuration section.
     *
     * @param config Configuration section.
     */
    public abstract void load(@NotNull ConfigurationSection config);

}

package eu.decentsoftware.holograms.conditions;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.api.conditions.Condition;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;

public class DefaultConditionHolder extends ConditionHolder {

    @Override
    public boolean check(@NotNull Profile profile) {
        boolean success = true;
        for (Condition condition : this) {
            // Check and flip if inverted.
            boolean fulfilled = condition.isInverted() != condition.check(profile);
            ActionHolder actions;
            if (!fulfilled) {
                // Not met
                if ((actions = condition.getNotMetActions()) != null && actions.isNotEmpty()) {
                    // Execute 'not met' actions if any.
                    actions.execute(profile);
                }
                if (condition.isRequired()) {
                    // Not all required conditions are fulfilled.
                    success = false;
                }
            } else if ((actions = condition.getMetActions()) != null && actions.isNotEmpty()) {
                // Execute 'met' actions if any.
                actions.execute(profile);
            }
        }
        // All conditions are fulfilled.
        return success;
    }

}

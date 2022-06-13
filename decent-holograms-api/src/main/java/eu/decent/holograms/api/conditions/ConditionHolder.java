package eu.decent.holograms.api.conditions;

import eu.decent.holograms.api.profile.Profile;
import eu.decent.holograms.api.utils.collection.DList;
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
    public abstract boolean check(@NotNull Profile profile);

}

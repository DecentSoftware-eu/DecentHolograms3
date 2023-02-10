/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.conditions;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.profile.Profile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a holder for conditions.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ConditionHolder {

    private final @NotNull List<Condition> conditions;

    public ConditionHolder() {
        this(new ArrayList<>());
    }

    public ConditionHolder(@NotNull List<Condition> conditions) {
        this.conditions = conditions;
    }

    /**
     * Checks all Conditions stored in this holder. This method also executes
     * all 'met' or 'not met' actions of the checked conditions.
     *
     * @param profile Profile of the player for whom we want to check the conditions.
     * @return true if all the conditions are fulfilled, false otherwise.
     */
    public boolean check(@NotNull Profile profile) {
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
    public void addCondition(@NotNull Condition condition) {
        this.conditions.add(condition);
    }

    /**
     * Remove the given condition from this holder.
     *
     * @param condition The condition.
     */
    public void removeCondition(@NotNull Condition condition) {
        this.conditions.remove(condition);
    }

    /**
     * Remove the condition at the given index from this holder.
     *
     * @param index The index.
     */
    public void removeCondition(int index) {
        this.conditions.remove(index);
    }

    /**
     * Remove all conditions from this holder.
     */
    public void clearConditions() {
        this.conditions.clear();
    }

    /**
     * Get all conditions in this holder. The returned list is immutable.
     *
     * @return Immutable list of conditions.
     */
    @NotNull
    public List<Condition> getConditions() {
        return ImmutableList.copyOf(conditions);
    }

}

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
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a holder for conditions. It stores a list of conditions and provides methods
 * to modify this list or to check all conditions.
 *
 * @author d0by
 * @see Condition
 * @since 3.0.0
 */
public class ConditionHolder {

    private final @NonNull List<Condition> conditions;

    public ConditionHolder() {
        this(new ArrayList<>());
    }

    @Contract(pure = true)
    public ConditionHolder(@NonNull List<Condition> conditions) {
        this.conditions = conditions;
    }

    /**
     * Checks all Conditions stored in this holder. This method also executes
     * all 'not met' actions of the checked conditions.
     *
     * @param player The player for whom we want to check the conditions.
     * @return true if all the conditions are fulfilled, false otherwise.
     */
    public boolean check(@NotNull Player player) {
        for (Condition condition : getConditions()) {
            // Check and flip if inverted.
            boolean fulfilled = condition.isInverted() != condition.check(player);
            if (fulfilled) {
                continue;
            }

            condition.getNotMetActions().ifPresent(actions -> actions.execute(player));

            if (condition.isRequired()) {
                return false;
            }
        }
        // All conditions are fulfilled.
        return true;
    }

    public void addCondition(@NonNull Condition condition) {
        this.conditions.add(condition);
    }

    public void removeCondition(@NonNull Condition condition) {
        this.conditions.remove(condition);
    }

    public void removeCondition(int index) {
        this.conditions.remove(index);
    }

    public void clearConditions() {
        this.conditions.clear();
    }

    public boolean isEmpty() {
        return this.conditions.isEmpty();
    }

    @NotNull
    public List<Condition> getConditions() {
        return ImmutableList.copyOf(this.conditions);
    }

}

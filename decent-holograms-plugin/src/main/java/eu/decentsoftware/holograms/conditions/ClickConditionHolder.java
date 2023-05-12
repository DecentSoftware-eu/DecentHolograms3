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
import eu.decentsoftware.holograms.api.hologram.component.ClickType;
import eu.decentsoftware.holograms.profile.Profile;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a holder for conditions. It stores a list of conditions and
 * provides methods to modify this list or to check all conditions.
 *
 * @author d0by
 * @see Condition
 * @since 3.0.0
 */
public class ClickConditionHolder {

    private final @NotNull Map<ClickType, List<Condition>> conditions = new EnumMap<>(ClickType.class);

    /**
     * Create a new {@link ClickConditionHolder} with no conditions. You can add conditions later.
     */
    public ClickConditionHolder() {
        this(new EnumMap<>(ClickType.class));
    }

    /**
     * Create a new {@link ClickConditionHolder} with the given conditions.
     *
     * @param conditions The list of conditions.
     */
    @Contract(pure = true)
    public ClickConditionHolder(@NotNull Map<ClickType, List<Condition>> conditions) {
        for (ClickType clickType : ClickType.values()) {
            this.conditions.put(clickType, conditions.getOrDefault(clickType, new ArrayList<>()));
        }
    }

    /**
     * Checks all Conditions stored in this holder. This method also executes
     * all 'met' or 'not met' actions of the checked conditions.
     *
     * @param clickType The click type.
     * @param profile   Profile of the player for whom we want to check the conditions.
     * @return true if all the conditions are fulfilled, false otherwise.
     */
    public boolean check(@NonNull ClickType clickType, @NotNull Profile profile) {
        for (Condition condition : getConditions(clickType)) {
            // Check and flip if inverted.
            boolean fulfilled = condition.isInverted() != condition.check(profile);
            if (fulfilled) {
                continue;
            }

            condition.getNotMetActions().ifPresent(actions -> actions.execute(profile));

            if (condition.isRequired()) {
                return false;
            }
        }
        // All conditions are fulfilled.
        return true;
    }

    /**
     * Add the given condition to this holder.
     *
     * @param clickType The click type.
     * @param condition The condition.
     * @see Condition
     */
    public void addCondition(@NonNull ClickType clickType, @NotNull Condition condition) {
        this.conditions.get(clickType).add(condition);
    }

    /**
     * Remove the given condition from this holder.
     *
     * @param clickType The click type.
     * @param condition The condition.
     * @see Condition
     */
    public void removeCondition(@NonNull ClickType clickType, @NotNull Condition condition) {
        this.conditions.get(clickType).remove(condition);
    }

    /**
     * Remove the condition at the given index from this holder.
     *
     * @param clickType The click type.
     * @param index     The index.
     * @see Condition
     */
    public void removeCondition(@NonNull ClickType clickType, int index) {
        this.conditions.get(clickType).remove(index);
    }

    /**
     * Remove all conditions from this holder.
     *
     * @param clickType The click type.
     * @see Condition
     */
    public void clearConditions(@NonNull ClickType clickType) {
        this.conditions.get(clickType).clear();
    }

    /**
     * Check if this holder is empty.
     *
     * @param clickType The click type.
     * @return True if this holder is empty, false otherwise.
     * @see Condition
     */
    public boolean isEmpty(@NonNull ClickType clickType) {
        return this.conditions.get(clickType).isEmpty();
    }

    /**
     * Get all conditions in this holder. The returned list is immutable.
     *
     * @param clickType The click type.
     * @return Immutable list of conditions.
     * @see Condition
     */
    @NotNull
    public List<Condition> getConditions(@NonNull ClickType clickType) {
        return ImmutableList.copyOf(this.conditions.get(clickType));
    }

}

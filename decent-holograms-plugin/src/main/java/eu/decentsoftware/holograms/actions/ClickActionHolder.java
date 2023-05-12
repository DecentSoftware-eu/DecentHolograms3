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

package eu.decentsoftware.holograms.actions;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.api.hologram.component.ClickType;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a holder for actions with click types. It stores a list of actions
 * for each click type and provides methods to modify this list or to execute all actions.
 *
 * @author d0by
 * @see Action
 * @since 3.0.0
 */
public class ClickActionHolder {

    private final @NonNull Map<ClickType, List<Action>> actions = new EnumMap<>(ClickType.class);

    public ClickActionHolder() {
        this(new EnumMap<>(ClickType.class));
    }

    @Contract(pure = true)
    public ClickActionHolder(@NonNull Map<ClickType, List<Action>> actions) {
        for (ClickType clickType : ClickType.values()) {
            this.actions.put(clickType, actions.getOrDefault(clickType, new ArrayList<>()));
        }
    }

    /**
     * Execute all Actions in this holder for the given {@link Profile}.
     *
     * @param clickType The click type.
     * @param profile   The profile.
     */
    public void execute(@NonNull ClickType clickType, @NonNull Profile profile) {
        for (Action action : getActions(clickType)) {
            // Check the chance
            if (!action.checkChance()) {
                continue;
            }
            // Execute with delay if needed
            long delay = action.getDelay();
            if (delay > 0) {
                SchedulerUtil.run(() -> action.execute(profile), delay);
            } else {
                SchedulerUtil.run(() -> action.execute(profile));
            }
        }
    }

    /**
     * Add the given action to this holder.
     *
     * @param clickType The click type.
     * @param action    The action.
     * @see Action
     */
    public void addAction(@NonNull ClickType clickType, @NonNull Action action) {
        this.actions.get(clickType).add(action);
    }

    /**
     * Remove the given action from this holder.
     *
     * @param clickType The click type.
     * @param action    The action.
     * @see Action
     */
    public void removeAction(@NonNull ClickType clickType, @NonNull Action action) {
        this.actions.get(clickType).remove(action);
    }

    /**
     * Remove the action at the given index from this holder.
     *
     * @param clickType The click type.
     * @param index     The index.
     * @see Action
     */
    public void removeAction(@NonNull ClickType clickType, int index) {
        this.actions.get(clickType).remove(index);
    }

    /**
     * Remove all actions from this holder.
     *
     * @param clickType The click type.
     * @see Action
     */
    public void clearActions(@NonNull ClickType clickType) {
        this.actions.get(clickType).clear();
    }

    /**
     * Check if this holder is empty.
     *
     * @param clickType The click type.
     * @return True if this holder is empty, false otherwise.
     */
    public boolean isEmpty(@NonNull ClickType clickType) {
        return this.actions.get(clickType).isEmpty();
    }

    /**
     * Get all actions in this holder. The returned list is immutable.
     *
     * @param clickType The click type.
     * @return Immutable list of all actions.
     */
    @NotNull
    public List<Action> getActions(@NonNull ClickType clickType) {
        return ImmutableList.copyOf(this.actions.get(clickType));
    }

}

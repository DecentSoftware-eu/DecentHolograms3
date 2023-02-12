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
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a holder for actions.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ActionHolder {

    private final @NotNull List<Action> actions;

    public ActionHolder() {
        this(new ArrayList<>());
    }

    @Contract(pure = true)
    public ActionHolder(@NotNull List<Action> actions) {
        this.actions = actions;
    }

    /**
     * Execute all Actions in this holder for the given {@link Profile}.
     *
     * @param profile The profile.
     */
    public void execute(@NotNull Profile profile) {
        for (Action action : getActions()) {
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
     * @param action The action.
     * @see Action
     */
    public void addAction(@NotNull Action action) {
        this.actions.add(action);
    }

    /**
     * Remove the given action from this holder.
     *
     * @param action The action.
     * @see Action
     */
    public void removeAction(@NotNull Action action) {
        this.actions.remove(action);
    }

    /**
     * Remove the action at the given index from this holder.
     *
     * @param index The index.
     * @see Action
     */
    public void removeAction(int index) {
        this.actions.remove(index);
    }

    /**
     * Remove all actions from this holder.
     *
     * @see Action
     */
    public void clearActions() {
        this.actions.clear();
    }

    /**
     * Get all actions in this holder. The returned list is immutable.
     *
     * @return Immutable list of all actions.
     */
    @NotNull
    public List<Action> getActions() {
        return ImmutableList.copyOf(actions);
    }

}

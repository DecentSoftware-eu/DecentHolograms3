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
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a holder for actions. It stores a list of actions and provides methods
 * to modify this list or to execute all actions.
 *
 * @author d0by
 * @see Action
 * @since 3.0.0
 */
public class ActionHolder {

    private final List<Action> actions;

    public ActionHolder() {
        this(new ArrayList<>());
    }

    @Contract(pure = true)
    public ActionHolder(@NonNull List<Action> actions) {
        this.actions = actions;
    }

    public void execute(@NonNull Player player) {
        for (Action action : getActions()) {
            // Check the chance
            if (!action.checkChance()) {
                continue;
            }
            // Execute with delay if needed
            long delay = action.getDelay();
            if (delay > 0) {
                SchedulerUtil.run(() -> action.execute(player), delay);
            } else {
                SchedulerUtil.run(() -> action.execute(player));
            }
        }
    }

    public void addAction(@NonNull Action action) {
        this.actions.add(action);
    }

    public void removeAction(@NonNull Action action) {
        this.actions.remove(action);
    }

    public void removeAction(int index) {
        this.actions.remove(index);
    }

    public void clearActions() {
        this.actions.clear();
    }

    public boolean isEmpty() {
        return this.actions.isEmpty();
    }

    @NonNull
    public List<Action> getActions() {
        return ImmutableList.copyOf(this.actions);
    }

}

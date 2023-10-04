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
import eu.decentsoftware.holograms.api.hologram.click.ClickType;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import lombok.NonNull;
import org.bukkit.entity.Player;
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

    public void execute(@NonNull ClickType clickType, @NonNull Player player) {
        for (Action action : getActions(clickType)) {
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

    public void addAction(@NonNull ClickType clickType, @NonNull Action action) {
        this.actions.get(clickType).add(action);
    }

    public void removeAction(@NonNull ClickType clickType, @NonNull Action action) {
        this.actions.get(clickType).remove(action);
    }

    public void removeAction(@NonNull ClickType clickType, int index) {
        this.actions.get(clickType).remove(index);
    }

    public void clearActions(@NonNull ClickType clickType) {
        this.actions.get(clickType).clear();
    }

    public boolean isEmpty(@NonNull ClickType clickType) {
        return this.actions.get(clickType).isEmpty();
    }

    @NotNull
    public List<Action> getActions(@NonNull ClickType clickType) {
        return ImmutableList.copyOf(this.actions.get(clickType));
    }

}

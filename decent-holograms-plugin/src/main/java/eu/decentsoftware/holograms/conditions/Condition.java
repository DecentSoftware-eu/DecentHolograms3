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

import eu.decentsoftware.holograms.actions.ActionHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This class represents a Condition. We can then check whether the Condition is met.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@Setter
public abstract class Condition {

    protected boolean inverted;
    protected boolean required;
    protected @Nullable ActionHolder notMetActions;

    /**
     * Create a new instance of {@link Condition}.
     */
    @Contract(pure = true)
    protected Condition() {
        this(false);
    }

    /**
     * Create a new instance of {@link Condition}.
     *
     * @param inverted true if the condition should be inverted when checked, false otherwise.
     */
    @Contract(pure = true)
    protected Condition(boolean inverted) {
        this.inverted = inverted;
        this.required = true;
    }

    /**
     * Get the {@link ActionHolder} that should be executed if the condition is not met.
     *
     * @return Optional of the {@link ActionHolder}.
     */
    public Optional<ActionHolder> getNotMetActions() {
        return Optional.ofNullable(notMetActions);
    }

    /**
     * Check whether this {@link Condition} is met.
     *
     * @param player The player to check this condition for.
     * @return true if the condition is met, false otherwise.
     */
    public abstract boolean check(@NotNull Player player);

}

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

import eu.decentsoftware.holograms.profile.Profile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a specific action. It can be instantiated,
 * then executed as many times as needed. This class holds all parameters
 * of the action and uses them to properly execute the action.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class Action {

    protected final long delay;
    protected final double chance;

    /**
     * Create a new instance of {@link Action} with default values.
     */
    @Contract(pure = true)
    protected Action() {
        this(0, -1.0);
    }

    /**
     * Create a new instance of {@link Action} with the given delay and chance.
     *
     * @param delay  The delay.
     * @param chance The chance.
     */
    @Contract(pure = true)
    protected Action(long delay, double chance) {
        this.delay = delay;
        this.chance = chance;
    }

    /**
     * Execute this action for the given {@link Profile}.
     *
     * @param profile The profile.
     */
    public abstract void execute(@NotNull Profile profile);

    /**
     * Check if this action should be executed according to chance.
     *
     * @return True if this action should be executed.
     */
    public boolean checkChance() {
        if (chance < 0) {
            return true;
        }
        double rand = Math.random();
        return rand < chance;
    }

    /**
     * Get the delay of this action. This is the amount of ticks
     * that should be waited before executing this action.
     *
     * @return The delay of this action.
     */
    public long getDelay() {
        return delay;
    }

    /**
     * Get the chance of this action. This is the chance that this action
     * should be executed.
     *
     * @return The chance of this action.
     */
    public double getChance() {
        return chance;
    }

}

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

package eu.decentsoftware.holograms.internal;

import org.jetbrains.annotations.Contract;

/**
 * This enum represents the action execution strategy of a hologram. This
 * is used to decide which component's actions to execute first.
 *
 * @author d0by
 * @since 3.0.0
 */
public enum ActionExecutionStrategy {
    /**
     * Execute actions from top to bottom. This will first execute actions on the top
     * level (hologram page) and then on the bottom level (hologram line).
     */
    TOP_TO_BOTTOM,
    /**
     * Execute actions from bottom to top. This will first execute actions on the bottom
     * level (hologram line) and then on the top level (hologram page).
     */
    BOTTOM_TO_TOP,
    /**
     * Execute actions only on the bottom level (hologram line). If there are no actions
     * on the bottom level, execute actions on the top level (hologram page).
     */
    ONLY_BOTTOM,
    /**
     * Execute actions only on the top level (hologram page). If there are no actions
     * on the top level, execute actions on the bottom level (hologram line).
     */
    ONLY_TOP;

    /**
     * Check if the action execution strategy is bottom first.
     *
     * @return True if the action execution strategy is bottom first.
     */
    @Contract(pure = true)
    public boolean isBottomFirst() {
        return this == BOTTOM_TO_TOP || this == ONLY_BOTTOM;
    }

}

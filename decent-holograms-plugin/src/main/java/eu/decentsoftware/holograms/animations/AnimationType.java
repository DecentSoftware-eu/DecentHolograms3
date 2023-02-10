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

package eu.decentsoftware.holograms.animations;

import org.jetbrains.annotations.NotNull;

/**
 * This enum represents the different types of animations that can be used. The
 * animation type determines how the hologram will be animated, e.g. in which
 * order will the steps be executed.
 *
 * @author d0by
 */
public enum AnimationType {
    /**
     * This animation type will execute the steps in the order they are added.
     */
    ASCEND,
    /**
     * This animation type will execute the steps in the reverse order they are
     * added.
     */
    DESCEND,
    /**
     * This animation type will execute the steps in the order they are added,
     * and then in the reverse order.
     */
    ASCEND_DESCEND,
    /**
     * This animation type will execute the steps in random order.
     */
    RANDOM,

    INTERNAL;

    /**
     * Get a {@link AnimationType} from a string.
     *
     * @param name The name of the animation type.
     * @return The animation type or {@link #ASCEND} by default.
     */
    @NotNull
    public static AnimationType getByName(@NotNull String name) {
        for (AnimationType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return ASCEND;
    }
}

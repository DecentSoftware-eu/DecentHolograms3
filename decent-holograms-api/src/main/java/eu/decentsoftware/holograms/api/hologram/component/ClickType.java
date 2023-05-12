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

package eu.decentsoftware.holograms.api.hologram.component;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * This enum represents the type of click on a hologram.
 *
 * @author d0by
 * @see ClickHandler
 * @since 3.0.0
 */
public enum ClickType {
    LEFT,
    RIGHT,
    SHIFT_LEFT,
    SHIFT_RIGHT;

    /**
     * Convert a {@link org.bukkit.event.inventory.ClickType} to a {@link ClickType}.
     *
     * @param clickType The {@link org.bukkit.event.inventory.ClickType} to convert.
     * @return The converted {@link ClickType} or null if the click type is not supported.
     */
    @Nullable
    @Contract(pure = true)
    public static ClickType fromInventoryClickType(@NonNull org.bukkit.event.inventory.ClickType clickType) {
        switch (clickType) {
            case LEFT:
                return ClickType.LEFT;
            case RIGHT:
                return ClickType.RIGHT;
            case SHIFT_LEFT:
                return ClickType.SHIFT_LEFT;
            case SHIFT_RIGHT:
                return ClickType.SHIFT_RIGHT;
            default:
                return null;
        }
    }

}

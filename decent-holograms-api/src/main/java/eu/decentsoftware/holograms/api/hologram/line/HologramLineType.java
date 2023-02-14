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

package eu.decentsoftware.holograms.api.hologram.line;

/**
 * This enum contains all the possible line types. The line type determines
 * how the line will be displayed for the player.
 *
 * @author d0by
 * @since 3.0.0
 */
public enum HologramLineType {
    /**
     * Just a normal text line.
     */
    TEXT,
    /**
     * A line displaying an ItemStack.
     */
    ICON,
    /**
     * A line displaying an ItemStack that doesn't move.
     */
    STATIC_ICON,
    /**
     * A line displaying an ItemStack as a Head of an ArmorStand.
     */
    HEAD,
    /**
     * A line displaying an ItemStack as a Head of a small ArmorStand.
     */
    SMALL_HEAD,
    /**
     * A line displaying an Entity.
     */
    ENTITY,
    /**
     * A line, made of multiple lines, displaying an image.
     */
    IMAGE,
    UNKNOWN
}

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

package eu.decentsoftware.holograms.api.component.hologram;

import org.jetbrains.annotations.NotNull;

/**
 * This class holds all the settings for a hologram.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramSettings {

    /**
     * Copy settings from the given {@link HologramSettings}.
     *
     * @param settings The settings to copy from.
     */
    void set(@NotNull HologramSettings settings);

    /**
     * Check if this hologram is enabled. If this hologram is disabled, it will
     * not be visible to players.
     *
     * @return True if this hologram is enabled.
     */
    boolean isEnabled();

    /**
     * Set if this hologram is enabled. If this hologram is disabled, it will
     * not be visible to players.
     *
     * @param enabled True if this hologram is enabled.
     */
    void setEnabled(boolean enabled);

    /**
     * Check if the hologram is interactive. If this hologram is interactive, it will
     * be clickable by players.
     *
     * @return True if the hologram is interactive.
     */
    boolean isInteractive();

    /**
     * Set if the hologram is interactive. If this hologram is interactive, it will
     * be clickable by players.
     *
     * @param interactive True if the hologram is interactive.
     */
    void setInteractive(boolean interactive);

    /**
     * Get if the hologram is persistent, meaning it will be saved and loaded
     * when the server is restarted.
     *
     * @return True if the hologram is persistent, false otherwise.
     */
    boolean isPersistent();

    /**
     * Set if the hologram is persistent, meaning it will be saved and loaded
     * when the server is restarted.
     *
     * @param persistent True if the hologram is persistent, false otherwise.
     */
    void setPersistent(boolean persistent);

    /**
     * Get if the origin of the hologram is down, meaning the hologram will
     * be built from the bottom of the hologram. If true, the location of the
     * hologram will be at the bottom of the hologram.
     *
     * @return True if the origin is down, false otherwise.
     */
    boolean isDownOrigin();

    /**
     * Set if the origin of the hologram is down, meaning the hologram will
     * be built from the bottom of the hologram. If true, the location of the
     * hologram will be at the bottom of the hologram.
     *
     * @param downOrigin True if the origin is down, false otherwise.
     */
    void setDownOrigin(boolean downOrigin);

    /**
     * Get if the hologram is editable in-game.
     *
     * @return True if the hologram is editable, false otherwise.
     */
    boolean isEditable();

    /**
     * Set if the hologram is editable in-game.
     *
     * @param editable True if the hologram is editable, false otherwise.
     */
    void setEditable(boolean editable);

    /**
     * Get if the hologram should rotate horizontally. If true, the hologram will
     * be rotated to face the player horizontally. Meaning player will be able to
     * see the hologram equally from all sides even with line offsets.
     *
     * @return True if the hologram should rotate horizontally, false otherwise.
     */
    boolean isRotateHorizontal();

    /**
     * Set if the hologram should rotate horizontally. If true, the hologram will
     * be rotated to face the player horizontally. Meaning player will be able to
     * see the hologram equally from all sides even with line offsets.
     *
     * @param rotateHorizontal True if the hologram should rotate horizontally, false otherwise.
     */
    void setRotateHorizontal(boolean rotateHorizontal);

    /**
     * Get if the hologram should rotate vertically. If true, the hologram will
     * be rotated to face the player vertically. Meaning player will be able to
     * see the hologram even from above.
     *
     * @return True if the hologram has fixed rotation, false otherwise.
     */
    boolean isRotateVertical();

    /**
     * Set if the hologram should rotate vertically. If true, the hologram will
     * be rotated to face the player vertically. Meaning player will be able to
     * see the hologram even from above.
     *
     * @param rotateVertical True if the hologram should rotate vertically, false otherwise.
     */
    void setRotateVertical(boolean rotateVertical);

    /**
     * Get if the hologram should rotate the heads of entities in it. If true,
     * the hologram will rotate all heads and entities to face the player.
     *
     * @return True if the hologram should rotate the heads, false otherwise.
     */
    boolean isRotateHeads();

    /**
     * Set if the hologram should rotate the heads of entities in it. If true,
     * the hologram will rotate all heads and entities to face the player.
     *
     * @param rotateHeads True if the hologram should rotate the heads, false otherwise.
     */
    void setRotateHeads(boolean rotateHeads);

    /**
     * Get the view distance of the hologram. This is the range in which the
     * hologram will be visible.
     *
     * @return The view distance of the hologram.
     */
    int getViewDistance();

    /**
     * Set the view distance of the hologram. This is the range in which the
     * hologram will be visible.
     *
     * @param viewDistance The view distance of the hologram.
     */
    void setViewDistance(int viewDistance);

    /**
     * Get the update distance of the hologram. This is the range in which the
     * hologram will be updated.
     *
     * @return The update distance of the hologram.
     */
    int getUpdateDistance();

    /**
     * Set the update distance of the hologram. This is the range in which the
     * hologram will be updated.
     *
     * @param updateDistance The update distance of the hologram.
     */
    void setUpdateDistance(int updateDistance);

    /**
     * Get the update interval of the hologram. This is the amount of ticks
     * between each update.
     *
     * @return The update interval of the hologram.
     */
    int getUpdateInterval();

    /**
     * Set the update interval of the hologram. This is the amount of ticks
     * between each update.
     *
     * @param updateInterval The update interval of the hologram.
     */
    void setUpdateInterval(int updateInterval);

    /**
     * Get if the hologram is updating.
     *
     * @return True if the hologram is updating, false otherwise.
     */
    boolean isUpdating();

    /**
     * Set if the hologram is updating.
     *
     * @param updating True if the hologram is updating, false otherwise.
     */
    void setUpdating(boolean updating);

}

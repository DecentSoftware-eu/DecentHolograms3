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

package eu.decentsoftware.holograms.api.hologram;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a hologram line. A line is a collection of components
 * that can be moved in the world. A line can be added to a {@link HologramPage}.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface HologramLine {

    /**
     * Destroy this line. This method makes this line unusable.
     * <p>
     * This line should not be referenced after this method is called.
     */
    void destroy();

    /**
     * Get the raw content of this line.
     *
     * @return The raw content of this line.
     */
    @Nullable
    String getContent();

    /**
     * Set the raw content of this line. This method also parses the content and
     * updates the line accordingly.
     *
     * @param content The raw content of this line.
     */
    void setContent(@NonNull String content);

    /**
     * Get the height of this line in blocks.
     *
     * @return The height of this line.
     */
    double getBlockHeight();

    /**
     * Get the width of this line in blocks.
     *
     * @param player The player to get the width for.
     * @return The width of this line.
     */
    double getBlockWidth(@NonNull Player player);

    /**
     * Get the parent {@link HologramPage} of this line.
     *
     * @return The parent {@link HologramPage} of this line.
     */
    @NonNull
    HologramPage getParent();

    /**
     * Get the type of this line.
     *
     * @return The type of this line.
     * @see HologramLineType
     */
    @NonNull
    HologramLineType getType();

    /**
     * Get the settings of this line.
     *
     * @return The settings of this line.
     * @see HologramLineSettings
     */
    @NonNull
    HologramLineSettings getSettings();

}

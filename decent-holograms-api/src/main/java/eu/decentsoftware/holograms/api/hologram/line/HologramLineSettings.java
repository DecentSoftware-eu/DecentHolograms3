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
 * This class represents the settings of a line.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramLineSettings {

    /**
     * Get if the line is updating, meaning it's contents will be updated every
     * update interval.
     *
     * @return True if the line is updating, false otherwise.
     */
    boolean isUpdating();

    /**
     * Set if the line is updating, meaning it's contents will be updated every
     * update interval.
     *
     * @param updating True if the line is updating, false otherwise.
     */
    void setUpdating(boolean updating);

    /**
     * Get the height of the line.
     *
     * @return The height of the line.
     */
    double getHeight();

    /**
     * Set the height of the line.
     *
     * @param height The height of the line.
     */
    void setHeight(double height);

    /**
     * Get the offset X of the line.
     *
     * @return The offset X of the line.
     */
    double getOffsetX();

    /**
     * Set the offset X of the line.
     *
     * @param offsetX The offset X of the line.
     */
    void setOffsetX(double offsetX);

    /**
     * Get the offset Y of the line.
     *
     * @return The offset Y of the line.
     */
    double getOffsetY();

    /**
     * Set the offset Y of the line.
     *
     * @param offsetY The offset Y of the line.
     */
    void setOffsetY(double offsetY);

    /**
     * Get the offset Z of the line.
     *
     * @return The offset Z of the line.
     */
    double getOffsetZ();

    /**
     * Set the offset Z of the line.
     *
     * @param offsetZ The offset Z of the line.
     */
    void setOffsetZ(double offsetZ);

}

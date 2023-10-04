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

package eu.decentsoftware.holograms.utils.color;

import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.Contract;

import java.awt.*;

/**
 * Wrapper for {@link Color} class.
 *
 * @author d0by
 * @since 3.0.0
 */
public class DecentColor {

    private final Color color;

    /**
     * Creates new instance of {@link DecentColor} class.
     *
     * @param hex Hex string representation of color.
     */
    public DecentColor(@NonNull String hex) {
        this(Integer.parseInt(hex, 16));
    }

    /**
     * Creates new instance of {@link DecentColor} class.
     *
     * @param red   Red component of color.
     * @param green Green component of color.
     * @param blue  Blue component of color.
     */
    @Contract(pure = true)
    public DecentColor(int red, int green, int blue) {
        this.color = new Color(red, green, blue);
    }

    /**
     * Creates new instance of {@link DecentColor} class.
     *
     * @param rgb Integer representation of color.
     */
    @Contract(pure = true)
    public DecentColor(int rgb) {
        this.color = new Color(rgb);
    }

    /**
     * Returns hex representation of color.
     *
     * @return Hex representation of color.
     */
    public String toHex() {
        return DecentColorAPI.rgbToHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Returns {@link ChatColor} representation of color.
     *
     * @return {@link ChatColor} representation of color.
     */
    public ChatColor toChatColor() {
        return DecentColorAPI.getClosestColor(color);
    }

}

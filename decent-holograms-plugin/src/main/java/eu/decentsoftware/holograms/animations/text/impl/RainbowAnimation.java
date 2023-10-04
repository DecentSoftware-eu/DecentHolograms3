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

package eu.decentsoftware.holograms.animations.text.impl;

import eu.decentsoftware.holograms.animations.AnimationType;
import eu.decentsoftware.holograms.animations.text.TextAnimation;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

/**
 * Rainbow text animation cycles through several colors, making the text
 * have a different color every few ticks.
 *
 * @author d0by
 * @see TextAnimation
 * @since 3.0.0
 */
public class RainbowAnimation extends TextAnimation {

    private static final ChatColor[] DEFAULT_COLORS = new ChatColor[] {
            ChatColor.RED,
            ChatColor.GOLD,
            ChatColor.YELLOW,
            ChatColor.GREEN,
            ChatColor.AQUA,
            ChatColor.LIGHT_PURPLE
    };

    public RainbowAnimation() {
        super("rainbow", AnimationType.INTERNAL, DEFAULT_COLORS.length, 2, 0);
    }

    @NonNull
    public String animate(int tick, @Nullable String frameData, String... args) {
        return DEFAULT_COLORS[tick % (DEFAULT_COLORS.length * interval) / interval] + (frameData == null ? "" : frameData);
    }

}

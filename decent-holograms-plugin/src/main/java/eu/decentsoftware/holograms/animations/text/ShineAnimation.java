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

package eu.decentsoftware.holograms.animations.text;

import eu.decentsoftware.holograms.animations.Animation;
import eu.decentsoftware.holograms.animations.AnimationType;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShineAnimation extends Animation {

    public ShineAnimation() {
        super("shine", AnimationType.ASCEND, 0, 60, 0);
    }

    @NotNull
    @Override
    public String animate(int step, @Nullable String string) {
        if (string == null) {
            return "";
        }

        // TODO

        int length = string.length();
        int index = step % length;

        String start = string.substring(0, index);
        String shine = string.substring(index, index + 1);
        String end = string.substring(index + 1);
        String color = ChatColor.getLastColors(start);

        return start + ChatColor.WHITE + shine + color + end;
    }

}

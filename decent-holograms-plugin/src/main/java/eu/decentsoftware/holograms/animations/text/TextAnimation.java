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
import org.jetbrains.annotations.NotNull;

/**
 * This animation is used to animate text.
 *
 * @author d0by
 * @see Animation
 * @see AnimationType
 * @since 3.0.0
 */
public abstract class TextAnimation extends Animation<String> {

    public TextAnimation(@NotNull String name, @NotNull AnimationType type, int totalSteps, int speed, int pause) {
        super(name, type, totalSteps, speed, pause);
    }

}

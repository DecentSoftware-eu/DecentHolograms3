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

import eu.decentsoftware.holograms.animations.AnimationType;
import eu.decentsoftware.holograms.animations.Animation;
import eu.decentsoftware.holograms.animations.AnimationFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomTextAnimation extends Animation {

    protected final @NotNull List<AnimationFrame<?>> frameList;

    public CustomTextAnimation(@NotNull String name, @NotNull AnimationType type, int totalSteps, int speed, int pause) {
        super(name, type, totalSteps, speed, pause);
        this.frameList = new ArrayList<>();
    }

    public CustomTextAnimation(@NotNull String name, @NotNull AnimationType type, int totalSteps, int speed, int pause, @NotNull List<AnimationFrame<?>> frameList) {
        super(name, type, totalSteps, speed, pause);
        this.frameList = frameList;
    }

    /**
     * Get the n-th frame of this animation.
     *
     * @param step The n.
     * @return The n-th frame of this animation.
     */
    public AnimationFrame<?> getFrame(int step) {
        return frameList.get(getActualStep(step));
    }

    @NotNull
    public String animate(int step, @Nullable String string) {
        return getFrame(step).toString();
    }

}

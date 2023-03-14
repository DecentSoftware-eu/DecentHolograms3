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

package eu.decentsoftware.holograms.animations;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a general animation. It is used to animate any object
 * based on the current tick of the animation.
 * <p>
 * There are different implementations of this class for different types of animations.
 *
 * @param <T> The type of the object to animate.
 * @author d0by
 * @see AnimationType
 * @since 3.0.0
 */
@Getter
public abstract class Animation<T> {

    protected final @NotNull String name;
    protected final @NotNull AnimationType type;
    protected final int totalSteps;
    protected final int interval;
    protected final int pause;

    /**
     * Creates a new animation.
     *
     * @param name       the name of the animation
     * @param totalSteps the total number of steps of the animation
     */
    @Contract(pure = true)
    public Animation(@NotNull String name, @NotNull AnimationType type, int totalSteps, int interval, int pause) {
        this.name = name;
        this.type = type;
        this.totalSteps = type == AnimationType.ASCEND_DESCEND ? totalSteps * 2 : totalSteps;
        this.interval = interval;
        this.pause = pause;
    }

    /**
     * Returns the current step of the animation based on the current tick.
     *
     * @param tick      The current tick of animations. (1 tick = 1/20 second)
     * @param frameData The data of the current frame.
     * @param args      The arguments of the animation.
     * @return The current step of the animation.
     */
    @NotNull
    public abstract T animate(int tick, @Nullable T frameData, String... args);

    /**
     * Get the current step of this animation.
     *
     * @param tick The current tick of animations. (1 tick = 1/20 second)
     * @return The current step of this animation.
     */
    protected int getActualStep(int tick) {
        if (totalSteps <= 0) {
            return 0;
        }
        int actualStep = tick / interval;
        int actualPause = pause <= 0 ? 0 : pause / interval;
        int currentStep = actualStep % (totalSteps + actualPause);
        return Math.min(currentStep, totalSteps - 1);
    }

}
package eu.decentsoftware.holograms.animations;

import eu.decentsoftware.holograms.api.animations.Animation;
import eu.decentsoftware.holograms.api.animations.AnimationType;
import org.jetbrains.annotations.NotNull;

public abstract class DefaultAnimation implements Animation {

    protected final @NotNull String name;
    protected final @NotNull AnimationType type;
    protected final int totalSteps;
    protected final int speed;
    protected final int pause;

    /**
     * Creates a new animation.
     *
     * @param name       the name of the animation
     * @param totalSteps the total number of steps of the animation
     */
    public DefaultAnimation(@NotNull String name, @NotNull AnimationType type, int totalSteps, int speed, int pause) {
        this.name = name;
        this.type = type;
        this.totalSteps = totalSteps;
        this.speed = speed;
        this.pause = pause;
    }

    /**
     * Get the current step of this animation.
     *
     * @param step The current tick (global step) of animations.
     * @return The current step of this animation.
     */
    protected int getActualStep(int step) {
        if (totalSteps <= 0) {
            return 0;
        }

        // TODO: rework to FPS
        int actualStep = step / speed;
        // Adapt the pause to speed.
        int actualPause = pause <= 0 ? 0 : pause / speed;
        int currentStep = actualStep % (totalSteps + actualPause);
        return Math.min(currentStep, totalSteps);
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public AnimationType getType() {
        return type;
    }

    @Override
    public int getTotalSteps() {
        return totalSteps;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int getPause() {
        return pause;
    }

}
package eu.decentsoftware.holograms.animations;

import eu.decentsoftware.holograms.api.animations.Animation;
import eu.decentsoftware.holograms.api.animations.AnimationType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an animation.
 */
@Getter
public abstract class AbstractAnimation implements Animation {

    private final String name;
    private final long totalSteps;

    /**
     * Creates a new animation.
     *
     * @param name the name of the animation
     * @param totalSteps the total number of steps of the animation
     */
    public AbstractAnimation(@NotNull String name, long totalSteps) {
        this.name = name;
        this.totalSteps = totalSteps;
    }

    /**
     * Gets the current step of the animation.
     *
     * @return the current step
     */
    protected long getActualStep(long step) {
        return step % totalSteps;
    }

    @Override
    public AnimationType getType() {
        return AnimationType.INTERNAL;
    }
}
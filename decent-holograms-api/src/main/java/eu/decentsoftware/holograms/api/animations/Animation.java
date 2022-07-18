package eu.decentsoftware.holograms.api.animations;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents a custom animation.
 *
 * @author d0by
 */
public interface Animation {

    /**
     * Gets the name of the animation.
     *
     * @return The name of the animation.
     */
    @NotNull
    String getName();

    /**
     * Gets the type of the animation.
     *
     * @return The type of the animation.
     * @see AnimationType
     */
    @NotNull
    AnimationType getType();

    /**
     * Gets the total steps of the animation.
     *
     * @return The total steps of the animation.
     */
    long getTotalSteps();

}

package eu.decent.holograms.api.animations;

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
    String getName();

    /**
     * Gets the type of the animation.
     *
     * @return The type of the animation.
     * @see AnimationType
     */
    AnimationType getType();

    /**
     * Gets the total steps of the animation.
     *
     * @return The total steps of the animation.
     */
    long getTotalSteps();

}

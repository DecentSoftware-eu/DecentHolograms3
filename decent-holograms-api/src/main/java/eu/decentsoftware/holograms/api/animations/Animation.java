package eu.decentsoftware.holograms.api.animations;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents a custom animation.
 *
 * @author d0by
 */
public interface Animation {

    /**
     * Animate the given string using this animation.
     *
     * @param text The text to animate.
     * @param step The current step.
     * @param args Arguments.
     * @return The animated string.
     */
    @NotNull
    String animate(@NotNull String text, int step, String... args);

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
    int getTotalSteps();

    /**
     * Get the speed of this animation. Speed represents the
     * time to wait between animation steps in ticks.
     *
     * @return The speed of this animation.
     */
    int getSpeed();

    /**
     * Get the pause of this animation. Pause represents the
     * amount of time to wait between each reset.
     *
     * @return The pause of this animation.
     */
    int getPause();

}

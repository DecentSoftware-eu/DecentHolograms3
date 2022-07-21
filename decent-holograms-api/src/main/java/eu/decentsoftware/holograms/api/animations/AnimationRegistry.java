package eu.decentsoftware.holograms.api.animations;

import eu.decentsoftware.holograms.api.intent.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This registry is responsible for storing and replacing animations.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface AnimationRegistry extends Manager {

    /**
     * Animate the given text by replacing all animations in it.
     *
     * @param text The text.
     * @return The animated text.
     */
    @NotNull
    String animate(@NotNull String text);

    /**
     * Register a new animation.
     *
     * @param animation The animation.
     * @see Animation
     */
    void registerAnimation(@NotNull Animation animation);

    /**
     * Get animation by its name.
     *
     * @param name The name.
     * @return The animation or null if no animation with the given name is registered.
     * @see Animation
     */
    Animation getAnimation(@NotNull String name);

    /**
     * Remove animation by its name.
     *
     * @param name The name.
     * @return The removed animation or null if the given animation isn't registered.
     * @see Animation
     */
    Animation removeAnimation(@NotNull String name);

    /**
     * Check it this registry contains an animation with the given name.
     *
     * @param name The name.
     * @return True if this registry contains an animation with the name, false otherwise.
     * @see Animation
     */
    boolean hasAnimation(@NotNull String name);

    /**
     * Get all registered animations as a map.
     *
     * @return The map.
     * @see Animation
     */
    Map<String, Animation> getAnimations();

}

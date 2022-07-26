package eu.decentsoftware.holograms.api.animations;

import org.jetbrains.annotations.NotNull;

/**
 * This enum represents the different types of animations that can be used. The
 * animation type determines how the hologram will be animated, e.g. in which
 * order will the steps be executed.
 *
 * @author d0by
 */
public enum AnimationType {
    /**
     * This animation type will execute the steps in the order they are added.
     */
    ASCEND,
    /**
     * This animation type will execute the steps in the reverse order they are
     * added.
     */
    DESCEND,
    /**
     * This animation type will execute the steps in the order they are added,
     * and then in the reverse order.
     */
    ASCEND_DESCEND,
    /**
     * This animation type will execute the steps in random order.
     */
    RANDOM,

    INTERNAL;

    /**
     * Get a {@link AnimationType} from a string.
     *
     * @param name The name of the animation type.
     * @return The animation type or {@link #ASCEND} by default.
     */
    @NotNull
    public static AnimationType getByName(@NotNull String name) {
        for (AnimationType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return ASCEND;
    }
}

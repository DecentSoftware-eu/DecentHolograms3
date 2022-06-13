package eu.decent.holograms.api.animations;

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

    INTERNAL
}

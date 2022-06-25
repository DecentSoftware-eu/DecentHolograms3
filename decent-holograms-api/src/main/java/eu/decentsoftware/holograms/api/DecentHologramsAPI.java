package eu.decentsoftware.holograms.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * This class represents a provider of the current {@link DecentHolograms} instance.
 *
 * @author d0by
 * @since 3.0.0
 */
public final class DecentHologramsAPI {

    /**
     * The current running instance of {@link DecentHolograms}.
     */
    private static DecentHolograms instance;

    /**
     * Get the current running instance of {@link DecentHolograms}.
     *
     * @return The instance.
     */
    public static DecentHolograms getInstance() {
        return instance;
    }

    /**
     * > INTERNAL METHOD
     * <p>
     * Set the current running instance of {@link DecentHolograms}. This
     * can only be performed once - when the plugin starts.
     *
     * @param instance The instance.
     * @throws RuntimeException If the instance is already set.
     * @deprecated This method is only used internally.
     */
    @ApiStatus.Internal
    public static void setInstance(DecentHolograms instance) throws RuntimeException {
        if (DecentHologramsAPI.instance != null) {
            throw new RuntimeException("DecentHolograms is already running.");
        }
        DecentHologramsAPI.instance = instance;
    }

}

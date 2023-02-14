package eu.decentsoftware.holograms.api.internal;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * !! INTERNAL USE ONLY !!
 * <p>
 * Internal class to provide the {@link DecentHologramsAPI} instance.
 *
 * @author d0by
 * @since 3.0.0
 */
@ApiStatus.Internal
public final class DecentHologramsAPIProvider {

    private static DecentHologramsAPI instance;

    @Contract(value = " -> fail", pure = true)
    private DecentHologramsAPIProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    @Contract(pure = true)
    public static DecentHologramsAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DecentHologramsAPI instance is not set.");
        }
        return instance;
    }

    public static void setInstance(@NotNull DecentHologramsAPI api) {
        if (instance != null) {
            throw new IllegalStateException("DecentHologramsAPI instance is already set.");
        }
        instance = api;
    }

}

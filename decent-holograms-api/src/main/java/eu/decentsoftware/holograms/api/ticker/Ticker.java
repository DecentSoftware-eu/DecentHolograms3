package eu.decentsoftware.holograms.api.ticker;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents a ticker. It's used to tick the registered {@link ITicked} objects every tick.
 *
 * @author d0by
 * @see ITicked
 * @since 3.0.0
 */
public interface Ticker {

    /**
     * Register the given object to the ticker.
     *
     * @param ticked The object to register.
     */
    void register(@NotNull ITicked ticked);

    /**
     * Unregister the given object from the ticker.
     *
     * @param ticked The object to unregister.
     */
    void unregister(@NotNull ITicked ticked);

    /**
     * Unregister all objects from the ticker.
     */
    void unregisterAll();

    /**
     * Start the ticker.
     */
    void start();

    /**
     * Stop the ticker.
     */
    void stop();

    /**
     * Destroy the ticker unregistering all objects and stopping it.
     */
    void shutdown();

}

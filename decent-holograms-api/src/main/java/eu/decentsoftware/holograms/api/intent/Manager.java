package eu.decentsoftware.holograms.api.intent;

/**
 * This interface represents a manager.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface Manager {

    /**
     * Reload this manager.
     */
    void reload();

    /**
     * Shutdown this manager.
     */
    void shutdown();

}

package eu.decentsoftware.holograms.api.server;

import eu.decentsoftware.holograms.api.intent.Manager;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of pinged servers.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface ServerRegistry extends Manager {

    /**
     * Register a new server.
     *
     * @param server The server.
     */
    default void registerServer(@NotNull Server server) {
        registerServer(server.getName(), server);
    }

    void registerServer(@NotNull String name, @NotNull Server server);

    Server getServer(@NotNull String name);

    boolean containsServer(@NotNull String name);

}

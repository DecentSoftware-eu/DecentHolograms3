package eu.decentsoftware.holograms.api.server;

import eu.decentsoftware.holograms.api.utils.collection.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of pinged servers.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class ServerRegistry extends Registry<String, Server> {

    /**
     * Register a new server.
     *
     * @param server The server.
     */
    public void register(@NotNull Server server) {
        register(server.getName(), server);
    }

}

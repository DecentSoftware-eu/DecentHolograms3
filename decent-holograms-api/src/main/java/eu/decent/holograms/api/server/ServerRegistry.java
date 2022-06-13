package eu.decent.holograms.api.server;

import eu.decent.holograms.api.utils.collection.DictRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of pinged servers.
 */
public abstract class ServerRegistry extends DictRegistry<String, Server> {

    /**
     * Register a new server.
     *
     * @param server The server.
     */
    public void register(@NotNull Server server) {
        register(server.getName(), server);
    }

}

/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.server;

import eu.decentsoftware.holograms.BootMessenger;
import eu.decentsoftware.holograms.Config;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a registry of pinged servers.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ServerRegistry {

    private final Map<String, Server> serverMap;

    /**
     * Creates a new server registry and loads all servers into it.
     */
    public ServerRegistry() {
        this.serverMap = new ConcurrentHashMap<>();
        this.reload();
    }

    /**
     * Reload the registry, stop all servers from ticking and clear the cache, if any.
     * Then load all servers from the config.
     */
    public void reload() {
        this.shutdown();

        if (Config.PINGER_ENABLED && !Config.PINGER_SERVERS.isEmpty()) {
            long startMillis = System.currentTimeMillis();
            int counter = 0;
            for (String serverString : Config.PINGER_SERVERS) {
                if (!serverString.matches("\\S+:(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}):\\d{1,5}")) {
                    continue;
                }
                // -- Parse data
                String[] spl = serverString.split(":");
                String serverName = spl[0];
                String serverAddress = spl[1];
                int serverPort = Integer.parseInt(spl[2]);
                // -- Register the server
                InetSocketAddress inetSocketAddress = new InetSocketAddress(serverAddress, serverPort);
                Server server = new Server(serverName, inetSocketAddress);
                registerServer(server);
                counter++;
            }
            long took = System.currentTimeMillis() - startMillis;
            BootMessenger.log(String.format("Successfully loaded %d server%s in %d ms!", counter, counter == 1 ? "" : "s", took));
        }
    }

    /**
     * Shutdown the registry, stop all servers from ticking and clear the cache.
     */
    public void shutdown() {
        // -- Stop the existing servers from ticking
        for (Server server : this.serverMap.values()) {
            server.stopTicking();
        }
        this.serverMap.clear();
    }

    /**
     * Register a new server.
     *
     * @param server The server.
     * @see Server
     */
    public void registerServer(@NotNull Server server) {
        this.serverMap.put(server.getName(), server);
    }

    /**
     * Register a new server.
     *
     * @param name   The name of the server.
     * @param server The server.
     * @see Server
     */
    public void registerServer(@NotNull String name, @NotNull Server server) {
        this.serverMap.put(name, server);
    }

    /**
     * Get a server by its name.
     *
     * @param name The name of the server.
     * @return The server.
     * @see Server
     */
    public Server getServer(@NotNull String name) {
        return this.serverMap.get(name);
    }

    /**
     * Check if a server exists.
     *
     * @param name The name of the server.
     * @return True if the server exists.
     */
    public boolean containsServer(@NotNull String name) {
        return this.serverMap.containsKey(name);
    }

}

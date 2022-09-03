package eu.decentsoftware.holograms.server;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.api.server.Server;
import eu.decentsoftware.holograms.api.server.ServerRegistry;
import eu.decentsoftware.holograms.api.utils.Common;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServerRegistry implements ServerRegistry {

    private final Map<String, Server> serverMap;

    /**
     * Creates a new server registry and loads all servers into it.
     */
    public DefaultServerRegistry() {
        this.serverMap = new ConcurrentHashMap<>();
        this.reload();
    }

    @Override
    public void reload() {
        this.shutdown();

        if (Config.PINGER_ENABLED && !Config.PINGER_SERVERS.isEmpty()) {
            long startMillis = System.currentTimeMillis();
            Common.log("Loading server(s)...");
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
                DefaultServer server = new DefaultServer(serverName, inetSocketAddress);
                this.registerServer(server);
                counter++;
            }
            long took = System.currentTimeMillis() - startMillis;
            Common.log("Successfully loaded %d server%s in %d ms!", counter, counter == 1 ? "" : "s", took);
        }
    }

    @Override
    public void shutdown() {
        // -- Stop the existing servers from ticking
        for (Server server : this.serverMap.values()) {
            server.stopTicking();
        }
        this.serverMap.clear();
    }

    @Override
    public void registerServer(@NotNull String name, @NotNull Server server) {
        this.serverMap.put(name, server);
    }

    @Override
    public Server getServer(@NotNull String name) {
        return this.serverMap.get(name);
    }

    @Override
    public boolean containsServer(@NotNull String name) {
        return this.serverMap.containsKey(name);
    }
}

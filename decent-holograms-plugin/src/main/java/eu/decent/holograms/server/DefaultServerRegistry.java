package eu.decent.holograms.server;

import eu.decent.holograms.Config;
import eu.decent.holograms.api.server.Server;
import eu.decent.holograms.api.server.ServerRegistry;
import eu.decent.holograms.api.utils.Common;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

public class DefaultServerRegistry extends ServerRegistry {

    /**
     * Creates a new server registry and loads all servers into it.
     */
    public DefaultServerRegistry() {
        this.reload();
    }

    @Override
    public void reload() {
        this.clear();

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
                this.register(server);
                counter++;
            }
            long took = System.currentTimeMillis() - startMillis;
            Common.log("Successfully loaded %d server(s) in %d ms!", counter, took);
        }
    }

    @Override
    public void clear() {
        // -- Stop the existing servers from ticking
        for (Server server : getValues()) {
            server.stopTicking();
        }
        super.clear();
    }

    @Override
    public void register(@NotNull Server server) {
        register(server.getName(), server);
    }

}

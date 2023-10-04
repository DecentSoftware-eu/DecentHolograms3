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

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import lombok.NonNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * This registry holds all servers that are being pinged.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ServerRegistry {

    private static final Pattern ADDRESS_PATTERN = Pattern.compile("\\S+:(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}):\\d{1,5}");
    private final Map<String, Server> serverMap = new ConcurrentHashMap<>();
    private final DecentHolograms plugin;

    public ServerRegistry(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
        this.reload();
    }

    public synchronized void reload() {
        this.shutdown();

        if (Config.PINGER_ENABLED && !Config.PINGER_SERVERS.isEmpty()) {
            long startMillis = System.currentTimeMillis();
            int counter = 0;
            for (String serverString : Config.PINGER_SERVERS) {
                if (!ADDRESS_PATTERN.matcher(serverString).matches()) {
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
            plugin.getBootMessenger().log(String.format("Successfully loaded %d server%s in %d ms!", counter, counter == 1 ? "" : "s", took));
        }
    }

    public synchronized void shutdown() {
        // -- Stop the existing servers from ticking
        for (Server server : this.serverMap.values()) {
            server.stopTicking();
        }
        this.serverMap.clear();
    }

    public void registerServer(@NonNull Server server) {
        this.serverMap.put(server.getName(), server);
    }

    public Server getServer(@NonNull String name) {
        return this.serverMap.get(name);
    }

    public boolean containsServer(@NonNull String name) {
        return this.serverMap.containsKey(name);
    }

}

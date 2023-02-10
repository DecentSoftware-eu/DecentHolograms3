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
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import eu.decentsoftware.holograms.utils.pinger.Pinger;
import eu.decentsoftware.holograms.utils.pinger.PingerResponse;
import eu.decentsoftware.holograms.ticker.Ticked;
import eu.decentsoftware.holograms.utils.BungeeUtils;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class represents a server. It stores data about a pinged server.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
public class Server implements Ticked {

    // TODO: motd lines

    private final @NotNull String name;
    private final Pinger pinger;
    private final AtomicBoolean online;
    private final AtomicLong lastUpdate;
    private PingerResponse data;

    /**
     * Creates a new server instance.
     *
     * @param name    the name of the server
     * @param address the address of the server
     */
    public Server(@NotNull String name, @NotNull InetSocketAddress address) {
        this.name = name;
        this.pinger = new Pinger(address, Config.PINGER_TIMEOUT);
        this.online = new AtomicBoolean(false);
        this.lastUpdate = new AtomicLong(0);
        this.startTicking();
    }

    @Override
    public void tick() {
        long now = System.currentTimeMillis();
        if (now - lastUpdate.get() > Config.PINGER_UPDATE_INTERVAL * 50L) {
            SchedulerUtil.async(this::update);
            lastUpdate.set(now);
        }
    }

    /**
     * Update the server data.
     */
    public void update() {
        try {
            data = pinger.fetchData();
            online.set(true);
        } catch (Exception e) {
            online.set(false);
        }
    }

    /**
     * Connects the player to the server.
     *
     * @param player the player to connect
     */
    public void connect(@NotNull Player player) {
        BungeeUtils.connect(player, name);
    }

    /**
     * Get the name of the server.
     *
     * @return The name of the server.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the last server ping response.
     *
     * @return The last server ping response.
     */
    public PingerResponse getData() {
        return data;
    }

    /**
     * Check if the server is online.
     *
     * @return True if the server is online, false otherwise.
     */
    public boolean isOnline() {
        return data != null && online.get();
    }

    /**
     * Check if the server is full.
     *
     * @return True if the server is full, false otherwise.
     */
    public boolean isFull() {
        if (isOnline()) {
            PingerResponse.Players players = getData().getPlayers();
            return players.getMax() >= players.getOnline();
        }
        return false;
    }

}

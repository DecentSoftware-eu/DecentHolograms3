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
import eu.decentsoftware.holograms.ticker.Ticked;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import eu.decentsoftware.holograms.utils.pinger.Pinger;
import eu.decentsoftware.holograms.utils.pinger.PingerResponse;
import lombok.Getter;
import lombok.NonNull;

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

    private final String name;
    private final Pinger pinger;
    private final AtomicBoolean online;
    private final AtomicLong lastUpdate;
    private PingerResponse data;

    public Server(@NonNull String name, @NonNull InetSocketAddress address) {
        this.name = name;
        this.pinger = new Pinger(address, Config.PINGER_TIMEOUT);
        this.online = new AtomicBoolean(false);
        this.lastUpdate = new AtomicLong(0);
        this.startTicking();
    }

    @Override
    public void tick() {
        long now = System.currentTimeMillis();
        if (now - this.lastUpdate.get() > Config.PINGER_UPDATE_INTERVAL * 50L) {
            SchedulerUtil.async(this::update);
            this.lastUpdate.set(now);
        }
    }

    public void update() {
        try {
            this.data = this.pinger.fetchData();
            this.online.set(true);
        } catch (Exception e) {
            this.online.set(false);
        }
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public PingerResponse getData() {
        return this.data;
    }

    public boolean isOnline() {
        return this.data != null && this.online.get();
    }

    public boolean isFull() {
        if (isOnline()) {
            PingerResponse.Players players = this.data.getPlayers();
            return players.getMax() >= players.getOnline();
        }
        return false;
    }

}

package eu.decentsoftware.holograms.server;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.api.server.Server;
import eu.decentsoftware.holograms.api.utils.S;
import eu.decentsoftware.holograms.api.utils.pinger.Pinger;
import eu.decentsoftware.holograms.api.utils.pinger.PingerResponse;
import eu.decentsoftware.holograms.utils.BungeeUtils;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class DefaultServer implements Server {

    // TODO: motd lines

    private final String name;
    private final Pinger pinger;
    private final AtomicBoolean online;
    private final AtomicLong lastUpdate;
    private PingerResponse data;

    /**
     * Creates a new server instance.
     *
     * @param name the name of the server
     * @param address the address of the server
     */
    public DefaultServer(@NotNull String name, @NotNull InetSocketAddress address) {
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
            S.async(this::update);
            lastUpdate.set(now);
        }
    }

    @Override
    public void update() {
        try {
            data = pinger.fetchData();
            online.set(true);
        } catch (Exception e) {
            online.set(false);
        }
    }

    @Override
    public void connect(@NotNull Player player) {
        BungeeUtils.connect(player, name);
    }

    @Override
    public boolean isOnline() {
        return data != null && online.get();
    }

}

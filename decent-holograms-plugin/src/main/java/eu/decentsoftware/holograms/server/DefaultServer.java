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

/**
 * This class represents a pinged server.
 */
@Getter
public class DefaultServer implements Server {

    private final String name;
    private final Pinger pinger;
    private final AtomicBoolean online;
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
        this.startTicking();
    }

    @Override
    public void update() {
        S.async(() -> {
            try {
                data = pinger.fetchData();
                online.set(true);
            } catch (Exception e) {
                online.set(false);
            }
        });
    }

    @Override
    public void tick() {
        this.update();
    }

    @Override
    public void startTicking() {
        // TODO: ticking
    }

    @Override
    public void stopTicking() {

    }

    /**
     * Check if the server is online.
     *
     * @return true if the server is online, false otherwise
     */
    public boolean isOnline() {
        return data != null && online.get();
    }

    /**
     * Set the server online status.
     *
     * @param online the new online status
     */
    public void setOnline(boolean online) {
        this.online.set(online);
    }

    /**
     * Connects the player to the server.
     *
     * @param player the player to connect
     */
    public void connect(@NotNull Player player) {
        BungeeUtils.connect(player, name);
    }


}

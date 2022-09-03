package eu.decentsoftware.holograms.api.server;

import eu.decentsoftware.holograms.api.ticker.ITicked;
import eu.decentsoftware.holograms.api.utils.pinger.PingerResponse;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a server. It stores data about a pinged server.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface Server extends ITicked {

    /**
     * Update the server data.
     */
    void update();

    /**
     * Connects the player to the server.
     *
     * @param player the player to connect
     */
    void connect(@NotNull Player player);

    /**
     * Get the name of the server.
     *
     * @return The name of the server.
     */
    String getName();

    /**
     * Get the last server ping response.
     *
     * @return The last server ping response.
     */
    PingerResponse getData();

    /**
     * Check if the server is online.
     *
     * @return True if the server is online, false otherwise.
     */
    boolean isOnline();

    /**
     * Check if the server is full.
     *
     * @return True if the server is full, false otherwise.
     */
    default boolean isFull() {
        if (isOnline()) {
            PingerResponse.Players players = getData().getPlayers();
            return players.getMax() >= players.getOnline();
        }
        return false;
    }

}

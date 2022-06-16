package eu.decentsoftware.holograms.api.server;

import eu.decentsoftware.holograms.api.utils.pinger.PingerResponse;
import eu.decentsoftware.holograms.api.component.common.ITicked;

/**
 * This class represents a server.
 *
 * @author d0by
 */
public interface Server extends ITicked {

    /**
     * Update the server data.
     */
    void update();

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

}

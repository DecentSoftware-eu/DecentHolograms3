package eu.decentsoftware.holograms.api.nms;

import eu.decentsoftware.holograms.api.nms.listener.PacketListener;

/**
 * This class is responsible for providing NMS adapter
 * for the current Minecraft version.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface NMSProvider {

    /**
     * Gets the NMS adapter for the current version.
     *
     * @return The NMS adapter.
     * @see NMSAdapter
     */
    NMSAdapter getAdapter();

    /**
     * Gets the packet listener.
     *
     * @return The packet listener.
     * @see PacketListener
     */
    PacketListener getPacketListener();

    /**
     * Shutdown the NMS provider.
     */
    default void shutdown() {
        getPacketListener().destroy();
    }

}

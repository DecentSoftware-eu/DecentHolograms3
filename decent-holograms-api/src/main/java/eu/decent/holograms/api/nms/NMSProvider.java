package eu.decent.holograms.api.nms;

/**
 * This class is responsible for providing NMS adapter for the current Minecraft version.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface NMSProvider {

    /**
     * Gets the NMS adapter for the current version.
     *
     * @return The NMS adapter.
     */
    NMSAdapter getAdapter();

}

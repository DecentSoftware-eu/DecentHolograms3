package eu.decentsoftware.holograms.api.component.hologram;

import eu.decentsoftware.holograms.api.utils.collection.Registry;

/**
 * This class represents a registry of holograms. It holds all holograms that are currently registered.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class HologramRegistry extends Registry<String, Hologram> {

    /**
     * Shutdown the registry. This method hides and removes all holograms.
     */
    public abstract void shutdown();

}

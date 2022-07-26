package eu.decentsoftware.holograms.api.component.hologram;

import com.google.gson.Gson;
import eu.decentsoftware.holograms.api.utils.collection.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a registry of holograms. It holds all holograms that are currently registered.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class HologramRegistry extends Registry<String, Hologram> {

    /**
     * Register a new hologram.
     *
     * @param hologram The hologram.
     */
    public void register(@NotNull Hologram hologram) {
        register(hologram.getName(), hologram);
    }

    @NotNull
    public abstract Gson getGson();

}

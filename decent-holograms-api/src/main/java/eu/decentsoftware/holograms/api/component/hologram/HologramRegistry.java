package eu.decentsoftware.holograms.api.component.hologram;

import eu.decentsoftware.holograms.api.intent.Manager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * This class represents a registry of holograms. It holds all holograms that are currently registered.
 *
 * @author d0by
 * @see Hologram
 * @since 3.0.0
 */
public interface HologramRegistry extends Manager {

    /**
     * Register a new hologram.
     *
     * @param hologram The hologram.
     * @see Hologram
     */
    void registerHologram(@NotNull Hologram hologram);

    /**
     * Get a hologram by its name.
     *
     * @param name The name of the hologram.
     * @return The hologram.
     * @see Hologram
     */
    @Nullable
    Hologram getHologram(@NotNull String name);

    /**
     * Remove a hologram by its name.
     *
     * @param name The name of the hologram.
     * @see Hologram
     */
    @Nullable
    Hologram removeHologram(@NotNull String name);

    /**
     * Get the names of all registered holograms.
     *
     * @return The set of all hologram names.
     * @see Hologram
     */
    Set<String> getHologramNames();

    /**
     * Get all registered holograms.
     *
     * @return The collection of all holograms.
     * @see Hologram
     */
    Collection<Hologram> getHolograms();

}

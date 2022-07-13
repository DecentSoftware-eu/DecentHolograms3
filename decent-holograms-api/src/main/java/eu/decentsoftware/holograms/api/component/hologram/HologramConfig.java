package eu.decentsoftware.holograms.api.component.hologram;

import dev.dejvokep.boostedyaml.block.implementation.Section;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a hologram config. It is responsible for loading and saving the parent hologram.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramConfig {

    /**
     * Get the parent hologram.
     *
     * @return The parent hologram.
     */
    Hologram getParent();

    /**
     * Get the hologram file.
     *
     * @return The hologram file.
     */
    File getFile();

    /**
     * Get the hologram config as a {@link Section}.
     *
     * @return The hologram config as a {@link Section}.
     */
    Section getConfig();

    /**
     * Save the hologram file.
     *
     * @return True if the operation was successful, false otherwise in a {@link CompletableFuture}.
     */
    CompletableFuture<Boolean> save();

    /**
     * Load the hologram file.
     *
     * @return True if the operation was successful, false otherwise in a {@link CompletableFuture}.
     */
    CompletableFuture<Boolean> load();

    /**
     * Delete the hologram file.
     */
    void delete();

}

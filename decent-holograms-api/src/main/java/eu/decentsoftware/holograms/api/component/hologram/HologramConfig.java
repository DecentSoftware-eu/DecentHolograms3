package eu.decentsoftware.holograms.api.component.hologram;

import org.jetbrains.annotations.NotNull;

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
    @NotNull
    Hologram getParent();

    /**
     * Get the hologram file.
     *
     * @return The hologram file.
     */
    @NotNull
    File getFile();

    /**
     * Save the hologram file.
     *
     * @return True if the operation was successful, false otherwise in a {@link CompletableFuture}.
     */
    CompletableFuture<Void> save();

    /**
     * Reload the hologram file.
     *
     * @return True if the operation was successful, false otherwise in a {@link CompletableFuture}.
     */
    CompletableFuture<Void> reload();

    /**
     * Delete the hologram file.
     */
    void delete();

}

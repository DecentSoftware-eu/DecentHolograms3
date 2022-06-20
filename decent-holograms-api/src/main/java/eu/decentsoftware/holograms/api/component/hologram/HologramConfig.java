package eu.decentsoftware.holograms.api.component.hologram;

import dev.dejvokep.boostedyaml.YamlDocument;

import java.io.File;

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
     * Get the hologram file as a {@link YamlDocument}.
     *
     * @return The hologram file as a {@link YamlDocument}.
     */
    YamlDocument getConfig();

    /**
     * Save the hologram file.
     */
    default void save() {
        this.save(null);
    }

    /**
     * Save the hologram file.
     *
     * @param callback The callback to run after the hologram file has been saved.
     */
    void save(Runnable callback);

    /**
     * Load the hologram file.
     */
    default void load() {
        this.load(null);
    }

    /**
     * Load the hologram file.
     *
     * @param callback The callback to run after the hologram file has been loaded.
     */
    void load(Runnable callback);

    /**
     * Delete the hologram file.
     */
    void delete();

}

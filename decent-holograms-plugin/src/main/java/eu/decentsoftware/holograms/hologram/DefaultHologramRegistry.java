/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.hologram;

import eu.decentsoftware.holograms.BootMessenger;
import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.hologram.serialization.SerializableHologram;
import eu.decentsoftware.holograms.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a registry of holograms. It holds all holograms that are currently registered.
 *
 * @author d0by
 * @see Hologram
 * @since 3.0.0
 */
public class DefaultHologramRegistry {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private final @NotNull Map<String, DefaultHologram> holograms;

    public DefaultHologramRegistry() {
        this.holograms = new ConcurrentHashMap<>();
        this.reload();
    }

    /**
     * Reload the registry. This method is called when the plugin is reloaded.
     */
    public synchronized void reload() {
        this.shutdown();

        // Load holograms
        final long startMillis = System.currentTimeMillis();
        int counter = 0;

        File folder = new File(PLUGIN.getDataFolder(), "holograms");
        List<File> files = FileUtils.getFilesFromTree(folder, Config.NAME_REGEX + "\\.json", true);
        if (files.isEmpty()) {
            return;
        }

        for (File file : files) {
            try {
                String fileName = file.getName();
                String name = fileName.substring(0, fileName.length() - ".json".length());
                String string = new String(Files.readAllBytes(file.toPath()));
                SerializableHologram hologram = PLUGIN.getGson().fromJson(string, SerializableHologram.class);
                registerHologram(hologram.toHologram(name));
                counter++;
            } catch (Exception e) {
                PLUGIN.getLogger().severe("Failed to load hologram from '" + file.getName() + "'! Skipping...");
                e.printStackTrace();
            }
        }
        long took = System.currentTimeMillis() - startMillis;
        BootMessenger.log(String.format("Successfully loaded %d hologram%s in %d ms!", counter, counter == 1 ? "" : "s", took));
    }

    /**
     * Shutdown the registry. This method is called when the plugin is disabled.
     */
    public synchronized void shutdown() {
        // Destroy all holograms
        for (DefaultHologram hologram : this.holograms.values()) {
            hologram.destroy();
        }
        // Clear the cache
        this.holograms.clear();
    }

    /**
     * Register a new hologram.
     *
     * @param hologram The hologram.
     * @see Hologram
     */
    public void registerHologram(@NotNull DefaultHologram hologram) {
        this.holograms.put(hologram.getName(), hologram);
    }

    /**
     * Get a hologram by its name.
     *
     * @param name The name of the hologram.
     * @return The hologram.
     * @see DefaultHologram
     */
    @Nullable
    public DefaultHologram getHologram(@NotNull String name) {
        return this.holograms.get(name);
    }

    /**
     * Remove a hologram by its name.
     *
     * @param name The name of the hologram.
     * @see DefaultHologram
     */
    @Nullable
    public DefaultHologram removeHologram(@NotNull String name) {
        return this.holograms.remove(name);
    }

    /**
     * Get the names of all registered holograms.
     *
     * @return The set of all hologram names.
     * @see DefaultHologram
     */
    @NotNull
    public Set<String> getHologramNames() {
        return this.holograms.keySet();
    }

    /**
     * Get all registered holograms.
     *
     * @return The collection of all holograms.
     * @see DefaultHologram
     */
    @NotNull
    public Collection<DefaultHologram> getHolograms() {
        return this.holograms.values();
    }

}

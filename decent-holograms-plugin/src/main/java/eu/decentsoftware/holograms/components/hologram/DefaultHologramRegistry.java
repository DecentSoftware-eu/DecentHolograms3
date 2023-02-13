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

package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.BootMessenger;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
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

public class DefaultHologramRegistry implements HologramRegistry {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private final @NotNull Map<String, Hologram> holograms;

    public DefaultHologramRegistry() {
        this.holograms = new ConcurrentHashMap<>();
        this.reload();
    }

    @Override
    public void reload() {
        this.shutdown();

        // Load holograms
        final long startMillis = System.currentTimeMillis();
        int counter = 0;

        File folder = new File(PLUGIN.getDataFolder(), "holograms");
        List<File> files = FileUtils.getFilesFromTree(folder, "[a-zA-Z0-9_-]+\\.json", true);
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
            } catch (IOException e) {
                PLUGIN.getLogger().severe("Failed to load hologram from '" + file.getName() + "'! Skipping...");
                e.printStackTrace();
            }
        }
        long took = System.currentTimeMillis() - startMillis;
        BootMessenger.log(String.format("Successfully loaded %d hologram%s in %d ms!", counter, counter == 1 ? "" : "s", took));
    }

    @Override
    public void shutdown() {
        // Destroy all holograms
        for (Hologram hologram : this.holograms.values()) {
            hologram.destroy();
        }
        // Clear the cache
        this.holograms.clear();
    }

    @Override
    public void registerHologram(@NotNull Hologram hologram) {
        this.holograms.put(hologram.getName(), hologram);
    }

    @Nullable
    @Override
    public Hologram getHologram(@NotNull String name) {
        return this.holograms.get(name);
    }

    @Nullable
    @Override
    public Hologram removeHologram(@NotNull String name) {
        return this.holograms.remove(name);
    }

    @Override
    public Set<String> getHologramNames() {
        return this.holograms.keySet();
    }

    @Override
    public Collection<Hologram> getHolograms() {
        return this.holograms.values();
    }

}

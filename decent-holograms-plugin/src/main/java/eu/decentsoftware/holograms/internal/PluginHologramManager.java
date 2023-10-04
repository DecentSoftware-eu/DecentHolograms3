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

package eu.decentsoftware.holograms.internal;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.internal.serialization.SerializableHologram;
import eu.decentsoftware.holograms.utils.FileUtils;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginHologramManager {

    private final DecentHolograms plugin;
    private final Map<String, PluginHologram> holograms = new ConcurrentHashMap<>();
    private final PluginHologramListener listener;

    public PluginHologramManager(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
        this.listener = new PluginHologramListener(this);
        this.reload();

        Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
    }

    public synchronized void reload() {
        this.clear();

        final long startMillis = System.currentTimeMillis();
        File folder = new File(this.plugin.getDataFolder(), "holograms");
        List<File> files = FileUtils.getFilesFromTree(folder, Config.NAME_REGEX + "\\.json", true);
        if (files.isEmpty()) {
            return;
        }

        int counter = 0;
        for (File file : files) {
            try {
                String fileName = file.getName();
                String name = fileName.substring(0, fileName.length() - ".json".length());
                String string = new String(Files.readAllBytes(file.toPath()));
                SerializableHologram hologram = this.plugin.getGson().fromJson(string, SerializableHologram.class);
                registerHologram(hologram.toHologram(this.plugin, name));
                counter++;
            } catch (Exception e) {
                this.plugin.warnOrBoot("Failed to load hologram from '%s'! Skipping...", file.getName());
                e.printStackTrace();
            }
        }
        long took = System.currentTimeMillis() - startMillis;
        this.plugin.logOrBoot("Successfully loaded %d hologram%s in %d ms!", counter, counter == 1 ? "" : "s", took);
    }

    public synchronized void shutdown() {
        HandlerList.unregisterAll(this.listener);

        this.clear();
    }

    private void clear() {
        for (PluginHologram hologram : this.holograms.values()) {
            hologram.destroy();
        }
        this.holograms.clear();
    }

    public void registerHologram(@NonNull PluginHologram hologram) {
        this.holograms.put(hologram.getName(), hologram);
    }

    @Nullable
    public PluginHologram getHologram(@NonNull String name) {
        return this.holograms.get(name);
    }

    public void removeHologram(@NonNull String name) {
        this.holograms.remove(name);
    }

    public boolean hasHologram(@NonNull String name) {
        return this.holograms.containsKey(name);
    }

    @NonNull
    public Collection<PluginHologram> getHolograms() {
        return this.holograms.values();
    }

}

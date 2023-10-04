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

import com.google.gson.JsonSyntaxException;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.conditions.Condition;
import eu.decentsoftware.holograms.internal.serialization.SerializableHologram;
import eu.decentsoftware.holograms.internal.serialization.SerializablePage;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PluginHologramConfig {

    private final DecentHolograms plugin;
    private final PluginHologram parent;
    private final File file;

    public PluginHologramConfig(@NonNull DecentHolograms plugin, @NonNull PluginHologram parent) {
        this(plugin, parent, new File(plugin.getDataFolder(), "holograms/" + parent.getName() + ".json"));
    }

    @Contract(pure = true)
    public PluginHologramConfig(@NonNull DecentHolograms plugin, @NonNull PluginHologram parent, @NonNull File file) {
        this.plugin = plugin;
        this.parent = parent;
        this.file = file;
    }

    public void save() {
        CompletableFuture.runAsync(() -> {
            ensureFileExists();

            try (FileWriter writer = new FileWriter(this.file.getPath())) {
                this.plugin.getGson().toJson(new SerializableHologram(this.parent), writer);
            } catch (IOException e) {
                this.plugin.getLogger().severe("Failed to save hologram " + this.parent.getName() + ":");
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> reload() {
        return CompletableFuture.runAsync(() -> {
            if (!this.file.exists() || !this.file.isFile()) {
                return;
            }
            try (FileReader reader = new FileReader(this.file.getPath())) {
                SerializableHologram hologram = this.plugin.getGson().fromJson(reader, SerializableHologram.class);
                this.parent.getPositionManager().setLocation(hologram.getLocation());
                this.parent.getSettings().set(hologram.getSettings());
                List<PluginHologramPage> pages = new ArrayList<>();
                for (SerializablePage page : hologram.getPages()) {
                    pages.add(page.toPage(this.plugin, this.parent));
                }
                this.parent.setPages(pages);
                this.parent.getViewConditions().clearConditions();
                for (Condition condition : hologram.getViewConditions().getConditions()) {
                    this.parent.getViewConditions().addCondition(condition);
                }
            } catch (JsonSyntaxException | IOException e) {
                this.plugin.getLogger().severe("Failed to load hologram " + this.parent.getName() + ":");
                e.printStackTrace();
            }
        });
    }

    public void delete() {
        // Run asynchronously to prevent blocking the main thread
        if (Bukkit.isPrimaryThread()) {
            SchedulerUtil.async(this::delete);
        }
        this.file.delete();
    }

    private void ensureFileExists() {
        if (this.file.exists()) {
            return;
        }

        try {
            this.file.createNewFile();
        } catch (IOException e) {
            this.plugin.getLogger().severe("Failed to create hologram file " + this.file.getName() + ":");
            e.printStackTrace();
        }
    }

}

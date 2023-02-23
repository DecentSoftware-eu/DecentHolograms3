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

import com.google.gson.JsonSyntaxException;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.HologramConfig;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.hologram.serialization.SerializableHologram;
import eu.decentsoftware.holograms.conditions.Condition;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import eu.decentsoftware.holograms.hologram.serialization.SerializablePage;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DefaultHologramConfig implements HologramConfig {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private final @NotNull DefaultHologram parent;
    private final @NotNull File file;

    public DefaultHologramConfig(@NotNull DefaultHologram parent) {
        this(parent, new File(PLUGIN.getDataFolder(), "holograms/" + parent.getName() + ".json"));
    }

    public DefaultHologramConfig(@NotNull DefaultHologram parent, @NotNull File file) {
        this.parent = parent;
        this.file = file;
    }

    @NotNull
    @Override
    public DefaultHologram getParent() {
        return parent;
    }

    @NotNull
    @Override
    public File getFile() {
        return file;
    }

    @Override
    public CompletableFuture<Void> save() {
        return CompletableFuture.runAsync(() -> {
            if (parent.getSettings().isPersistent()) {
                return;
            }

            ensureFileExists();

            try (FileWriter writer = new FileWriter(file.getPath())) {
                PLUGIN.getGson().toJson(SerializableHologram.fromHologram(parent), writer);
            } catch (IOException e) {
                PLUGIN.getLogger().severe("Failed to save hologram " + parent.getName() + ":");
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> reload() {
        return CompletableFuture.runAsync(() -> {
            try (FileReader reader = new FileReader(file.getPath())) {
                SerializableHologram hologram = PLUGIN.getGson().fromJson(reader, SerializableHologram.class);
                parent.getPositionManager().setLocation(hologram.getLocation());
                parent.getSettings().set(hologram.getSettings());
                List<HologramPage> pages = new ArrayList<>();
                for (SerializablePage page : hologram.getPages()) {
                    pages.add(page.toPage(parent));
                }
                parent.setPages(pages);
                parent.getViewConditions().clearConditions();
                for (Condition condition : hologram.getViewConditions().getConditions()) {
                    parent.getViewConditions().addCondition(condition);
                }
            } catch (JsonSyntaxException | IOException e) {
                PLUGIN.getLogger().severe("Failed to load hologram " + parent.getName() + ":");
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void delete() {
        // Run asynchronously to prevent blocking the main thread
        if (Bukkit.isPrimaryThread()) {
            SchedulerUtil.async(this::delete);
        }
        getFile().delete();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void ensureFileExists() {
        if (!getFile().exists()) {
            try {
                getFile().createNewFile();
            } catch (IOException e) {
                PLUGIN.getLogger().severe("Failed to create hologram file " + getFile().getName() + ":");
                e.printStackTrace();
            }
        }
    }

}

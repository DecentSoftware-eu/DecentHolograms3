package eu.decentsoftware.holograms.components.hologram;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramConfig;
import eu.decentsoftware.holograms.api.component.hologram.HologramSettings;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.utils.S;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class DefaultHologramConfig implements HologramConfig {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();

    private final Hologram parent;
    private YamlDocument config;

    public DefaultHologramConfig(@NotNull Hologram parent) {
        this(parent, new File(PLUGIN.getHologramFolder(), parent.getName() + ".yml"));
    }

    public DefaultHologramConfig(@NotNull Hologram parent, @NotNull File file) {
        this.parent = parent;
        try {
            this.config = YamlDocument.create(file, PLUGIN.getResource(file.getName()));
        } catch (IOException e) {
            PLUGIN.getLogger().severe("Failed to load hologram file " + file.getName() + ":");
            e.printStackTrace();
        }
    }

    @Override
    public Hologram getParent() {
        return parent;
    }

    @Override
    public File getFile() {
        return config.getFile();
    }

    @Override
    public YamlDocument getConfig() {
        return config;
    }

    @Override
    public CompletableFuture<Boolean> save() {
        return CompletableFuture.supplyAsync(() -> {
            // - SAVE HOLOGRAM
            // Set location
            config.set("location", parent.getPositionManager().getLocation());
            // Set settings
            config.set("settings.enabled", parent.getSettings().isEnabled());
            config.set("settings.editable", parent.getSettings().isEditable());
            config.set("settings.down-origin", parent.getSettings().isDownOrigin());
            config.set("settings.fixed-rotation", parent.getSettings().isFixedRotation());
            config.set("settings.fixed-offsets", parent.getSettings().isFixedOffsets());
            config.set("settings.view-distance", parent.getSettings().getViewDistance());
            config.set("settings.updating", parent.getSettings().isUpdating());
            config.set("settings.update-distance", parent.getSettings().getUpdateDistance());
            config.set("settings.update-interval", parent.getSettings().getUpdateInterval());
            // Set pages
            for (Page page : parent.getPageHolder().getPages()) {

            }

            // Save hologram file
            ensureFileExists();
            saveConfig();

            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> load() {
        return CompletableFuture.supplyAsync(() -> {
            // Reload the config
            ensureFileExists();
            reloadConfig();

            // - LOAD HOLOGRAM
            // Load location
            Location location = (Location) config.get("location", Location.class);
            if (location == null) {
                PLUGIN.getLogger().severe("Failed to load hologram file " + getFile().getName() + ": location is null");
                return false;
            }
            // Load settings
            HologramSettings settings = parent.getSettings();
            settings.setEnabled(config.getBoolean("settings.enabled", settings.isEnabled()));
            settings.setEditable(config.getBoolean("settings.editable", settings.isEditable()));
            settings.setDownOrigin(config.getBoolean("settings.down-origin", settings.isDownOrigin()));
            settings.setFixedRotation(config.getBoolean("settings.fixed-rotation", settings.isFixedRotation()));
            settings.setFixedOffsets(config.getBoolean("settings.fixed-offsets", settings.isFixedOffsets()));
            settings.setViewDistance(config.getInt("settings.view-distance", settings.getViewDistance()));
            settings.setUpdating(config.getBoolean("settings.updating", settings.isUpdating()));
            settings.setUpdateDistance(config.getInt("settings.update-distance", settings.getUpdateDistance()));
            settings.setUpdateInterval(config.getInt("settings.update-interval", settings.getUpdateInterval()));
            // Load pages

            return true;
        });
    }

    @Override
    public void delete() {
        // Run asynchronously to prevent blocking the main thread
        if (Bukkit.isPrimaryThread()) {
            S.async(this::delete);
        }

        config.clear();
        getFile().delete();
    }

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

    private void reloadConfig() {
        try {
            config.reload();
        } catch (IOException e) {
            PLUGIN.getLogger().severe("Failed to load hologram file " + getFile().getName() + ":");
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            config.save();
        } catch (IOException e) {
            PLUGIN.getLogger().severe("Failed to save hologram file " + getFile().getName() + ":");
            e.printStackTrace();
        }
    }

}

package eu.decentsoftware.holograms.components.hologram;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.component.common.IClickable;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramConfig;
import eu.decentsoftware.holograms.api.component.hologram.HologramSettings;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineSettings;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.component.page.PageLineHolder;
import eu.decentsoftware.holograms.api.config.ConfigUtils;
import eu.decentsoftware.holograms.api.exception.LocationParseException;
import eu.decentsoftware.holograms.api.utils.S;
import eu.decentsoftware.holograms.components.line.DefaultLine;
import eu.decentsoftware.holograms.components.page.DefaultPage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public DefaultHologramConfig(@NotNull Hologram parent, @NotNull YamlDocument config) {
        this.parent = parent;
        this.config = config;
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
            if (!getParent().getSettings().isPersistent()) {
                return true;
            }

            // - SAVE HOLOGRAM
            // Set location
            config.set("location", ConfigUtils.locToString(parent.getPositionManager().getLocation(), true));
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
            List<Map<String, Object>> pages = new ArrayList<>();
            for (Page page : parent.getPageHolder().getPages()) {
                Map<String, Object> pageMap = new HashMap<>();
                PageLineHolder lineHolder = page.getLineHolder();
                List<Map<String, Object>> lines = new ArrayList<>();
                for (Line line : lineHolder.getLines()) {
                    Map<String, Object> lineMap = new HashMap<>();
                    lineMap.put("content", line.getContent());
                    lineMap.put("settings.updating", line.getSettings().isUpdating());
                    lineMap.put("settings.height", line.getSettings().getHeight());
                    lineMap.put("view-conditions", line.getViewConditionHolder());
                    lineMap.put("click-actions", line.getClickActionHolder());
                    lineMap.put("click-conditions", line.getClickConditionHolder());
                    lines.add(lineMap);
                }
                pageMap.put("lines", lines);
                pageMap.put("click-conditions", page.getClickConditionHolder());
                pageMap.put("click-actions", page.getClickActionHolder());
                pages.add(pageMap);
            }
            config.set("pages", pages);

            // Save hologram file
            ensureFileExists();
            saveConfig();
            return true;
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<Boolean> load() {

        return CompletableFuture.supplyAsync(() -> {
            if (!parent.getSettings().isPersistent()) {
                return true;
            }

            // Reload the config
            ensureFileExists();
            reloadConfig();

            // Erase current data
            parent.stopTicking();
            parent.getVisibilityManager().destroy();
            parent.getPageHolder().clearPages();

            // == LOAD HOLOGRAM ==

            // Load location
            Location location;
            try {
                location = ConfigUtils.stringToLoc(config.getString("location"));
                parent.getPositionManager().setLocation(location);
            } catch (LocationParseException e) {
                PLUGIN.getLogger().severe("Failed to load hologram " + parent.getName() + "!");
                e.printStackTrace();
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
            List<Map<?, ?>> pages = config.getMapList("pages");
            if (pages == null) {
                return true;
            }

            for (Map<?, ?> pageMap : pages) {
                Page page = new DefaultPage(parent);

                // Get lines
                List<Map<?, ?>> lines;
                try {
                    lines = (List<Map<?, ?>>) pageMap.get("lines");
                } catch (ClassCastException e) {
                    lines = null;
                }

                // Parse lines (if any)
                if (lines != null && !lines.isEmpty()) {
                    for (Map<?, ?> lineMap : lines) {
                        Line line = new DefaultLine(page, page.getNextLineLocation());

                        // Get line content
                        String content;
                        try {
                            content = (String) lineMap.get("content");
                        } catch (ClassCastException e) {
                            content = "";
                        }

                        // Get line settings
                        ConfigurationSection settingsSection;
                        try {
                            settingsSection = (ConfigurationSection) lineMap.get("settings");
                        } catch (ClassCastException e) {
                            settingsSection = null;
                        }

                        // Parse line settings (if any)
                        if (settingsSection != null) {
                            LineSettings lineSettings = line.getSettings();
                            lineSettings.setUpdating(settingsSection.getBoolean("updating", lineSettings.isUpdating()));
                            lineSettings.setHeight(settingsSection.getDouble("height", lineSettings.getHeight()));
                        }

                        // Get the view conditions
                        Object viewConditions = lineMap.get("view-conditions");
                        if (viewConditions instanceof ConfigurationSection) {
                            line.getViewConditionHolder().load((ConfigurationSection) viewConditions);
                        }

                        // Get the click actions and conditions
                        loadClickable(lineMap, line);
                        // Set the line content
                        line.setContent(content);
                        // Add the line to the page
                        page.getLineHolder().addLine(line);
                    }
                }

                // Get the click conditions and actions
                loadClickable(pageMap, page);
                // Add the page to the hologram
                parent.getPageHolder().addPage(page);
            }
            // Start ticking again
            parent.startTicking();
            return true;
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void delete() {
        // Run asynchronously to prevent blocking the main thread
        if (Bukkit.isPrimaryThread()) {
            S.async(this::delete);
        }

        config.clear();
        getFile().delete();
    }

    private void loadClickable(@NotNull Map<?, ?> map, @NotNull IClickable clickable) {
        Object conditions = map.get("click-conditions");
        if (conditions instanceof ConfigurationSection) {
            clickable.getClickConditionHolder().load((ConfigurationSection) conditions);
        }
        Object actions = map.get("click-actions");
        if (actions instanceof ConfigurationSection) {
            clickable.getClickActionHolder().load((ConfigurationSection) actions);
        }
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

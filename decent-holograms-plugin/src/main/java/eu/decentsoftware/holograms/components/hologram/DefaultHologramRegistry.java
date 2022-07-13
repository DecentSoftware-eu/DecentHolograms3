package eu.decentsoftware.holograms.components.hologram;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import eu.decentsoftware.holograms.api.exception.LocationParseException;
import eu.decentsoftware.holograms.api.utils.config.ConfigUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class DefaultHologramRegistry extends HologramRegistry {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();
    private final YamlDocument config;

    public DefaultHologramRegistry() {
        try {
            this.config = YamlDocument.create(new File(PLUGIN.getDataFolder(), "holograms.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.reload();
    }

    @Override
    public void reload() {
        // -- Load holograms from 'holograms.yml' -- //

        if (this.config != null) {
            // Reload the config
            try {
                this.config.reload();
            } catch (IOException e) {
                PLUGIN.getLogger().severe("Failed to load holograms from the 'holograms.yml' file! Skipping...");
                e.printStackTrace();
            }

            // Load all holograms
            for (String name : this.config.getRoutesAsStrings(false)) {
                Section section = this.config.getSection(name);
                try {
                    loadHologram(section, name);
                } catch (LocationParseException | InterruptedException | ExecutionException e) {
                    PLUGIN.getLogger().severe("Failed to load hologram " + name + "! Skipping...");
                    e.printStackTrace();
                }
            }
        }

        // -- Load holograms from individual files -- //

        // Get all files from the hologram folder
        File[] files = PLUGIN.getHologramFolder()
                .listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(".yml"));
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            // Get the hologram name from the file name
            String name = file.getName().substring(0, file.getName().length() - 4);
            try {
                // Load the hologram config
                YamlDocument section = YamlDocument.create(file);
                loadHologram(section, name);
            } catch (InterruptedException | ExecutionException | IOException | LocationParseException e) {
                PLUGIN.getLogger().severe("Failed to load hologram " + name + "! Skipping...");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Hologram hologram : this.getValues()) {
            hologram.destroy();
        }
        this.clear();
    }

    private void loadHologram(@NotNull Section config, @NotNull String name)
            throws LocationParseException, InterruptedException, ExecutionException {
        // Get the hologram location
        Location location = ConfigUtils.stringToLoc(config.getString("location"));
        // Create the hologram
        Hologram hologram = new DefaultHologram(name, location, config, true, true);
        // Load the hologram
        if (hologram.getConfig().load().get()) {
            // Set the hologram to be visible by default
            hologram.getVisibilityManager().setVisibleByDefault(true);
            // Add the hologram to the registry
            this.register(hologram);
        }
    }

}

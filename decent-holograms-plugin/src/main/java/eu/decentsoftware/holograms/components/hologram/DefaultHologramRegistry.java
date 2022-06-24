package eu.decentsoftware.holograms.components.hologram;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import eu.decentsoftware.holograms.api.utils.config.ConfigUtils;
import eu.decentsoftware.holograms.api.exception.LocationParseException;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class DefaultHologramRegistry extends HologramRegistry {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();

    public DefaultHologramRegistry() {
        this.reload();
    }

    @Override
    public void reload() {
        // Get all files from the hologram folder
        File[] files = PLUGIN.getHologramFolder().listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            // Make sure the file is a yaml file
            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            // Get the hologram name from the file name
            String hologramName = file.getName().substring(0, file.getName().length() - 4);

            try {
                // Load the hologram config
                YamlDocument config = YamlDocument.create(file);
                // Get the hologram location
                Location location = ConfigUtils.stringToLoc(config.getString("location"));
                // Create the hologram
                Hologram hologram = new DefaultHologram(hologramName, location, config, true, true);
                // Load the hologram
                if (hologram.getConfig().load().get()) {
                    // Set the hologram to be visible by default
                    hologram.getVisibilityManager().setVisibleByDefault(true);
                    // Add the hologram to the registry
                    this.register(hologram);
                }
            } catch (InterruptedException | ExecutionException | IOException | LocationParseException e) {
                PLUGIN.getLogger().severe("Failed to load hologram " + hologramName + ": " + e.getMessage());
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

}

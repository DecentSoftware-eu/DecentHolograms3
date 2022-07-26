package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import eu.decentsoftware.holograms.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class DefaultHologramRegistry extends HologramRegistry {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();

    private final @NotNull File hologramsFile;

    public DefaultHologramRegistry() {
        this.hologramsFile = new File(PLUGIN.getDataFolder(), "holograms.yml");
        this.reload();
    }

    @Override
    public void reload() {
        this.shutdown();

        // -- Load holograms from 'holograms.json' -- //
        // TODO

//        if (hologramsFile.exists()) {
//            try (FileInputStream fis = new FileInputStream(hologramsFile)) {
//                String string = new String(fis.readAllBytes());
//                List<SerializableHologram> holograms = gson.fromJson(string, new TypeToken<List<SerializableHologram>>() {}.getType());
//                holograms.forEach((hologram) -> register(hologram.toHologram()));
//            } catch (IOException e) {
//                PLUGIN.getLogger().severe("Failed to load holograms from the 'holograms.json' file! Skipping...");
//                e.printStackTrace();
//            }
//        }

        // -- Load holograms from individual files -- //

        List<File> files = FileUtils.getFilesFromTree(PLUGIN.getHologramFolder(), (f) -> f.getName().endsWith(".yml"));
        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file)) {
                String string = new String(fis.readAllBytes());
                SerializableHologram hologram = PLUGIN.getGson().fromJson(string, SerializableHologram.class);
                register(hologram.toHologram());
            } catch (IOException e) {
                PLUGIN.getLogger().severe("Failed to load hologram from '" + file.getName() + "'! Skipping...");
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

}

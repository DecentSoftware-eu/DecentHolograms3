package eu.decentsoftware.holograms.components.hologram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.decentsoftware.holograms.actions.DefaultActionDeserializer;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import eu.decentsoftware.holograms.components.serialization.LocationSerializer;
import eu.decentsoftware.holograms.utils.FileUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class DefaultHologramRegistry extends HologramRegistry {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();

    private final @NotNull File hologramsFile;
    private final @NotNull Gson gson;

    public DefaultHologramRegistry() {
        this.hologramsFile = new File(PLUGIN.getDataFolder(), "holograms.yml");
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .registerTypeAdapter(Action.class, new DefaultActionDeserializer())
                .setPrettyPrinting()
                .create();
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
                SerializableHologram hologram = gson.fromJson(string, SerializableHologram.class);
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

    @NotNull
    @Override
    public Gson getGson() {
        return gson;
    }

}

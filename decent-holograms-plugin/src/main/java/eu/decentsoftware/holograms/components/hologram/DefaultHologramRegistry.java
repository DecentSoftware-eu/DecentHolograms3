package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import eu.decentsoftware.holograms.api.utils.Common;
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
        Common.log("Loading hologram(s)...");
        long startMillis = System.currentTimeMillis();
        int counter = 0;
        List<File> files = FileUtils.getFilesFromTree(PLUGIN.getHologramFolder(), (f) -> f.getName().endsWith(".json"));
        for (File file : files) {
            try {
                String fileName = file.getName();;
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
        Common.log(String.format("Successfully loaded %d hologram%s in %d ms!", counter, counter == 1 ? "" : "s", took));
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

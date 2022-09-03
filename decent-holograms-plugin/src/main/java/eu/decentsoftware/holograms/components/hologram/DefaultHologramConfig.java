package eu.decentsoftware.holograms.components.hologram;

import com.google.gson.JsonSyntaxException;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramConfig;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.conditions.Condition;
import eu.decentsoftware.holograms.api.utils.S;
import eu.decentsoftware.holograms.api.utils.collection.DecentList;
import eu.decentsoftware.holograms.components.page.SerializablePage;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class DefaultHologramConfig implements HologramConfig {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private final @NotNull Hologram parent;
    private final @NotNull File file;

    public DefaultHologramConfig(@NotNull Hologram parent) {
        this(parent, new File(PLUGIN.getHologramFolder(), parent.getName() + ".yml"));
    }

    public DefaultHologramConfig(@NotNull Hologram parent, @NotNull File file) {
        this.parent = parent;
        this.file = file;
    }

    @NotNull
    @Override
    public Hologram getParent() {
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

            try (FileWriter writer = new FileWriter(getFile().getPath())) {
                PLUGIN.getGson().toJson(SerializableHologram.fromHologram((DefaultHologram) parent), writer);
            } catch (IOException e) {
                PLUGIN.getLogger().severe("Failed to save hologram " + parent.getName() + ":");
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> reload() {
        return CompletableFuture.runAsync(() -> {
            try (FileReader reader = new FileReader(getFile().getPath())) {
                SerializableHologram hologram = PLUGIN.getGson().fromJson(reader, SerializableHologram.class);
                parent.getPositionManager().setLocation(hologram.getLocation());
                parent.getSettings().set(hologram.getSettings());
                DecentList<Page> pages = new DecentList<>();
                for (SerializablePage page : hologram.getPages()) {
                    pages.add(page.toPage((DefaultHologram) parent));
                }
                parent.getPageHolder().setPages(pages);
                parent.getViewConditionHolder().clearConditions();
                for (Condition condition : hologram.getViewConditions().getConditions()) {
                    parent.getViewConditionHolder().addCondition(condition);
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
            S.async(this::delete);
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

package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.APIHologram;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.HologramPage;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import lombok.NonNull;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;

import java.util.List;

public class DecentHologramsAPIImpl implements DecentHologramsAPI {

    private final DecentHolograms plugin;

    @Contract(pure = true)
    public DecentHologramsAPIImpl(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
    }

    @NonNull
    @Override
    public Hologram createHologram(@NonNull Location location) {
        return new APIHologram(plugin, new DecentLocation(location));
    }

    @NonNull
    @Override
    public Hologram createHologram(@NonNull Location location, @NonNull List<String> lines) {
        APIHologram hologram = new APIHologram(plugin, new DecentLocation(location));
        HologramPage page = hologram.appendPage();
        page.setLinesFromStrings(lines);
        return hologram;
    }

}

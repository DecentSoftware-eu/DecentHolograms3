package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class DefaultDecentHologramsAPI implements DecentHologramsAPI {

    @NotNull
    @Override
    public Hologram createHologram(@NotNull Location location) {
        return new DefaultHologram(UUID.randomUUID().toString(), location);
    }

    @NotNull
    @Override
    public Hologram createHologram(@NotNull Location location, boolean persistent) {
        return new DefaultHologram(UUID.randomUUID().toString(), location, true, persistent);
    }

    @NotNull
    @Override
    public Hologram createHologram(@NotNull Location location, @NotNull List<String> lines) {
        Hologram hologram = new DefaultHologram(UUID.randomUUID().toString(), location);
        hologram.addPage().getPage(0).setLinesFromStrings(lines);
        return hologram;
    }

    @NotNull
    @Override
    public Hologram createHologram(@NotNull Location location, @NotNull List<String> lines, boolean persistent) {
        Hologram hologram = new DefaultHologram(UUID.randomUUID().toString(), location, true, persistent);
        hologram.addPage().getPage(0).setLinesFromStrings(lines);
        return hologram;
    }

}

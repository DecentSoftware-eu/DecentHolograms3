package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class DecentHologramsAPIImpl implements DecentHologramsAPI {

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
        HologramPage page = hologram.addPage();
        page.setLinesFromStrings(lines);
        return hologram;
    }

    @NotNull
    @Override
    public Hologram createHologram(@NotNull Location location, @NotNull List<String> lines, boolean persistent) {
        Hologram hologram = new DefaultHologram(UUID.randomUUID().toString(), location, true, persistent);
        HologramPage page = hologram.addPage();
        page.setLinesFromStrings(lines);
        return hologram;
    }

}

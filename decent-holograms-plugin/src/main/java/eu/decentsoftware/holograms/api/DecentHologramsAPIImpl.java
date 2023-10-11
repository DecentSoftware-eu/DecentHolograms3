package eu.decentsoftware.holograms.api;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.APIHologram;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.HologramPage;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DecentHologramsAPIImpl implements DecentHologramsAPI {

    private final DecentHolograms decentHolograms;
    private final List<Hologram> holograms = new ArrayList<>();

    @Contract(pure = true)
    public DecentHologramsAPIImpl(@NonNull DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
    }

    public void destroy() {
        destroyHolograms();
    }

    @NonNull
    @Override
    public Hologram createHologram(@NonNull DecentLocation location) {
        return new APIHologram(this.decentHolograms, location);
    }

    @NonNull
    @Override
    public Hologram createHologram(@NonNull DecentLocation location, @NonNull List<String> lines) {
        APIHologram hologram = new APIHologram(this.decentHolograms, location);
        HologramPage page = hologram.appendPage();
        page.setLinesFromStrings(lines);
        return hologram;
    }

    @Override
    public Collection<Hologram> getHolograms() {
        return ImmutableList.copyOf(this.holograms);
    }

    @Override
    public void destroyHolograms() {
        this.holograms.forEach(Hologram::destroy);
        this.holograms.clear();
    }
}

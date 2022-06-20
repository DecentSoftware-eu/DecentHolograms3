package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;

public class DefaultHologramRegistry extends HologramRegistry {

    public DefaultHologramRegistry() {
        this.reload();
    }

    @Override
    public void reload() {
        // TODO: Reload all holograms
    }

    @Override
    public void shutdown() {
        for (Hologram hologram : this.getValues()) {
            hologram.destroy();
        }
        this.clear();
    }

}

package eu.decent.holograms.components.hologram;

import eu.decent.holograms.api.component.hologram.HologramSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultHologramSettings implements HologramSettings {

    private boolean persistent;
    private boolean downOrigin;
    private boolean editable;
    private boolean fixedRotation;
    private boolean fixedOffsets;
    private int viewDistance;
    private int updateDistance;
    private int updateInterval;
    private boolean updating;

    /**
     * Creates a new instance of {@link DefaultHologramSettings} with default values.
     *
     * @param persistent Whether the hologram is persistent.
     */
    public DefaultHologramSettings(boolean persistent) {
        this.persistent = persistent;
        this.downOrigin = false;
        this.editable = true;
        this.fixedRotation = false;
        this.fixedOffsets = false;
        this.viewDistance = 48;
        this.updateDistance = 48;
        this.updateInterval = 20;
        this.updating = true;
    }

}

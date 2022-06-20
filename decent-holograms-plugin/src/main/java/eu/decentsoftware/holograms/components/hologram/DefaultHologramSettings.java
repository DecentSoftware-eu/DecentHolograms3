package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.hologram.HologramSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultHologramSettings implements HologramSettings {

    private boolean enabled;
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
     * @param enabled    Whether the hologram is enabled.
     * @param persistent Whether the hologram is persistent.
     */
    public DefaultHologramSettings(boolean enabled, boolean persistent) {
        this.enabled = enabled;
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

package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.hologram.HologramSettings;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class DefaultHologramSettings implements HologramSettings {

    private boolean enabled;
    private transient boolean persistent;
    private boolean editable;
    private boolean interactive;
    private boolean downOrigin;
    private boolean rotateHorizontal;
    private boolean rotateVertical;
    private boolean rotateHeads;
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
        this.interactive = false;
        this.rotateHorizontal = true;
        this.rotateVertical = true;
        this.rotateHeads = true;
        this.viewDistance = 48;
        this.updateDistance = 48;
        this.updateInterval = 20;
        this.updating = true;
    }

    @Override
    public void set(@NotNull HologramSettings settings) {
        this.enabled = settings.isEnabled();
        this.persistent = settings.isPersistent();
        this.editable = settings.isEditable();
        this.interactive = settings.isInteractive();
        this.downOrigin = settings.isDownOrigin();
        this.rotateHorizontal = settings.isRotateHorizontal();
        this.rotateVertical = settings.isRotateVertical();
        this.rotateHeads = settings.isRotateHeads();
        this.viewDistance = settings.getViewDistance();
        this.updateDistance = settings.getUpdateDistance();
        this.updateInterval = settings.getUpdateInterval();
        this.updating = settings.isUpdating();
    }

}

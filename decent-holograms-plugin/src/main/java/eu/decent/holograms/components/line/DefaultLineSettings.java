package eu.decent.holograms.components.line;

import eu.decent.holograms.api.component.line.LineSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultLineSettings implements LineSettings {

    private boolean updating;
    private double height;
    private double offsetX;
    private double offsetY;
    private double offsetZ;

    /**
     * Create a new instance of {@link DefaultLineSettings} with default values.
     */
    public DefaultLineSettings() {
        this.updating = true;
        this.height = 0.3d;
        this.offsetX = 0.0d;
        this.offsetY = 0.0d;
        this.offsetZ = 0.0d;
    }

}

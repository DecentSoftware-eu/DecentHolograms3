package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.api.component.line.LineSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultLineSettings implements LineSettings {

    private boolean updating;
    private double height;

    /**
     * Create a new instance of {@link DefaultLineSettings} with default values.
     */
    public DefaultLineSettings() {
        this.updating = true;
        this.height = 0.3d;
    }

}

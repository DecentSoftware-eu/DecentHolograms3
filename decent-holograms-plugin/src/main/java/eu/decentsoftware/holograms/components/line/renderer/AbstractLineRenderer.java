package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLineRenderer implements LineRenderer {

    /*
     * TODO:
     *  - Add support for animations
     *  - Add offsets to align the lines properly
     */

    protected static final DecentHolograms API = DecentHologramsAPI.getInstance();
    protected static final NMSAdapter NMS = API.getNMSProvider().getAdapter();

    private final @NotNull Line parent;
    private final @NotNull LineType type;

    public AbstractLineRenderer(@NotNull Line parent, @NotNull LineType type) {
        this.parent = parent;
        this.type = type;
    }

    @Override
    public double getHeight() {
        return getParent().getSettings().getHeight();
    }

    @Override
    public double getWidth() {
        return 0; // TODO
    }

    @NotNull
    @Override
    public Line getParent() {
        return parent;
    }

    @NotNull
    @Override
    public LineType getType() {
        return type;
    }

}

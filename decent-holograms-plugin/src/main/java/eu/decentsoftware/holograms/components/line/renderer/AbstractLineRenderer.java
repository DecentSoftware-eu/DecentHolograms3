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
     *  - Add support for glowing
     *  - Add offsets to align the lines properly
     */

    protected static final DecentHolograms API = DecentHologramsAPI.getInstance();
    protected static final NMSAdapter NMS = API.getNMSProvider().getAdapter();
    private static int ENTITY_ID = 1_000_000;

    private final Line parent;
    private final LineType type;

    public AbstractLineRenderer(@NotNull Line parent, @NotNull LineType type) {
        this.parent = parent;
        this.type = type;
    }

    /**
     * Get a new entity ID.
     *
     * @return The new entity ID.
     */
    protected int getFreeEntityId() {
        return ENTITY_ID++;
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

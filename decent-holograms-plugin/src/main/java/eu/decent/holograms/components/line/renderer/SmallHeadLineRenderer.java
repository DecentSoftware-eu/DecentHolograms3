package eu.decent.holograms.components.line.renderer;

import eu.decent.holograms.api.component.line.Line;
import eu.decent.holograms.api.component.line.LineType;
import eu.decent.holograms.api.utils.item.LineItemStack;
import org.jetbrains.annotations.NotNull;

public class SmallHeadLineRenderer extends HeadLineRenderer {

    public SmallHeadLineRenderer(@NotNull Line parent, @NotNull LineItemStack itemStack) {
        super(parent, itemStack, LineType.SMALL_HEAD, true);
    }

}

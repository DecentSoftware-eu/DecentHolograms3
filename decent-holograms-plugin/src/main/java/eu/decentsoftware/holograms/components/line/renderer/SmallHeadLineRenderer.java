package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.components.line.content.objects.LineItemStack;
import org.jetbrains.annotations.NotNull;

public class SmallHeadLineRenderer extends HeadLineRenderer {

    public SmallHeadLineRenderer(@NotNull Line parent, @NotNull LineItemStack itemStack) {
        super(parent, itemStack, LineType.SMALL_HEAD, true);
    }

}

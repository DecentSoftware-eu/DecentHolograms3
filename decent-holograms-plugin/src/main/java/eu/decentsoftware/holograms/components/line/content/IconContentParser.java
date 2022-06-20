package eu.decentsoftware.holograms.components.line.content;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.api.component.line.content.objects.LineItemStack;
import eu.decentsoftware.holograms.components.line.renderer.IconLineRenderer;
import org.jetbrains.annotations.NotNull;

public class IconContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull Line line) {
        String content = line.getContent();
        if (!content.startsWith("#ICON:")) {
            return false;
        }
        LineItemStack itemStack = LineItemStack.fromString(content);
        if (itemStack == null) {
            return false;
        }
        LineRenderer renderer = new IconLineRenderer(line, itemStack);
        line.setRenderer(renderer);
        return true;
    }

}

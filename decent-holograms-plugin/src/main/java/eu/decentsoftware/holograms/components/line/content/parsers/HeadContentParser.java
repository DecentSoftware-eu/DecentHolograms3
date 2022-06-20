package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.api.component.line.content.objects.LineItemStack;
import eu.decentsoftware.holograms.components.line.renderer.HeadLineRenderer;
import org.jetbrains.annotations.NotNull;

public class HeadContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull Line line) {
        String content = line.getContent();
        if (!content.startsWith("#HEAD:")) {
            return false;
        }
        LineItemStack itemStack = LineItemStack.fromString(content);
        if (itemStack == null) {
            return false;
        }
        LineRenderer renderer = new HeadLineRenderer(line, itemStack);
        line.setRenderer(renderer);
        return true;
    }

}
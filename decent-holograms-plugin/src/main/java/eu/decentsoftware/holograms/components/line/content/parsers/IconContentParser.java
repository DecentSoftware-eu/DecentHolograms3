package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.components.line.content.objects.LineItemStack;
import eu.decentsoftware.holograms.components.line.renderer.IconLineRenderer;
import org.jetbrains.annotations.NotNull;

public class IconContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull Line line) {
        String content = line.getContent();
        if (!content.startsWith("#ICON:")) {
            return false;
        }
        content = content.substring("#ICON:".length());

        boolean glowing = content.matches(".*(?i)--glow(ing)?.*");
        if (glowing) {
            content = content.replaceAll("(?i)--glow(ing)?", "");
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

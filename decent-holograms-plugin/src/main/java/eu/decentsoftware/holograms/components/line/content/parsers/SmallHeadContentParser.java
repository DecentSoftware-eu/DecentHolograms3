package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.components.line.content.objects.DecentItemStack;
import eu.decentsoftware.holograms.components.line.renderer.SmallHeadLineRenderer;
import org.jetbrains.annotations.NotNull;

public class SmallHeadContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull Line line) {
        String content = line.getContent();
        if (content == null || !content.startsWith("#SMALLHEAD:")) {
            return false;
        }
        DecentItemStack itemStack = DecentItemStack.fromString(content);
        LineRenderer renderer = new SmallHeadLineRenderer(line, itemStack);
        line.setRenderer(renderer);
        line.getPositionManager().getOffsets().setY(-1.1875d);
        return true;
    }

}

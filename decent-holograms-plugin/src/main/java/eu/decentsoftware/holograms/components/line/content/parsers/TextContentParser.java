package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.components.line.renderer.TextLineRenderer;
import org.jetbrains.annotations.NotNull;

public class TextContentParser implements ContentParser {

    // TODO: hover content

    @Override
    public boolean parse(@NotNull Line line) {
        String content = line.getContent();
        if (content == null) {
            content = "";
        }
        LineRenderer renderer = new TextLineRenderer(line, content);
        line.setRenderer(renderer);
        line.getPositionManager().getOffsets().setY(-0.5d);
        return true;
    }

}

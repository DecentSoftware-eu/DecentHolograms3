package eu.decentsoftware.holograms.components.line.content;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.components.line.renderer.TextLineRenderer;
import org.jetbrains.annotations.NotNull;

public class TextContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull Line line) {
        LineRenderer renderer = new TextLineRenderer(line, line.getContent());
        line.setRenderer(renderer);
        return true;
    }

}

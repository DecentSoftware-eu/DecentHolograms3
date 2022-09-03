package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.components.line.content.objects.DecentImage;
import eu.decentsoftware.holograms.components.line.renderer.ImageLineRenderer;
import org.jetbrains.annotations.NotNull;

public class ImageContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull Line line) {
        String content = line.getContent();
        if (content == null || !content.startsWith("#IMAGE:")) {
            return false;
        }
        content = content.substring("#IMAGE:".length());

        DecentImage image = DecentImage.fromString(content);
        if (image == null) {
            return false;
        }

        ImageLineRenderer renderer = new ImageLineRenderer(line, image);
        line.setRenderer(renderer);
        line.getPositionManager().getOffsets().setY(-0.5d);
        return true;
    }

}

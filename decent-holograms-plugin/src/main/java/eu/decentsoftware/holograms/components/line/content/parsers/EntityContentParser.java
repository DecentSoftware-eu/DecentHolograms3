package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.components.line.content.objects.DecentEntity;
import eu.decentsoftware.holograms.components.line.renderer.EntityLineRenderer;
import org.jetbrains.annotations.NotNull;

public class EntityContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull Line line) {
        String content = line.getContent();
        if (content == null || !content.startsWith("#ENTITY:")) {
            return false;
        }
        content = content.substring("#ENTITY:".length());

        DecentEntity entity = DecentEntity.fromString(content);

        EntityLineRenderer renderer = new EntityLineRenderer(line, entity);
        line.setRenderer(renderer);
        return true;
    }

}

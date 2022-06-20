package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.components.line.renderer.EntityLineRenderer;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class EntityContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull Line line) {
        String content = line.getContent();
        if (!content.startsWith("#ENTITY:")) {
            return false;
        }
        content = content.substring("#ENTITY:".length());

        boolean glowing = content.matches(".*(?i)--glow(ing)?.*");
        if (glowing) {
            content = content.replaceAll("(?i)--glow(ing)?", "");
        }
        content = content.trim();

        EntityType entityType = EntityType.PIG;
        for (EntityType value : EntityType.values()) {
            if (value.name().equalsIgnoreCase(content)) {
                entityType = value;
            }
        }
        line.setRenderer(new EntityLineRenderer(line, entityType, glowing));
        return true;
    }

}

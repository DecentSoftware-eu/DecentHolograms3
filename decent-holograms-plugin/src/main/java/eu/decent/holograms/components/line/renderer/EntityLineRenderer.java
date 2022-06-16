package eu.decent.holograms.components.line.renderer;

import eu.decent.holograms.api.component.line.Line;
import eu.decent.holograms.api.component.line.LineType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityLineRenderer extends AbstractDoubleEntityLineRenderer {

    private final EntityType entityType;

    public EntityLineRenderer(@NotNull Line parent, @NotNull EntityType entityType) {
        super(parent, LineType.ENTITY);
        this.entityType = entityType;
    }

    @Override
    public void display(@NotNull Player player) {
        // Create the entity metadata objects
        Object metaEntityOther = NMS.getMetaEntityProperties(false, false, false, false, false, false, false);

        // Display
        super.display(player, entityType, metaEntityOther);
    }

    @Override
    public void update(@NotNull Player player) {
        // Nothing to do until animations are implemented
    }

}

package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityLineRenderer extends AbstractDoubleEntityLineRenderer {

    private final EntityType entityType;
    private final boolean glowing;

    public EntityLineRenderer(@NotNull Line parent, @NotNull EntityType entityType) {
        this(parent, entityType, false);
    }

    public EntityLineRenderer(@NotNull Line parent, @NotNull EntityType entityType, boolean glowing) {
        super(parent, LineType.ENTITY);
        this.entityType = entityType;
        this.glowing = glowing;
    }

    @Override
    public void display(@NotNull Player player) {
        // Create the entity metadata objects
        Object metaEntityOther = NMS.getMetaEntityProperties(false, false, false, false, false, glowing, false);

        // Display
        super.display(player, entityType, metaEntityOther);
    }

    @Override
    public void update(@NotNull Player player) {
        // Nothing to do until animations are implemented
    }

}

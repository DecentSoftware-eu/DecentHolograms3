package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.components.line.content.objects.DecentEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityLineRenderer extends AbstractDoubleEntityLineRenderer {

    private final @NotNull DecentEntity entity;

    public EntityLineRenderer(@NotNull Line parent, @NotNull DecentEntity entity) {
        super(parent, LineType.ENTITY);
        this.entity = entity;
    }

    @Override
    public void display(@NotNull Player player) {
        // Create the entity metadata objects
        Object metaEntityOther = NMS.getMetaEntityProperties(false, false, false, false, false, entity.glowing(), false);
        Object metaGravity = NMS.getMetaEntityGravity(false);
        Object metaSilenced = NMS.getMetaEntitySilenced(true);

        // Display
        super.display(player, entity.type(), metaEntityOther, metaGravity, metaSilenced);
    }

    @Override
    public void update(@NotNull Player player) {
        // Nothing to do until animations are implemented
    }

}

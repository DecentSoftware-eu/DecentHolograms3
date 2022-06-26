package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.components.line.content.objects.DecentItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IconLineRenderer extends AbstractDoubleEntityLineRenderer {

    private final DecentItemStack itemStack;

    public IconLineRenderer(@NotNull Line parent, @NotNull DecentItemStack itemStack) {
        super(parent, LineType.ICON);
        this.itemStack = itemStack;
    }

    @Override
    public void display(@NotNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Create the item metadata objects
        Object metaEntityItem = NMS.getMetaEntityProperties(false, false, false, false, false, itemStack.glowing(), false);
        Object metaItem = NMS.getMetaItemStack(item);
        Object metaGravity = NMS.getMetaEntityGravity(false);

        // Display
        super.display(player, EntityType.DROPPED_ITEM, metaEntityItem, metaItem, metaGravity);
    }

    @Override
    public void update(@NotNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Create the item metadata objects
        Object metaItem = NMS.getMetaItemStack(item);

        // Send the metadata
        NMS.sendEntityMetadata(player, eidOther, metaItem);
    }

}

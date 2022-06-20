package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.api.component.line.content.objects.LineItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IconLineRenderer extends AbstractDoubleEntityLineRenderer {

    private final LineItemStack itemStack;
    private final boolean glowing;

    public IconLineRenderer(@NotNull Line parent, @NotNull LineItemStack itemStack) {
        this(parent, itemStack, false);
    }

    public IconLineRenderer(@NotNull Line parent, @NotNull LineItemStack itemStack, boolean glowing) {
        super(parent, LineType.ICON);
        this.itemStack = itemStack;
        this.glowing = glowing;
    }

    @Override
    public void display(@NotNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Create the item metadata objects
        Object metaEntityItem = NMS.getMetaEntityProperties(false, false, false, false, false, glowing, false);
        Object metaItem = NMS.getMetaItemStack(item);

        // Display
        super.display(player, EntityType.DROPPED_ITEM, metaEntityItem, metaItem);
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

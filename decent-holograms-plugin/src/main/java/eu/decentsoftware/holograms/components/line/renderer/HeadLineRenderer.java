package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.components.line.content.objects.DecentItemStack;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HeadLineRenderer extends AbstractLineRenderer {

    private final DecentItemStack itemStack;
    private final boolean small;
    private final int eid;

    public HeadLineRenderer(@NotNull Line parent, @NotNull DecentItemStack itemStack) {
        this(parent, itemStack, LineType.HEAD, false);
    }

    public HeadLineRenderer(@NotNull Line parent, @NotNull DecentItemStack itemStack, @NotNull LineType type, boolean small) {
        super(parent, type);
        this.itemStack = itemStack;
        this.small = small;
        this.eid = DecentHolograms.getFreeEntityId();
    }

    @Override
    public void display(@NotNull Player player) {
        Location loc = getParent().getPositionManager().getActualLocation();
        ItemStack item = itemStack.toItemStack(player);

        // Create the metadata objects
        Object metaEntity = NMS.getMetaEntityProperties(false, false, false, false, true, false, false);
        Object metaArmorStand = NMS.getMetaArmorStandProperties(small, false, true, true);
        Object metaNameVisible = NMS.getMetaEntityCustomNameVisible(false);
        Object metaGravity = NMS.getMetaEntityGravity(false);

        // Spawn the fake armor stand entity
        NMS.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, loc);
        // Send the metadata
        NMS.sendEntityMetadata(player, eid, metaEntity, metaGravity, metaArmorStand, metaNameVisible);
        // Set the helmet
        NMS.setHelmet(player, eid, item);
    }

    @Override
    public void update(@NotNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Set the helmet
        NMS.setHelmet(player, eid, item);
    }

    @Override
    public void hide(@NotNull Player player) {
        // Remove the entity
        NMS.removeEntity(player, eid);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        Location loc = getParent().getPositionManager().getActualLocation();

        // Teleport the armor stand
        NMS.teleportEntity(player, eid, loc, false);
    }

}

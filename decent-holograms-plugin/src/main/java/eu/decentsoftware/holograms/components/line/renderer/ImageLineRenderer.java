package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.image.DecentImage;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ImageLineRenderer extends AbstractLineRenderer {

    private final DecentImage image;
    private final int[] entityIds;

    public ImageLineRenderer(@NotNull Line parent, @NotNull DecentImage image) {
        super(parent, LineType.IMAGE);
        this.image = image;
        this.entityIds = new int[image.getWidth() * image.getHeight()];
        for (int i = 0; i < image.getWidth() * image.getHeight(); i++) {
            this.entityIds[i] = super.getFreeEntityId();
        }
    }

    @Override
    public double getHeight() {
        return entityIds.length * 0.25d;
    }

    @Override
    public void display(@NotNull Player player) {
        Location loc = getParent().getPositionManager().getActualLocation();
        String[] lines = image.toStringLines();
        for (int i = 0; i < lines.length; i++) {
            int eid = this.entityIds[i];
            String formattedText = Common.colorize(lines[i]);

            // Create the armor stand metadata objects
            Object metaEntity = NMS.getMetaEntityProperties(false, false, false, false, true, false, false);
            Object metaArmorStand = NMS.getMetaArmorStandProperties(false, false, true, true);
            Object metaNameVisible = NMS.getMetaEntityCustomNameVisible(true);
            Object metaName = NMS.getMetaEntityCustomName(formattedText);
            Object metaGravity = NMS.getMetaEntityGravity(true);

            // Spawn the fake armor stand entity
            NMS.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, loc);
            // Send the metadata
            NMS.sendEntityMetadata(player, eid, metaEntity, metaArmorStand, metaNameVisible, metaName, metaGravity);

            // Update the location
            loc.subtract(0, 0.25, 0);
        }
    }

    @Override
    public void update(@NotNull Player player) {
        String[] lines = image.toStringLines();
        for (int i = 0; i < lines.length; i++) {
            int eid = this.entityIds[i];
            String formattedText = Common.colorize(lines[i]);

            // Create the armor stand metadata objects
            Object metaName = NMS.getMetaEntityCustomName(formattedText);

            // Send the metadata
            NMS.sendEntityMetadata(player, eid, metaName);
        }
    }

    @Override
    public void hide(@NotNull Player player) {
        for (int eid : this.entityIds) {
            NMS.removeEntity(player, eid);
        }
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        Location loc = location.clone();
        for (int eid : this.entityIds) {
            NMS.teleportEntity(player, eid, loc, false);

            // Update the location
            loc.subtract(0, 0.25, 0);
        }
    }

}

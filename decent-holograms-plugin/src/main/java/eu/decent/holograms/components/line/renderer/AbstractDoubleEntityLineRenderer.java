package eu.decent.holograms.components.line.renderer;

import eu.decent.holograms.api.component.line.Line;
import eu.decent.holograms.api.component.line.LineType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A utility class for rendering lines that contain two entities, where the first entity
 * is the main entity and the second entity is a passenger of the main entity. In this
 * case, the main entity is the armor stand. This class was created to avoid code duplication.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class AbstractDoubleEntityLineRenderer extends AbstractLineRenderer {

    protected final int eid;
    protected final int eidOther;

    public AbstractDoubleEntityLineRenderer(@NotNull Line parent, @NotNull LineType type) {
        super(parent, type);
        this.eid = getFreeEntityId();
        this.eidOther = getFreeEntityId();
    }

    /**
     * A utility method for displaying the line. This method was created to avoid code duplication. It
     * handles the display of the main entity and the passenger entity.
     *
     * @param player The player to display the line to.
     * @param typeOther The type of the passenger entity.
     * @param metaOther The metadata of the passenger entity. (Gravity is set to false by default; no need to set it)
     */
    protected void display(@NotNull Player player, @NotNull EntityType typeOther, @NotNull Object... metaOther) {
        Location loc = getParent().getPositionManager().getActualLocation();

        // Create the armor stand metadata objects
        Object metaEntity = NMS.getMetaEntityProperties(false, false, false, false, true, false, false);
        Object metaArmorStand = NMS.getMetaArmorStandProperties(false, false, true, true);
        Object metaNameVisible = NMS.getMetaEntityCustomNameVisible(false);
        Object metaGravity = NMS.getMetaEntityGravity(false);

        // Spawn the fake armor stand entity
        NMS.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, loc);
        // Send the metadata
        NMS.sendEntityMetadata(player, eid, metaEntity, metaGravity, metaArmorStand, metaNameVisible);

        // Spawn the passenger entity
        if (typeOther.isAlive()) {
            NMS.spawnEntityLiving(player, eidOther, UUID.randomUUID(), typeOther, loc);
        } else {
            NMS.spawnEntity(player, eidOther, UUID.randomUUID(), typeOther, loc);
        }
        // Send the metadata
        NMS.sendEntityMetadata(player, eidOther, metaOther, metaGravity);

        // Add the other entity to the armor stand
        NMS.updatePassengers(player, eid, eidOther);
    }

    @Override
    public void hide(@NotNull Player player) {
        // Remove the entity from the armor stand
        NMS.updatePassengers(player, eid);
        // Remove the armor stand for the player
        NMS.removeEntity(player, eid);
        // Remove the entity for the player
        NMS.removeEntity(player, eidOther);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        // Remove the entity from the armor stand
        NMS.updatePassengers(player, eid);
        // Teleport the armor stand to the new location
        NMS.teleportEntity(player, eid, location, false);
        // Add the entity to the armor stand
        NMS.updatePassengers(player, eid, eidOther);
    }

}

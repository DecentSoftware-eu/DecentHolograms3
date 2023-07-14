/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.hologram.line.renderer;

import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineType;
import eu.decentsoftware.holograms.nms.NMSAdapter;
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
public abstract class DoubleEntityLineRenderer extends LineRenderer {

    protected final int eid;
    protected final int eidOther;

    public DoubleEntityLineRenderer(@NotNull NMSAdapter nmsAdapter, @NotNull HologramLine parent, @NotNull HologramLineType type) {
        super(nmsAdapter, parent, type);
        this.eid = nmsAdapter.getFreeEntityId();
        this.eidOther = nmsAdapter.getFreeEntityId();
    }

    /**
     * A utility method for displaying the line. This method was created to avoid code duplication. It
     * handles the display of the main entity and the passenger entity.
     *
     * @param player The player to display the line to.
     * @param typeOther The type of the passenger entity.
     * @param metaOther The metadata of the passenger entity.
     */
    protected void display(@NotNull Player player, @NotNull Location location, @NotNull EntityType typeOther, @NotNull Object... metaOther) {
        // Create the armor stand metadata objects
        Object metaEntity = nmsAdapter.getMetaEntityProperties(false, false, false,
                false, true, false, false);
        Object metaArmorStand = nmsAdapter.getMetaArmorStandProperties(false, false, true,
                true);
        Object metaNameVisible = nmsAdapter.getMetaEntityCustomNameVisible(false);

        // Spawn the fake armor stand entity
        nmsAdapter.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, location);
        // Send the metadata
        nmsAdapter.sendEntityMetadata(player, eid, metaEntity, metaArmorStand, metaNameVisible);

        // Spawn the passenger entity
        if (typeOther.isAlive()) {
            nmsAdapter.spawnEntityLiving(player, eidOther, UUID.randomUUID(), typeOther, location);
        } else {
            nmsAdapter.spawnEntity(player, eidOther, UUID.randomUUID(), typeOther, location);
        }
        // Send the metadata
        nmsAdapter.sendEntityMetadata(player, eidOther, metaOther);

        // Add the other entity to the armor stand
        nmsAdapter.updatePassengers(player, eid, eidOther);
    }

    @Override
    public void hide(@NotNull Player player) {
        // Remove the entity from the armor stand
        nmsAdapter.updatePassengers(player, eid);
        // Remove the armor stand for the player
        nmsAdapter.removeEntity(player, eid);
        // Remove the entity for the player
        nmsAdapter.removeEntity(player, eidOther);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        // Remove the entity from the armor stand
        nmsAdapter.updatePassengers(player, eid);
        // Teleport the armor stand to the new location
        nmsAdapter.teleportEntity(player, eid, location, false);
        // Add the entity to the armor stand
        nmsAdapter.updatePassengers(player, eid, eidOther);
    }

}

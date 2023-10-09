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

package eu.decentsoftware.holograms.core.line.renderer;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.HologramLineType;
import eu.decentsoftware.holograms.core.CoreHologramEntityIDManager;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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

    public DoubleEntityLineRenderer(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramLine parent,
            @NonNull HologramLineType type
    ) {
        super(plugin, parent, type);
        CoreHologramEntityIDManager entityIDManager = parent.getParent().getParent().getEntityIDManager();
        int lineIndex = parent.getParent().getIndex(parent);
        this.eid = entityIDManager.getEntityId(lineIndex, 0);
        this.eidOther = entityIDManager.getEntityId(lineIndex, 1);
    }

    /**
     * A utility method for displaying the line. This method was created to avoid code duplication. It
     * handles the display of the main entity and the passenger entity.
     *
     * @param player    The player to display the line to.
     * @param typeOther The type of the passenger entity.
     * @param metaOther The metadata of the passenger entity.
     */
    protected void display(@NonNull Player player, @NonNull Location location, @NonNull EntityType typeOther, @NonNull Object... metaOther) {
        Object metaEntity = this.nmsAdapter.getMetaEntityProperties(
                false,
                false,
                false,
                false,
                true,
                false,
                false
        );
        Object metaArmorStand = this.nmsAdapter.getMetaArmorStandProperties(
                false,
                false,
                true,
                true
        );
        Object metaNameVisible = this.nmsAdapter.getMetaEntityCustomNameVisible(false);

        this.nmsAdapter.spawnEntityLiving(player, this.eid, UUID.randomUUID(), EntityType.ARMOR_STAND, location);
        this.nmsAdapter.sendEntityMetadata(player, this.eid, metaEntity, metaArmorStand, metaNameVisible);

        if (typeOther.isAlive()) {
            this.nmsAdapter.spawnEntityLiving(player, this.eidOther, UUID.randomUUID(), typeOther, location);
        } else {
            this.nmsAdapter.spawnEntity(player, this.eidOther, UUID.randomUUID(), typeOther, location);
        }
        this.nmsAdapter.sendEntityMetadata(player, this.eidOther, metaOther);

        this.nmsAdapter.updatePassengers(player, this.eid, this.eidOther);
    }

    @Override
    public void hide(@NonNull Player player) {
        this.nmsAdapter.updatePassengers(player, this.eid);
        this.nmsAdapter.removeEntity(player, this.eid);
        this.nmsAdapter.removeEntity(player, this.eidOther);
    }

    @Override
    public void updateLocation(@NonNull Player player, @NonNull Location location) {
        this.nmsAdapter.updatePassengers(player, this.eid);
        this.nmsAdapter.teleportEntity(player, this.eid, location, true);
        this.nmsAdapter.updatePassengers(player, this.eid, this.eidOther);
    }

}

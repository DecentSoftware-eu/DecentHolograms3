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
import eu.decentsoftware.holograms.hologram.line.content.objects.DecentItemStack;
import eu.decentsoftware.holograms.nms.EntityEquipmentSlot;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HeadLineRenderer extends LineRenderer {

    private final @NotNull DecentItemStack itemStack;
    private final boolean small;
    private final int eid;

    public HeadLineRenderer(@NotNull HologramLine parent, @NotNull DecentItemStack itemStack) {
        this(parent, itemStack, HologramLineType.HEAD, false);
    }

    public HeadLineRenderer(@NotNull HologramLine parent, @NotNull DecentItemStack itemStack, @NotNull HologramLineType type, boolean small) {
        super(parent, type);
        this.itemStack = itemStack;
        this.small = small;
        this.eid = NMS.getFreeEntityId();
    }

    @Override
    public void display(@NotNull Player player) {
        Location loc = getParent().getPositionManager().getActualLocation();
        ItemStack item = itemStack.toItemStack(player);

        // Create the metadata objects
        Object metaEntity = NMS.getMetaEntityProperties(false, false, false,
                false, true, false, false);
        Object metaArmorStand = NMS.getMetaArmorStandProperties(small, false, true, true);
        Object metaNameVisible = NMS.getMetaEntityCustomNameVisible(false);

        // Spawn the fake armor stand entity
        NMS.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, loc);
        // Send the metadata
        NMS.sendEntityMetadata(player, eid, metaEntity, metaArmorStand, metaNameVisible);
        // Set the helmet
        NMS.setEquipment(player, eid, EntityEquipmentSlot.HEAD, item);
    }

    @Override
    public void update(@NotNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Set the helmet
        NMS.setEquipment(player, eid, EntityEquipmentSlot.HEAD, item);
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

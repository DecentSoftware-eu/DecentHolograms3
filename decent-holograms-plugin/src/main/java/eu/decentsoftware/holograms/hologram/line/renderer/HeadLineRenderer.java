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

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineType;
import eu.decentsoftware.holograms.hologram.line.content.objects.DecentItemStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class HeadLineRenderer extends LineRenderer {

    private @NotNull DecentItemStack itemStack;
    private boolean small;
    
    @Getter(AccessLevel.NONE)
    private final int eid;

    public HeadLineRenderer(@NotNull DecentHolograms plugin, @NotNull HologramLine parent, @NotNull DecentItemStack itemStack) {
        this(plugin, parent, itemStack, HologramLineType.HEAD, false);
    }

    HeadLineRenderer(@NotNull DecentHolograms plugin, @NotNull HologramLine parent, @NotNull DecentItemStack itemStack, @NotNull HologramLineType type, boolean small) {
        super(plugin, parent, type);
        this.itemStack = itemStack;
        this.small = small;
        this.eid = nmsAdapter.getFreeEntityId();
    }

    @Override
    public void display(@NotNull Player player) {
        Location loc = getParent().getPositionManager().getActualLocation();
        ItemStack item = itemStack.toItemStack(player);

        // Create the metadata objects
        Object metaEntity = nmsAdapter.getMetaEntityProperties(false, false, false,
                false, true, itemStack.glowing(), false);
        Object metaArmorStand = nmsAdapter.getMetaArmorStandProperties(small, false, true, true);
        Object metaNameVisible = nmsAdapter.getMetaEntityCustomNameVisible(false);

        // Spawn the fake armor stand entity
        nmsAdapter.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, loc);
        // Send the metadata
        nmsAdapter.sendEntityMetadata(player, eid, metaEntity, metaArmorStand, metaNameVisible);
        // Set the helmet
        nmsAdapter.setEquipment(player, eid, EquipmentSlot.HEAD, item);
    }

    @Override
    public void update(@NotNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Set the helmet
        nmsAdapter.setEquipment(player, eid, EquipmentSlot.HEAD, item);
    }

    @Override
    public void hide(@NotNull Player player) {
        // Remove the entity
        nmsAdapter.removeEntity(player, eid);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        Location loc = getParent().getPositionManager().getActualLocation();

        // Teleport the armor stand
        nmsAdapter.teleportEntity(player, eid, loc, true);
    }

}

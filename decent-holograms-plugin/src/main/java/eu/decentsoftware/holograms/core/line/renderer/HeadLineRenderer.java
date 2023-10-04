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
import eu.decentsoftware.holograms.content.DecentItemStack;
import eu.decentsoftware.holograms.core.CoreHologramEntityIDManager;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
public class HeadLineRenderer extends LineRenderer {

    private DecentItemStack itemStack;
    private boolean small;

    @Getter(AccessLevel.NONE)
    private final int eid;

    public HeadLineRenderer(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramLine parent,
            @NonNull DecentItemStack itemStack
    ) {
        this(plugin, parent, itemStack, HologramLineType.HEAD, false);
    }

    HeadLineRenderer(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramLine parent,
            @NonNull DecentItemStack itemStack,
            @NonNull HologramLineType type,
            boolean small
    ) {
        super(plugin, parent, type);
        this.itemStack = itemStack;
        this.small = small;
        CoreHologramEntityIDManager entityIDManager = parent.getParent().getParent().getEntityIDManager();
        int index = parent.getParent().getIndex(parent);
        this.eid = entityIDManager.getEntityId(index, 0);
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public void tick(@NonNull Collection<Player> viewers) {

    }

    @Override
    public void display(@NonNull Player player) {
        ItemStack item = this.itemStack.toItemStack(player);

        // Create the metadata objects
        Object metaEntity = this.nmsAdapter.getMetaEntityProperties(false, false, false,
                false, true, this.itemStack.glowing(), false);
        Object metaArmorStand = this.nmsAdapter.getMetaArmorStandProperties(this.small, false, true, true);
        Object metaNameVisible = this.nmsAdapter.getMetaEntityCustomNameVisible(false);

        // Spawn the fake armor stand entity
        this.nmsAdapter.spawnEntityLiving(player, this.eid, UUID.randomUUID(), EntityType.ARMOR_STAND, this.parent.getActualBukkitLocation());
        // Send the metadata
        this.nmsAdapter.sendEntityMetadata(player, this.eid, metaEntity, metaArmorStand, metaNameVisible);
        // Set the helmet
        this.nmsAdapter.setEquipment(player, this.eid, EquipmentSlot.HEAD, item);
    }

    @Override
    public void updateContent(@NonNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Set the helmet
        this.nmsAdapter.setEquipment(player, this.eid, EquipmentSlot.HEAD, item);
    }

    @Override
    public void hide(@NonNull Player player) {
        // Remove the entity
        this.nmsAdapter.removeEntity(player, this.eid);
    }

    @Override
    public void updateLocation(@NonNull Player player) {
        // Teleport the armor stand
        this.nmsAdapter.teleportEntity(player, this.eid, this.parent.getActualBukkitLocation(), true);
    }

}

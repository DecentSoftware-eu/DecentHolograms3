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

package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.line.HologramLineType;
import eu.decentsoftware.holograms.components.line.content.objects.DecentItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IconLineRenderer extends AbstractDoubleEntityLineRenderer {

    private final @NotNull DecentItemStack itemStack;

    public IconLineRenderer(@NotNull HologramLine parent, @NotNull DecentItemStack itemStack) {
        super(parent, HologramLineType.ICON);
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

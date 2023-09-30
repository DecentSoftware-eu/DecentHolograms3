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
import eu.decentsoftware.holograms.ticker.Ticked;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class IconLineRenderer extends DoubleEntityLineRenderer implements Ticked {

    private @NotNull DecentItemStack itemStack;

    public IconLineRenderer(DecentHolograms plugin, @NotNull HologramLine parent, @NotNull DecentItemStack itemStack) {
        super(plugin, parent, HologramLineType.ICON);
        this.itemStack = itemStack;
    }

    @Override
    public void tick() {

    }

    @Override
    public void display(@NotNull Player player) {
        ItemStack item = itemStack.toItemStack(player);
        Location location = getParent().getPositionManager().getActualLocation();

        // Create the item metadata objects
        Object metaEntityItem = nmsAdapter.getMetaEntityProperties(false, false, false,
                false, false, itemStack.glowing(), false);
        Object metaItem = nmsAdapter.getMetaItemStack(item);

        // Display
        super.display(player, location, EntityType.DROPPED_ITEM, metaEntityItem, metaItem);
    }

    @Override
    public void update(@NotNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Create the item metadata objects
        Object metaItem = nmsAdapter.getMetaItemStack(item);

        // Send the metadata
        nmsAdapter.sendEntityMetadata(player, eidOther, metaItem);
    }

}

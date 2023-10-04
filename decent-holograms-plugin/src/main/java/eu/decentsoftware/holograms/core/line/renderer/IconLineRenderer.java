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
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

@Getter
@Setter
public class IconLineRenderer extends DoubleEntityLineRenderer {

    private DecentItemStack itemStack;

    public IconLineRenderer(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramLine parent,
            @NonNull DecentItemStack itemStack
    ) {
        super(plugin, parent, HologramLineType.ICON);
        this.itemStack = itemStack;
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
        ItemStack item = itemStack.toItemStack(player);

        // Create the item metadata objects
        Object metaEntityItem = nmsAdapter.getMetaEntityProperties(false, false, false,
                false, false, itemStack.glowing(), false);
        Object metaItem = nmsAdapter.getMetaItemStack(item);

        // Display
        super.display(player, parent.getActualBukkitLocation(), EntityType.DROPPED_ITEM, metaEntityItem, metaItem);
    }

    @Override
    public void updateContent(@NonNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        // Create the item metadata objects
        Object metaItem = nmsAdapter.getMetaItemStack(item);

        // Send the metadata
        nmsAdapter.sendEntityMetadata(player, eidOther, metaItem);
    }

}

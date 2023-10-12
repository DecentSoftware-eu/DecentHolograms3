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
import eu.decentsoftware.holograms.ticker.Ticked;
import lombok.NonNull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IconLineRenderer extends DoubleEntityLineRenderer implements Ticked {

    private DecentItemStack itemStack;

    public IconLineRenderer(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramLine parent,
            @NonNull DecentItemStack itemStack
    ) {
        super(plugin, parent, HologramLineType.ICON);
        this.itemStack = itemStack;
        this.startTicking();
    }

    @Override
    public void destroy() {
        this.stopTicking();
        super.destroy();
    }

    @Override
    public void tick() {

    }

    @Override
    public double getHeight() {
        return 0.6d;
    }

    @Override
    public double getWidth(@NonNull Player player) {
        return 0.6d;
    }

    @Override
    public void display(@NonNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        Object metaEntityItem = nmsAdapter.getMetaEntityProperties(
                false,
                false,
                false,
                false,
                false,
                itemStack.glowing(),
                false
        );
        Object metaItem = nmsAdapter.getMetaItemStack(item);

        super.display(player, parent.getActualBukkitLocation(), EntityType.DROPPED_ITEM, metaEntityItem, metaItem);
    }

    @Override
    public void updateContent(@NonNull Player player) {
        ItemStack item = itemStack.toItemStack(player);

        Object metaItem = nmsAdapter.getMetaItemStack(item);

        nmsAdapter.sendEntityMetadata(player, getEntityId(1), metaItem);
    }

    public void setItemStack(@NonNull DecentItemStack itemStack) {
        this.itemStack = itemStack;
    }

}

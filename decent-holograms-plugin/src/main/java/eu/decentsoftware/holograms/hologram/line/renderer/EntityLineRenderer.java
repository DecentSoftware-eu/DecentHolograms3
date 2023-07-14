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
import eu.decentsoftware.holograms.hologram.line.content.objects.DecentEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class EntityLineRenderer extends DoubleEntityLineRenderer {

    private @NotNull DecentEntity entity;

    public EntityLineRenderer(@NotNull HologramLine parent, @NotNull DecentEntity entity) {
        super(parent, HologramLineType.ENTITY);
        this.entity = entity;
    }

    @Override
    public void display(@NotNull Player player) {
        Location location = getParent().getPositionManager().getActualLocation();

        // Create the entity metadata objects
        Object metaEntityOther = nmsAdapter.getMetaEntityProperties(false, false, false,
                false, false, entity.glowing(), false);
        Object metaSilenced = nmsAdapter.getMetaEntitySilenced(true);

        // Display
        super.display(player, location, entity.type(), metaEntityOther, metaSilenced);
    }

    @Override
    public void update(@NotNull Player player) {
        // Nothing to do until animations are implemented
    }

}

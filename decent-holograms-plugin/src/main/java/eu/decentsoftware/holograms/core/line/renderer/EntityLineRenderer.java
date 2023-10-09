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
import eu.decentsoftware.holograms.content.DecentEntity;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class EntityLineRenderer extends DoubleEntityLineRenderer {

    private DecentEntity entity;

    public EntityLineRenderer(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramLine parent,
            @NonNull DecentEntity entity
    ) {
        super(plugin, parent, HologramLineType.ENTITY);
        this.entity = entity;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth(@NonNull Player player) {
        return 0;
    }

    @Override
    public void display(@NonNull Player player) {
        Object metaEntityOther = this.nmsAdapter.getMetaEntityProperties(
                false,
                false,
                false,
                false,
                false,
                this.entity.glowing(),
                false
        );
        Object metaSilenced = this.nmsAdapter.getMetaEntitySilenced(true);

        super.display(player, this.parent.getActualBukkitLocation(), this.entity.type(), metaEntityOther, metaSilenced);
    }

    @Override
    public void updateContent(@NonNull Player player) {
        // Nothing to do until animations are implemented
    }

    public void setEntity(@NonNull DecentEntity entity) {
        this.entity = entity;
    }

}

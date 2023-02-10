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

package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.DecentHologramsPlugin;
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.components.line.content.objects.DecentEntity;
import eu.decentsoftware.holograms.components.line.renderer.EntityLineRenderer;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import org.jetbrains.annotations.NotNull;

public class EntityContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull HologramLine line) {
        String content = line.getContent();
        if (content == null || !content.startsWith("#ENTITY:")) {
            return false;
        }
        content = content.substring("#ENTITY:".length());

        DecentEntity entity = DecentEntity.fromString(content);

        EntityLineRenderer renderer = new EntityLineRenderer(line, entity);
        line.setRenderer(renderer);
        NMSAdapter nmsAdapter = DecentHologramsPlugin.getInstance().getNMSManager().getAdapter();
        line.getPositionManager().getOffsets().setY(nmsAdapter.getEntityHeight(entity.type()));
        return true;
    }

}

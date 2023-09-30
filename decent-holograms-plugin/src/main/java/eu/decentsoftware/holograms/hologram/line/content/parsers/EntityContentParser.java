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

package eu.decentsoftware.holograms.hologram.line.content.parsers;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.hologram.line.content.objects.DecentEntity;
import eu.decentsoftware.holograms.hologram.line.renderer.EntityLineRenderer;
import eu.decentsoftware.holograms.hologram.line.renderer.LineRenderer;
import org.jetbrains.annotations.NotNull;

public class EntityContentParser implements ContentParser {

    private final DecentHolograms plugin;

    public EntityContentParser(DecentHolograms plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean parse(@NotNull HologramLine line) {
        String content = line.getContent();
        if (content == null || !content.startsWith("#ENTITY:")) {
            return false;
        }
        content = content.substring("#ENTITY:".length());

        DecentEntity entity = DecentEntity.fromString(content.substring("#ENTITY:".length()));
        LineRenderer renderer = (LineRenderer) line.getRenderer();
        if (renderer instanceof EntityLineRenderer) {
            ((EntityLineRenderer) line.getRenderer()).setEntity(entity);
            renderer.updateAll();
            return true;
        } else if (renderer != null) {
            renderer.hideAll();
        }

        renderer = new EntityLineRenderer(plugin, line, entity);
        line.setRenderer(renderer);
        double entityHeight = plugin.getNMSManager().getAdapter().getEntityHeight(entity.type());
        line.getPositionManager().getOffsets().setY(entityHeight);
        line.getSettings().setHeight(entityHeight);
        renderer.displayAll();
        return true;
    }

}

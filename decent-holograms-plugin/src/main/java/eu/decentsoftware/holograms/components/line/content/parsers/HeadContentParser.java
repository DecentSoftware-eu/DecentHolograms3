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

import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.line.HologramLineRenderer;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.components.line.content.objects.DecentItemStack;
import eu.decentsoftware.holograms.components.line.renderer.HeadLineRenderer;
import org.jetbrains.annotations.NotNull;

public class HeadContentParser implements ContentParser {

    @Override
    public boolean parse(@NotNull HologramLine line) {
        String content = line.getContent();
        if (content == null || !content.startsWith("#HEAD:")) {
            return false;
        }
        DecentItemStack itemStack = DecentItemStack.fromString(content);
        HologramLineRenderer renderer = new HeadLineRenderer(line, itemStack);
        line.setRenderer(renderer);
        line.getPositionManager().getOffsets().setY(-2.0d);
        line.getSettings().setHeight(0.75d);
        return true;
    }

}

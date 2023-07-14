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

import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.hologram.line.content.objects.DecentItemStack;
import eu.decentsoftware.holograms.hologram.line.renderer.IconLineRenderer;
import eu.decentsoftware.holograms.hologram.line.renderer.LineRenderer;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import org.jetbrains.annotations.NotNull;

public class IconContentParser implements ContentParser {

    private final NMSAdapter nmsAdapter;

    public IconContentParser(NMSAdapter nmsAdapter) {
        this.nmsAdapter = nmsAdapter;
    }

    @Override
    public boolean parse(@NotNull HologramLine line) {
        String content = line.getContent();
        if (content == null || !content.startsWith("#ICON:")) {
            return false;
        }
        content = content.substring("#ICON:".length());

        DecentItemStack itemStack = DecentItemStack.fromString(content);
        LineRenderer renderer = (LineRenderer) line.getRenderer();
        if (renderer instanceof IconLineRenderer) {
            ((IconLineRenderer) line.getRenderer()).setItemStack(itemStack);
            renderer.updateAll();
            return true;
        } else if (renderer != null) {
            renderer.hideAll();
        }

        renderer = new IconLineRenderer(nmsAdapter, line, itemStack);
        line.setRenderer(renderer);
        line.getPositionManager().getOffsets().setY(-0.55d);
        line.getSettings().setHeight(0.6d);
        renderer.displayAll();
        return true;
    }

}

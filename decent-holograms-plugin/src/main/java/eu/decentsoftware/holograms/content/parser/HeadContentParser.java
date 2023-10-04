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

package eu.decentsoftware.holograms.content.parser;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.content.DecentItemStack;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.core.line.renderer.HeadLineRenderer;
import eu.decentsoftware.holograms.core.line.renderer.LineRenderer;
import lombok.NonNull;

public class HeadContentParser implements ContentParser {

    private final DecentHolograms plugin;

    public HeadContentParser(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean parse(@NonNull CoreHologramLine line) {
        String content = line.getContent();
        if (content == null || !content.startsWith("#HEAD:")) {
            return false;
        }

        DecentItemStack itemStack = DecentItemStack.fromString(content.substring("#HEAD:".length()));
        LineRenderer renderer = line.getRenderer();
        if (renderer instanceof HeadLineRenderer) {
            ((HeadLineRenderer) line.getRenderer()).setItemStack(itemStack);
            ((HeadLineRenderer) line.getRenderer()).setSmall(false);
            renderer.updateContentAll();
            return true;
        } else if (renderer != null) {
            renderer.hideAll();
        }

        renderer = new HeadLineRenderer(plugin, line, itemStack);
        line.setRenderer(renderer);
        line.setTypeYOffset(-2.0d);
        line.getSettings().setHeight(0.75d);
        renderer.displayAll();
        return true;
    }

}

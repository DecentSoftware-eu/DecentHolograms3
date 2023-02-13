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

package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.line.HologramLineRenderer;
import eu.decentsoftware.holograms.api.component.line.HologramLineType;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class LineRenderer implements HologramLineRenderer {

    /*
     * TODO:
     *  - Add support for animations
     *  - Add offsets to align the lines properly
     */

    protected static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    protected static final NMSAdapter NMS = PLUGIN.getNMSManager().getAdapter();

    private final @NotNull HologramLine parent;
    private final @NotNull HologramLineType type;

    public LineRenderer(@NotNull HologramLine parent, @NotNull HologramLineType type) {
        this.parent = parent;
        this.type = type;
    }

    @Override
    public double getHeight() {
        return getParent().getSettings().getHeight();
    }

    @Override
    public double getWidth() {
        return 0; // TODO
    }

    @NotNull
    @Override
    public HologramLine getParent() {
        return parent;
    }

    @NotNull
    @Override
    public HologramLineType getType() {
        return type;
    }

}

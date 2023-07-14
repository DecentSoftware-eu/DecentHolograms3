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
import eu.decentsoftware.holograms.hologram.line.content.objects.DecentItemStack;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import org.jetbrains.annotations.NotNull;

public class SmallHeadLineRenderer extends HeadLineRenderer {

    public SmallHeadLineRenderer(@NotNull NMSAdapter nmsAdapter, @NotNull HologramLine parent, @NotNull DecentItemStack itemStack) {
        super(nmsAdapter, parent, itemStack, HologramLineType.SMALL_HEAD, true);
    }

}

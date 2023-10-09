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

package eu.decentsoftware.holograms.api.hologram;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import lombok.NonNull;

public class APIHologramPage extends CoreHologramPage<APIHologramLine> implements HologramPage {

    public APIHologramPage(@NonNull DecentHolograms plugin, @NonNull APIHologram parent) {
        super(plugin, parent);
    }

    @NonNull
    @Override
    protected APIHologramLine createLine(@NonNull DecentLocation location, @NonNull String content) {
        APIHologramLine line = new APIHologramLine(this.plugin, this, location);
        line.setContent(content);
        return line;
    }

    @Override
    public int getIndex(@NonNull HologramLine line) {
        if (line instanceof APIHologramLine) {
            return super.getIndex((APIHologramLine) line);
        }
        return -1;
    }

    @NonNull
    @Override
    public APIHologram getParent() {
        return (APIHologram) super.getParent();
    }
}

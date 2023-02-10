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

package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.api.component.line.HologramLineSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultHologramLineSettings implements HologramLineSettings {

    private boolean updating;
    private double height;
    private double offsetX;
    private double offsetY;
    private double offsetZ;

    /**
     * Create a new instance of {@link DefaultHologramLineSettings} with default values.
     */
    public DefaultHologramLineSettings() {
        this.updating = true;
        this.height = 0.3d;
        this.offsetX = 0d;
        this.offsetY = 0d;
        this.offsetZ = 0d;
    }

}

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

package eu.decentsoftware.holograms.core;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class CoreHologramSettings {

    private boolean enabled;
    private boolean interactive = false;
    private boolean downOrigin = false;
    private boolean rotateHorizontal = true;
    private boolean rotateVertical = true;
    private int viewDistance = 48;
    private int updateDistance = 48;
    private int updateInterval = 20;
    private boolean updating = true;

    @Contract(pure = true)
    public CoreHologramSettings(boolean enabled) {
        this.enabled = enabled;
    }

    public void set(@NotNull CoreHologramSettings settings) {
        this.enabled = settings.isEnabled();
        this.interactive = settings.isInteractive();
        this.downOrigin = settings.isDownOrigin();
        this.rotateHorizontal = settings.isRotateHorizontal();
        this.rotateVertical = settings.isRotateVertical();
        this.viewDistance = settings.getViewDistance();
        this.updateDistance = settings.getUpdateDistance();
        this.updateInterval = settings.getUpdateInterval();
        this.updating = settings.isUpdating();
    }

}

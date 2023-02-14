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

package eu.decentsoftware.holograms.hologram;

import eu.decentsoftware.holograms.api.hologram.HologramSettings;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class DefaultHologramSettings implements HologramSettings {

    private boolean enabled;
    private transient boolean persistent;
    private boolean editable;
    private boolean interactive;
    private boolean downOrigin;
    private boolean rotateHorizontal;
    private boolean rotateVertical;
    private boolean rotateHeads;
    private int viewDistance;
    private int updateDistance;
    private int updateInterval;
    private boolean updating;

    /**
     * Creates a new instance of {@link DefaultHologramSettings} with default values.
     *
     * @param enabled    Whether the hologram is enabled.
     * @param persistent Whether the hologram is persistent.
     */
    @Contract(pure = true)
    public DefaultHologramSettings(boolean enabled, boolean persistent) {
        this.enabled = enabled;
        this.persistent = persistent;
        this.downOrigin = false;
        this.editable = true;
        this.interactive = false;
        this.rotateHorizontal = true;
        this.rotateVertical = true;
        this.rotateHeads = true;
        this.viewDistance = 48;
        this.updateDistance = 48;
        this.updateInterval = 20;
        this.updating = true;
    }

    @Override
    public void set(@NotNull HologramSettings settings) {
        this.enabled = settings.isEnabled();
        this.persistent = settings.isPersistent();
        this.editable = settings.isEditable();
        this.interactive = settings.isInteractive();
        this.downOrigin = settings.isDownOrigin();
        this.rotateHorizontal = settings.isRotateHorizontal();
        this.rotateVertical = settings.isRotateVertical();
        this.rotateHeads = settings.isRotateHeads();
        this.viewDistance = settings.getViewDistance();
        this.updateDistance = settings.getUpdateDistance();
        this.updateInterval = settings.getUpdateInterval();
        this.updating = settings.isUpdating();
    }

}

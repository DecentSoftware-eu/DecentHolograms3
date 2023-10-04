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
import eu.decentsoftware.holograms.api.hologram.click.ClickHandler;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.core.CoreHologram;
import lombok.NonNull;

public class APIHologram extends CoreHologram<APIHologramPage> implements Hologram {

    public APIHologram(DecentHolograms plugin, DecentLocation location) {
        this(plugin, location, null);
    }

    public APIHologram(DecentHolograms plugin, DecentLocation location, ClickHandler clickHandler) {
        super(plugin);
        this.positionManager = new APIPositionManager(location);
        this.visibilityManager = new APIHologramVisibilityManager(this);
        this.settings = new APIHologramSettings(true);
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    protected APIHologramPage createPage() {
        return new APIHologramPage(plugin, this);
    }

    @NonNull
    @Override
    public APIHologramSettings getSettings() {
        return (APIHologramSettings) super.getSettings();
    }

    @NonNull
    @Override
    public APIPositionManager getPositionManager() {
        return (APIPositionManager) super.getPositionManager();
    }

    @NonNull
    @Override
    public APIHologramVisibilityManager getVisibilityManager() {
        return (APIHologramVisibilityManager) super.getVisibilityManager();
    }
}

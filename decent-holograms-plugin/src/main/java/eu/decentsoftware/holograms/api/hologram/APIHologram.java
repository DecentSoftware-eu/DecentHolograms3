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
import eu.decentsoftware.holograms.core.CoreHologram;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class APIHologram extends CoreHologram<APIHologramPage> implements Hologram {

    private HologramClickHandler clickHandler;

    public APIHologram(@NonNull DecentHolograms plugin, @NonNull DecentLocation location) {
        this(plugin, location, null);
    }

    public APIHologram(@NonNull DecentHolograms plugin, @NonNull DecentLocation location, @Nullable HologramClickHandler clickHandler) {
        super(plugin);
        this.positionManager = new APIHologramPositionManager(location);
        this.visibilityManager = new APIHologramVisibilityManager(this);
        this.settings = new APIHologramSettings(true);
        this.clickHandler = clickHandler;
    }

    @Override
    public void onClick(@NonNull Player player, @NonNull ClickType clickType, @NonNull CoreHologramPage<?> page, @NonNull CoreHologramLine line) {
        if (clickHandler != null && page instanceof APIHologramPage && line instanceof APIHologramLine) {
            clickHandler.onClick(player, clickType, (APIHologramPage) page, (APIHologramLine) line);
        }
    }

    @NonNull
    @Override
    protected APIHologramPage createPage() {
        return new APIHologramPage(plugin, this);
    }

    @Nullable
    @Override
    public HologramClickHandler getClickHandler() {
        return clickHandler;
    }

    @Override
    public void setClickHandler(@Nullable HologramClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public APIHologramSettings getSettings() {
        return (APIHologramSettings) super.getSettings();
    }

    @NonNull
    @Override
    public APIHologramPositionManager getPositionManager() {
        return (APIHologramPositionManager) super.getPositionManager();
    }

    @NonNull
    @Override
    public APIHologramVisibilityManager getVisibilityManager() {
        return (APIHologramVisibilityManager) super.getVisibilityManager();
    }
}

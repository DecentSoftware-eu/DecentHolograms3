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

package eu.decentsoftware.holograms.internal;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;
import eu.decentsoftware.holograms.core.CoreHologram;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import lombok.NonNull;

public class PluginHologramPage extends CoreHologramPage<PluginHologramLine> implements IHasClickActions {

    private final ClickConditionHolder clickConditions;
    private final ClickActionHolder clickActions;

    public PluginHologramPage(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologram<? extends CoreHologramPage<?>> parent
    ) {
        super(plugin, parent);
        this.clickConditions = new ClickConditionHolder();
        this.clickActions = new ClickActionHolder();
    }

    public PluginHologramPage(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologram<? extends CoreHologramPage<?>> parent,
            @NonNull ClickConditionHolder clickConditions,
            @NonNull ClickActionHolder clickActions
    ) {
        super(plugin, parent);
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
    }

    @NonNull
    @Override
    protected PluginHologramLine createLine(@NonNull DecentLocation location, @NonNull String content) {
        PluginHologramLine line = new PluginHologramLine(this.plugin, this, location);
        line.setContent(content);
        return line;
    }

    @NonNull
    @Override
    public ClickConditionHolder getClickConditions() {
        return this.clickConditions;
    }

    @NonNull
    @Override
    public ClickActionHolder getClickActions() {
        return this.clickActions;
    }

}
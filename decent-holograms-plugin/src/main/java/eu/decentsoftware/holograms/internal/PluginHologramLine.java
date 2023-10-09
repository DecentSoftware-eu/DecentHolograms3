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
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.core.line.CoreHologramLineSettings;
import lombok.NonNull;

public class PluginHologramLine extends CoreHologramLine {

    private final ConditionHolder viewConditions;
    private final ClickConditionHolder clickConditions;
    private final ClickActionHolder clickActions;

    public PluginHologramLine(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramPage<? extends CoreHologramLine> parent,
            @NonNull DecentLocation location
    ) {
        super(plugin, parent, location);
        this.viewConditions = new ConditionHolder();
        this.clickConditions = new ClickConditionHolder();
        this.clickActions = new ClickActionHolder();
    }

    public PluginHologramLine(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramPage<? extends CoreHologramLine> parent,
            @NonNull DecentLocation location,
            @NonNull ConditionHolder viewConditions,
            @NonNull ClickConditionHolder clickConditions,
            @NonNull ClickActionHolder clickActions
    ) {
        super(plugin, parent, location);
        this.viewConditions = viewConditions;
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
    }

    public PluginHologramLine(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramPage<? extends CoreHologramLine> parent,
            @NonNull DecentLocation location,
            @NonNull CoreHologramLineSettings settings,
            @NonNull ConditionHolder viewConditions,
            @NonNull ClickConditionHolder clickConditions,
            @NonNull ClickActionHolder clickActions
    ) {
        super(plugin, parent, location, settings);
        this.viewConditions = viewConditions;
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
    }

    @NonNull
    public ConditionHolder getViewConditions() {
        return this.viewConditions;
    }

    @NonNull
    public ClickConditionHolder getClickConditions() {
        return this.clickConditions;
    }

    @NonNull
    public ClickActionHolder getClickActions() {
        return this.clickActions;
    }

}

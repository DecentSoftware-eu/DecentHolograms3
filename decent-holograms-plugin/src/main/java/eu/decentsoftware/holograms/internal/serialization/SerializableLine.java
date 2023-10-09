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

package eu.decentsoftware.holograms.internal.serialization;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.core.line.CoreHologramLineSettings;
import eu.decentsoftware.holograms.internal.PluginHologramLine;
import eu.decentsoftware.holograms.internal.PluginHologramPage;
import lombok.NonNull;

/**
 * This class is used to (de)serialize lines from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
public class SerializableLine {

    private CoreHologramLineSettings settings;
    private ConditionHolder viewConditions;
    private ClickConditionHolder clickConditions;
    private ClickActionHolder clickActions;
    private final String content;

    public SerializableLine(@NonNull PluginHologramLine line) {
        this.settings = line.getSettings();
        this.viewConditions = line.getViewConditions();
        this.clickConditions = line.getClickConditions();
        this.clickActions = line.getClickActions();
        this.content = line.getContent();
    }

    @NonNull
    public PluginHologramLine toLine(
            @NonNull DecentHolograms plugin,
            @NonNull PluginHologramPage page,
            @NonNull DecentLocation location
    ) {
        PluginHologramLine line = new PluginHologramLine(
                plugin,
                page,
                location,
                this.getSettings(),
                this.getViewConditions(),
                this.getClickConditions(),
                this.getClickActions()
        );
        line.setContent(this.getContent());
        return line;
    }

    @NonNull
    public CoreHologramLineSettings getSettings() {
        if (settings == null) {
            settings = new CoreHologramLineSettings();
        }
        return settings;
    }

    @NonNull
    public ConditionHolder getViewConditions() {
        if (viewConditions == null) {
            viewConditions = new ConditionHolder();
        }
        return viewConditions;
    }

    @NonNull
    public ClickConditionHolder getClickConditions() {
        if (clickConditions == null) {
            clickConditions = new ClickConditionHolder();
        }
        return clickConditions;
    }

    @NonNull
    public ClickActionHolder getClickActions() {
        if (clickActions == null) {
            clickActions = new ClickActionHolder();
        }
        return clickActions;
    }

    @NonNull
    public String getContent() {
        return this.content;
    }

}

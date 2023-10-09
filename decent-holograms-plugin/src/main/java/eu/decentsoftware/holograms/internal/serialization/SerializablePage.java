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
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;
import eu.decentsoftware.holograms.internal.PluginHologram;
import eu.decentsoftware.holograms.internal.PluginHologramLine;
import eu.decentsoftware.holograms.internal.PluginHologramPage;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to (de)serialize pages from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
public class SerializablePage {

    private final List<SerializableLine> lines;
    private ClickConditionHolder clickConditions;
    private ClickActionHolder clickActions;

    public SerializablePage(@NonNull PluginHologramPage page) {
        List<SerializableLine> lines = new ArrayList<>();
        for (PluginHologramLine line : page.getLines()) {
            SerializableLine serializableLine = new SerializableLine(line);
            lines.add(serializableLine);
        }
        this.lines = lines;
        this.clickConditions = page.getClickConditions();
        this.clickActions = page.getClickActions();
    }

    @NonNull
    public PluginHologramPage toPage(@NonNull DecentHolograms plugin, @NonNull PluginHologram hologram) {
        PluginHologramPage page = new PluginHologramPage(
                plugin,
                hologram,
                this.getClickConditions(),
                this.getClickActions()
        );
        for (SerializableLine line : this.getLines()) {
            PluginHologramLine defaultLine = line.toLine(plugin, page, page.getNextLineLocation());
            page.appendLine(defaultLine);
        }
        return page;
    }

    @NonNull
    public ClickConditionHolder getClickConditions() {
        if (this.clickConditions == null) {
            this.clickConditions = new ClickConditionHolder();
        }
        return this.clickConditions;
    }

    @NonNull
    public ClickActionHolder getClickActions() {
        if (this.clickActions == null) {
            this.clickActions = new ClickActionHolder();
        }
        return this.clickActions;
    }

    @NonNull
    public List<SerializableLine> getLines() {
        return new ArrayList<>(this.lines);
    }

}

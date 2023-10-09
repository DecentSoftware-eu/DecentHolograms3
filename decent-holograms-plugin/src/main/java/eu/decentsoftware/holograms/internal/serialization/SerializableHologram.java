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
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.internal.PluginHologram;
import eu.decentsoftware.holograms.internal.PluginHologramPage;
import eu.decentsoftware.holograms.internal.PluginHologramSettings;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to (de)serialize holograms from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
public class SerializableHologram {

    private final DecentLocation location;
    private PluginHologramSettings settings;
    private ConditionHolder viewConditions;
    private final List<SerializablePage> pages;

    public SerializableHologram(@NonNull PluginHologram hologram) {
        List<SerializablePage> pages = new ArrayList<>();
        for (PluginHologramPage page : hologram.getPages()) {
            SerializablePage serializablePage = new SerializablePage(page);
            pages.add(serializablePage);
        }
        this.location = hologram.getPositionManager().getLocation();
        this.settings = hologram.getSettings();
        this.viewConditions = hologram.getViewConditions();
        this.pages = pages;
    }

    @NonNull
    public PluginHologram toHologram(@NonNull DecentHolograms plugin, @NonNull String name) {
        PluginHologram hologram = new PluginHologram(plugin, this.getLocation(), name, this.getSettings(), this.getViewConditions());
        hologram.setPages(
                this.getPages().stream()
                        .map(page -> page.toPage(plugin, hologram))
                        .collect(Collectors.toList())
        );
        return hologram;
    }

    @NonNull
    public DecentLocation getLocation() {
        return this.location;
    }

    @NonNull
    public PluginHologramSettings getSettings() {
        if (this.settings == null) {
            this.settings = new PluginHologramSettings(true);
        }
        return this.settings;
    }

    @NonNull
    public ConditionHolder getViewConditions() {
        if (this.viewConditions == null) {
            this.viewConditions = new ConditionHolder();
        }
        return this.viewConditions;
    }

    @NonNull
    public List<SerializablePage> getPages() {
        return new ArrayList<>(this.pages);
    }

}

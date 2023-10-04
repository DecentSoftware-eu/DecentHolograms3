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
import eu.decentsoftware.holograms.api.hologram.click.ClickType;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.core.CoreHologram;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.CoreHologramSettings;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;

public class PluginHologram extends CoreHologram<PluginHologramPage> {

    private final String name;
    private final PluginHologramConfig config;
    private final ConditionHolder viewConditions;

    public PluginHologram(
            @NonNull DecentHolograms plugin,
            @NonNull DecentLocation location,
            @NonNull String name
    ) {
        this(plugin, location, name, new CoreHologramSettings(true), new ConditionHolder());
    }

    public PluginHologram(
            @NonNull DecentHolograms plugin,
            @NonNull DecentLocation location,
            @NonNull String name,
            @NonNull CoreHologramSettings settings,
            @NonNull ConditionHolder viewConditions
    ) {
        super(plugin, location);
        this.name = name;
        this.config = new PluginHologramConfig(this.plugin, this);
        this.viewConditions = viewConditions;
        this.settings = settings;
    }

    @NonNull
    public PluginHologram copy(@NonNull String newName) {
        return new PluginHologram(
                this.plugin,
                this.positionManager.getLocation(),
                newName,
                this.settings,
                this.viewConditions
        );
    }

    public void delete() {
        this.destroy();
        this.config.delete();
    }

    @Override
    public void onClick(@NonNull Player player, @NonNull ClickType clickType, @NonNull CoreHologramPage<?> page, @NonNull CoreHologramLine line) {
        if (line instanceof PluginHologramLine) {
            PluginHologramLine pluginLine = (PluginHologramLine) line;
            if (pluginLine.getClickConditions().check(clickType, player)) {
                pluginLine.getClickActions().execute(clickType, player);
            }
        } else if (page instanceof PluginHologramPage) {
            PluginHologramPage pluginPage = (PluginHologramPage) page;
            if (pluginPage.getClickConditions().check(clickType, player)) {
                pluginPage.getClickActions().execute(clickType, player);
            }
        }
    }

    @NonNull
    @Override
    protected PluginHologramPage createPage() {
        return new PluginHologramPage(plugin, this);
    }

    public void setPages(@NonNull List<PluginHologramPage> pages) {
        clearPages();

        this.pages.addAll(pages);

        recalculate();
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public ConditionHolder getViewConditions() {
        return this.viewConditions;
    }

    @NonNull
    public PluginHologramConfig getConfig() {
        return this.config;
    }

}

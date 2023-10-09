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
import eu.decentsoftware.holograms.api.hologram.ClickType;
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
        this(plugin, location, name, new PluginHologramSettings(true), new ConditionHolder());
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

    /**
     * Copy this hologram to a new one with the specified name.
     *
     * @param newName The name of the new hologram.
     * @return The new hologram.
     * @throws IllegalArgumentException If a hologram with the specified name already exists.
     */
    @NonNull
    public PluginHologram copy(@NonNull String newName) {
        if (this.plugin.getHologramManager().hasHologram(newName)) {
            throw new IllegalArgumentException("Hologram with name '" + newName + "' already exists.");
        }
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
    public void onClick(
            @NonNull Player player,
            @NonNull ClickType clickType,
            @NonNull CoreHologramPage<?> page,
            @NonNull CoreHologramLine line
    ) {
        checkDestroyed();

        if (!(page instanceof PluginHologramPage) || !(line instanceof PluginHologramLine)) {
            throw new IllegalArgumentException("Invalid hologram page or line.");
        }

        PluginHologramPage pluginPage = (PluginHologramPage) page;
        if (!this.equals(pluginPage.getParent())) {
            throw new IllegalArgumentException("Hologram page is not part of this hologram.");
        }
        PluginHologramLine pluginLine = (PluginHologramLine) line;
        if (!pluginPage.equals(pluginLine.getParent())) {
            throw new IllegalArgumentException("Hologram line is not part of this hologram page.");
        }

        ActionExecutionStrategy strategy = getSettings().getActionExecutionStrategy();
        if (strategy == null) {
            strategy = ActionExecutionStrategy.ONLY_BOTTOM;
        }

        if (strategy.isBottomFirst()) {
            boolean executedLine = executeActions(pluginLine, player, clickType);
            if (strategy == ActionExecutionStrategy.BOTTOM_TO_TOP || !executedLine) {
                executeActions(pluginPage, player, clickType);
            }
        } else {
            boolean executedPage = executeActions(pluginPage, player, clickType);
            if (strategy == ActionExecutionStrategy.TOP_TO_BOTTOM || !executedPage) {
                executeActions(pluginLine, player, clickType);
            }
        }
    }

    private boolean executeActions(@NonNull PluginHologramLine line, @NonNull Player player, @NonNull ClickType clickType) {
        if (line.getClickConditions().check(clickType, player) && !line.getClickActions().isEmpty(clickType)) {
            line.getClickActions().execute(clickType, player);
            return true;
        }
        return false;
    }

    private boolean executeActions(@NonNull PluginHologramPage page, @NonNull Player player, @NonNull ClickType clickType) {
        if (page.getClickConditions().check(clickType, player) && !page.getClickActions().isEmpty(clickType)) {
            page.getClickActions().execute(clickType, player);
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    protected PluginHologramPage createPage() {
        return new PluginHologramPage(this.plugin, this);
    }

    public void setPages(@NonNull List<PluginHologramPage> pages) {
        clearPages();

        this.pages.addAll(pages);

        recalculate();
    }

    @NonNull
    @Override
    public PluginHologramSettings getSettings() {
        return (PluginHologramSettings) super.getSettings();
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

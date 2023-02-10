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

package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.DecentHologramsPlugin;
import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.PositionManager;
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.line.HologramLineRenderer;
import eu.decentsoftware.holograms.api.component.line.HologramLineSettings;
import eu.decentsoftware.holograms.api.component.line.HologramLineType;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DefaultHologramLine implements HologramLine {

    private final @NotNull Page parent;
    private final @NotNull UUID uid;
    private final @NotNull HologramLineSettings settings;
    private final @NotNull PositionManager positionManager;
    private final @NotNull ConditionHolder viewConditions;
    private final @NotNull ConditionHolder clickConditions;
    private final @NotNull ActionHolder clickActions;
    private HologramLineRenderer renderer;
    private String content;

    /**
     * Creates a new instance of {@link DefaultHologramLine}.
     *
     * @param parent   The parent page of the line.
     * @param location The location of the line.
     */
    public DefaultHologramLine(@NotNull Page parent, @NotNull Location location) {
        this.parent = parent;
        this.uid = UUID.randomUUID();
        this.settings = new DefaultHologramLineSettings();
        this.positionManager = new DefaultLinePositionManager(this, location);
        this.viewConditions = new ConditionHolder();
        this.clickConditions = new ConditionHolder();
        this.clickActions = new ActionHolder();
    }

    /**
     * Creates a new instance of {@link DefaultHologramLine}.
     *
     * @param parent          The parent page of the line.
     * @param settings        The settings of the line.
     * @param location        The location of the line.
     * @param viewConditions  The conditions that must be met to view the line.
     * @param clickConditions The conditions that must be met to click the line.
     * @param clickActions    The actions that will be executed when the line is clicked.
     * @param content         The content of the line.
     */
    public DefaultHologramLine(@NotNull Page parent, @NotNull HologramLineSettings settings, @NotNull Location location,
                               @NotNull ConditionHolder viewConditions, @NotNull ConditionHolder clickConditions,
                               @NotNull ActionHolder clickActions, @NotNull String content) {
        this.parent = parent;
        this.uid = UUID.randomUUID();
        this.settings = settings;
        this.positionManager = new DefaultLinePositionManager(this, location);
        this.viewConditions = viewConditions;
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
        this.setContent(content);
    }

    @NotNull
    @Override
    public UUID getUid() {
        return uid;
    }

    @NotNull
    @Override
    public Page getParent() {
        return parent;
    }

    @NotNull
    @Override
    public HologramLineType getType() {
        return renderer != null ? renderer.getType() : HologramLineType.UNKNOWN;
    }

    @NotNull
    @Override
    public HologramLineSettings getSettings() {
        return settings;
    }

    @NotNull
    @Override
    public PositionManager getPositionManager() {
        return positionManager;
    }

    @Nullable
    @Override
    public HologramLineRenderer getRenderer() {
        return renderer;
    }

    @Override
    public void setRenderer(@NotNull HologramLineRenderer renderer) {
        this.renderer = renderer;
    }

    @NotNull
    public ConditionHolder getViewConditionHolder() {
        return viewConditions;
    }

    @NotNull
    public ConditionHolder getClickConditionHolder() {
        return clickConditions;
    }

    @NotNull
    public ActionHolder getClickActionHolder() {
        return clickActions;
    }

    @Nullable
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(@NotNull String content) {
        this.content = content;

        // -- Parse content and update line accordingly
        DecentHologramsPlugin.getInstance().getContentParserManager().parse(this);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HologramLine other = (HologramLine) obj;
        return this.uid.equals(other.getUid());
    }

}

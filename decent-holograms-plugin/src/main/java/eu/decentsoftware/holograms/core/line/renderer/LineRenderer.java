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

package eu.decentsoftware.holograms.core.line.renderer;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.HologramLineType;
import eu.decentsoftware.holograms.core.CoreHologram;
import eu.decentsoftware.holograms.core.CoreHologramEntityIDManager;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public abstract class LineRenderer implements HologramLineRenderer {

    protected final DecentHolograms plugin;
    protected final NMSAdapter nmsAdapter;
    protected final CoreHologramLine parent;
    private final HologramLineType type;

    @Contract(pure = true)
    public LineRenderer(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramLine parent,
            @NonNull HologramLineType type
    ) {
        this.plugin = plugin;
        this.nmsAdapter = plugin.getNMSManager().getAdapter();
        this.parent = parent;
        this.type = type;
    }

    @Override
    public void destroy() {
        this.hideAll();
    }

    /**
     * Get a set of players, that are currently seeing the parent line.
     *
     * @return The set of players.
     */
    protected Set<Player> getViewerPlayers() {
        CoreHologramPage<?> parentPage = this.parent.getParent();
        CoreHologram<?> parentHologram = parentPage.getParent();
        return parentHologram.getVisibilityManager().getViewersAsPlayers(this.parent);
    }

    /**
     * Get the entity ID for the specified index of the parent line.
     *
     * @param entityIndex The index of the entity. Starts at 0.
     * @return The entity ID.
     */
    protected int getEntityId(int entityIndex) {
        CoreHologramPage<?> parentPage = this.parent.getParent();
        CoreHologram<?> parentHologram = parentPage.getParent();
        CoreHologramEntityIDManager entityIDManager = parentHologram.getEntityIDManager();
        return entityIDManager.getEntityId(parent.getIndex(), entityIndex);
    }

    /**
     * Display the line to all players, that are currently seeing the parent line.
     *
     * @see #display(Player)
     * @see #getViewerPlayers()
     */
    public void displayAll() {
        getViewerPlayers().forEach(this::display);
    }

    /**
     * Hide the line from all players, that are currently seeing the parent line.
     *
     * @see #hide(Player)
     * @see #getViewerPlayers()
     */
    public void hideAll() {
        getViewerPlayers().forEach(this::hide);
    }

    /**
     * Update the line for all players, that are currently seeing the parent line.
     *
     * @see #updateContent(Player)
     * @see #getViewerPlayers()
     */
    public void updateContentAll() {
        getViewerPlayers().forEach(this::updateContent);
    }

    @NonNull
    @Override
    public HologramLineType getType() {
        return this.type;
    }

}

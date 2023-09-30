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

package eu.decentsoftware.holograms.hologram.line.renderer;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineRenderer;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineType;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class LineRenderer implements HologramLineRenderer {

    /*
     * TODO:
     *  - Add support for animations
     */

    protected final DecentHolograms plugin;
    protected final NMSAdapter nmsAdapter;
    private final HologramLine parent;
    private final HologramLineType type;

    @Contract(pure = true)
    public LineRenderer(DecentHolograms plugin, @NotNull HologramLine parent, @NotNull HologramLineType type) {
        this.plugin = plugin;
        this.nmsAdapter = plugin.getNMSManager().getAdapter();
        this.parent = parent;
        this.type = type;
    }

    /**
     * Get a set of players, that are currently viewing the hologram.
     *
     * @return The set of players.
     */
    public Set<Player> getViewerPlayers() {
        return parent.getParent().getParent().getVisibilityManager().getViewersAsPlayers();
    }

    /**
     * Display the line to all players, that are currently viewing the hologram.
     *
     * @see #display(Player)
     * @see #getViewerPlayers()
     */
    public void displayAll() {
        getViewerPlayers().forEach(this::display);
    }

    /**
     * Update the line for all players, that are currently viewing the hologram.
     *
     * @see #update(Player)
     * @see #getViewerPlayers()
     */
    public void updateAll() {
        getViewerPlayers().forEach(this::update);
    }

    /**
     * Hide the line from all players, that are currently viewing the hologram.
     *
     * @see #hide(Player)
     * @see #getViewerPlayers()
     */
    public void hideAll() {
        getViewerPlayers().forEach(this::hide);
    }

    @Override
    public double getHeight() {
        return getParent().getSettings().getHeight();
    }

    @Override
    public double getWidth() {
        return 0; // TODO
    }

    @NotNull
    @Override
    public HologramLine getParent() {
        return parent;
    }

    @NotNull
    @Override
    public HologramLineType getType() {
        return type;
    }

}

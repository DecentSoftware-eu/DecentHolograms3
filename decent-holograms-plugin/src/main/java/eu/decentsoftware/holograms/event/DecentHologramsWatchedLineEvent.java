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

package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.core.CoreHologram;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * This event is called, when a player starts watching (hovering over)
 * a line of a hologram. It is also called when a player stops watching
 * any line of a hologram.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public class DecentHologramsWatchedLineEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final CoreHologramLine newWatchedLine;
    private final CoreHologramLine previousWatchedLine;

    /**
     * Create a new instance of this event.
     *
     * @param who                 The player, that is watching the new line and was watching the previous line.
     * @param newWatchedLine      The line, that the player is watching now.
     *                            Can be null, if the player is not watching any line.
     * @param previousWatchedLine The line, that the player was watching before watching the new line.
     *                            Can be null, if the player was not watching any line before.
     * @throws IllegalArgumentException If both new and previous watched line are null.
     */
    public DecentHologramsWatchedLineEvent(
            @NonNull Player who,
            @Nullable CoreHologramLine newWatchedLine,
            @Nullable CoreHologramLine previousWatchedLine
    ) {
        super(who);
        if (newWatchedLine == null && previousWatchedLine == null) {
            throw new IllegalArgumentException("Both new and previous watched line cannot be null.");
        }
        this.newWatchedLine = newWatchedLine;
        this.previousWatchedLine = previousWatchedLine;
    }

    /**
     * Get the hologram, that the player is/was watching.
     *
     * @return The hologram.
     */
    public CoreHologram<?> getHologram() {
        if (this.newWatchedLine != null) {
            return this.newWatchedLine.getParent().getParent();
        }
        if (this.previousWatchedLine != null) {
            return this.previousWatchedLine.getParent().getParent();
        }
        // This should never happen
        return null;
    }

    /**
     * Get the page, that the player is/was watching.
     *
     * @return The page.
     */
    public CoreHologramPage<?> getPage() {
        if (this.newWatchedLine != null) {
            return this.newWatchedLine.getParent();
        }
        if (this.previousWatchedLine != null) {
            return this.previousWatchedLine.getParent();
        }
        // This should never happen
        return null;
    }

    /**
     * Get the line, that the player is watching now.
     * <p>
     * This is the line, that the player is currently hovering over.
     *
     * @return The new watched line. Can be null, if the player is not watching any line.
     */
    @Nullable
    public CoreHologramLine getNewWatchedLine() {
        return this.newWatchedLine;
    }

    /**
     * Get the line, that the player was watching before watching the new line.
     *
     * @return The previous watched line. Can be null, if the player was not watching any line before.
     */
    @Nullable
    public CoreHologramLine getPreviousWatchedLine() {
        return this.previousWatchedLine;
    }

    @NonNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Contract(pure = true)
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}

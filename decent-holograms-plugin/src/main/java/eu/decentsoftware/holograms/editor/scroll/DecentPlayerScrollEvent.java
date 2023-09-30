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

package eu.decentsoftware.holograms.editor.scroll;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player scrolls on their mouse wheel. This event is called
 * after the {@link org.bukkit.event.player.PlayerItemHeldEvent}.
 *
 * @author d0by
 * @since 3.0.0
 */
public class DecentPlayerScrollEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final ScrollDirection scrollDirection;

    public DecentPlayerScrollEvent(@NotNull Player who, @NotNull ScrollDirection scrollDirection) {
        super(who);
        this.scrollDirection = scrollDirection;
    }

    /**
     * Get the direction the player scrolled.
     *
     * @return The direction the player scrolled.
     * @see ScrollDirection
     * @since 3.0.0
     */
    @NotNull
    public ScrollDirection getScrollDirection() {
        return scrollDirection;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}

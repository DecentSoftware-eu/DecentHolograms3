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

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.jetbrains.annotations.Contract;

public class ScrollListener implements Listener {

    @EventHandler
    public void onHeldItemChange(@NonNull PlayerItemHeldEvent event) {
        ScrollDirection scrollDirection = getScrollDirection(event.getPreviousSlot(), event.getNewSlot());
        DecentPlayerScrollEvent scrollEvent = new DecentPlayerScrollEvent(event.getPlayer(), scrollDirection);
        Bukkit.getPluginManager().callEvent(scrollEvent);
    }

    @Contract(pure = true)
    private ScrollDirection getScrollDirection(final int previousSlot, final int newSlot) {
        return ((newSlot - previousSlot + 9) % 9 <= 9 / 2) ? ScrollDirection.UP : ScrollDirection.DOWN;
    }

}

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

package eu.decentsoftware.holograms.editor.move;

import eu.decentsoftware.holograms.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This listener handles events related to moving of holograms. It is used in the editor.
 *
 * @author d0by
 * @see MoveController
 * @since 3.0.0
 */
public class MoveListener implements Listener {

    private final MoveController moveController;

    public MoveListener(MoveController moveController) {
        this.moveController = moveController;
    }

    @EventHandler
    public void onLeftClickAir(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }

        Player player = e.getPlayer();
        if (moveController.place(player)) {
            Lang.confTell(player, "editor.move.success");
        }
    }

    @EventHandler
    public void onHotbarScroll(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();

        moveController.findMovedHologram(player).ifPresent((hologram) -> {
            if (!(hologram.getPositionManager().getLocationBinder() instanceof MoveLocationBinder)) {
                return;
            }

            int newSlot = e.getNewSlot();
            int previousSlot = e.getPreviousSlot();

            boolean isScrollingUp = newSlot < previousSlot;
            if (newSlot == 0 && previousSlot == 8) {
                isScrollingUp = false;
            } else if (previousSlot == 0 && newSlot == 8) {
                isScrollingUp = true;
            }

            MoveLocationBinder binder = (MoveLocationBinder) hologram.getPositionManager().getLocationBinder();
            if (isScrollingUp) {
                binder.increaseDistance();
            } else {
                binder.decreaseDistance();
            }
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (moveController.cancel(player)) {
            Lang.confTell(player, "editor.move.cancel");
        }
    }

}

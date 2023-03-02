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

package eu.decentsoftware.holograms.editor;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.api.hologram.component.PositionManager;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.HologramContext;
import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

/**
 * This listener handles events related to moving of holograms. It is used in the editor.
 *
 * @author d0by
 * @since 3.0.0
 */
public class MoveListener implements Listener {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    @EventHandler
    public void onLeftClickAir(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }

        Player player = e.getPlayer();
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        DefaultHologram hologram = profile.getContext().getMovingHologram();
        if (hologram != null) {
            HologramContext hologramContext = hologram.getContext();
            if (hologramContext.getMover() != player.getUniqueId()) {
                profile.getContext().setMovingHologram(null);
                return;
            }

            PositionManager positionManager = hologram.getPositionManager();
            positionManager.setLocation(positionManager.getActualLocation());
            positionManager.unbindLocation();
            hologram.recalculate();

            profile.getContext().setMovingHologram(null);
            hologramContext.setMover(null);

            hologram.getConfig().save();
            Lang.confTell(player, "editor.move.finish", hologram.getName());
        }
    }

    @EventHandler
    public void onHotbarScroll(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        DefaultHologram hologram = profile.getContext().getMovingHologram();
        if (hologram != null) {
            if (hologram.getContext().getMover() != player.getUniqueId()) {
                profile.getContext().setMovingHologram(null);
                return;
            }

            int newSlot = e.getNewSlot();
            int previousSlot = e.getPreviousSlot();
            int currentDistance = hologram.getContext().getMoverDistance();

            boolean isScrollingUp = newSlot < previousSlot;
            if (newSlot == 0 && previousSlot == 8) {
                isScrollingUp = false;
            } else if (previousSlot == 0 && newSlot == 8) {
                isScrollingUp = true;
            }

            if (isScrollingUp && currentDistance < 13) {
                hologram.getContext().setMoverDistance(currentDistance + 1);
            } else if (!isScrollingUp && currentDistance > 3) {
                hologram.getContext().setMoverDistance(currentDistance - 1);
            }
        }
    }

}

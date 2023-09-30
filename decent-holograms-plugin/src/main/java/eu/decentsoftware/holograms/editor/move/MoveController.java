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

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.component.PositionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * This controller handles the interactive moving of holograms.
 *
 * @author d0by
 * @since 3.0.0
 */
public class MoveController {

    private final DecentHolograms plugin;
    private final Map<UUID, Hologram> movers = new ConcurrentHashMap<>();
    private final MoveListener listener;

    /**
     * Create a new MoveController and register the listener.
     */
    public MoveController(DecentHolograms plugin) {
        this.plugin = plugin;
        this.listener = new MoveListener(this);

        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Shutdown this controller, cancelling all move actions and unregistering the listener.
     */
    public void shutdown() {
        plugin.getHologramRegistry().getHolograms().forEach(hologram -> {
            Supplier<Location> binder = hologram.getPositionManager().getLocationBinder();
            if (binder instanceof MoveLocationBinder) {
                cancel(((MoveLocationBinder) binder).getPlayer());
            }
        });

        HandlerList.unregisterAll(listener);
    }

    /**
     * Initiate the move of the given hologram. This binds the hologram to the players location
     * and makes it follow the players view direction. The player can then SCROLL to move the hologram
     * farther or closer, SNEAK to snap it to block center, LEFT CLICK to place it or execute
     * the '/dh move' command again to cancel the move.
     *
     * @param player   The player who initiated the move.
     * @param hologram The hologram to move.
     * @see #place(Player)
     * @see #cancel(Player)
     */
    public boolean initiate(@NotNull Player player, @NotNull Hologram hologram) {
        hologram.getPositionManager().bindLocation(new MoveLocationBinder(player, hologram));
        movers.put(player.getUniqueId(), hologram);
        return true;
    }

    /**
     * Place the hologram at the current location. This will unbind the hologram from the player
     * and save the new location to the config. It will also update the hologram for all players
     * who can see it.
     *
     * @param player The player who is placing the hologram.
     * @see #initiate(Player, Hologram)
     * @see #cancel(Player)
     */
    public boolean place(@NotNull Player player) {
        Optional<Hologram> optional = findMovedHologram(player);
        if (optional.isPresent()) {
            Hologram hologram = optional.get();
            PositionManager positionManager = hologram.getPositionManager();
            positionManager.setLocation(positionManager.getActualLocation());
            positionManager.unbindLocation();

            hologram.recalculate();
            hologram.getConfig().save();

            movers.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    /**
     * Cancel the move of the hologram. This will unbind the hologram from the player and
     * put the hologram back to its original location.
     *
     * @param player The player who is cancelling the move.
     * @see #initiate(Player, Hologram)
     * @see #place(Player)
     */
    public boolean cancel(@NotNull Player player) {
        Optional<Hologram> optional = findMovedHologram(player);
        if (optional.isPresent()) {
            Hologram hologram = optional.get();
            hologram.getPositionManager().unbindLocation();
            hologram.recalculate();

            movers.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    /**
     * Find the hologram that is currently being moved by the given player.
     *
     * @param player The player who is moving the hologram.
     * @return The hologram that is being moved by the player. Empty if the player is not moving any hologram.
     */
    public Optional<Hologram> findMovedHologram(@NotNull Player player) {
        if (!movers.containsKey(player.getUniqueId())) {
            return Optional.empty();
        }
        return Optional.of(movers.get(player.getUniqueId()));
    }

}

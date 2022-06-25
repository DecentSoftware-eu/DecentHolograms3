package eu.decentsoftware.holograms.api.event;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Event, that is fired when something happens with hologram and player.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class HologramPlayerEvent extends HologramEvent {

    private final Player player;

    /**
     * Create a new instance of {@link HologramPlayerEvent}.
     *
     * @param hologram The hologram that is being affected.
     * @param player The player that is being affected.
     */
    public HologramPlayerEvent(@NotNull Hologram hologram, @NotNull Player player) {
        super(hologram);
        this.player = player;
    }

    /**
     * Get the player that is being affected.
     *
     * @return The player that is being affected.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

}

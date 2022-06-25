package eu.decentsoftware.holograms.api.event;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event, that is fired when something happens with hologram.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class HologramEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Hologram hologram;

    /**
     * Create a new instance of {@link HologramEvent}.
     *
     * @param hologram The hologram that is being affected.
     */
    public HologramEvent(@NotNull Hologram hologram) {
        this.hologram = hologram;
    }

    /**
     * Get the hologram that is being affected.
     *
     * @return The hologram that is being affected.
     */
    @NotNull
    public Hologram getHologram() {
        return hologram;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}

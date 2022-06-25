package eu.decentsoftware.holograms.api.event;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.page.Page;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event, that is fired when a player clicks on a hologram.
 *
 * @author d0by
 * @since 3.0.0
 */
public class HologramClickEvent extends HologramPlayerEvent implements Cancellable {

    private final ClickType clickType;
    private final Page page;
    private final Line line;
    private boolean cancelled;

    /**
     * Create a new instance of {@link HologramClickEvent}.
     *
     * @param hologram The hologram that is being affected.
     * @param player The player that is being affected.
     * @param clickType The type of click that was performed.
     * @param page The page that was clicked.
     * @param line The line that was clicked. (Can be null if page was clicked)
     */
    public HologramClickEvent(@NotNull Hologram hologram,
                              @NotNull Player player,
                              @NotNull ClickType clickType,
                              @NotNull Page page,
                              @Nullable Line line) {
        super(hologram, player);
        this.clickType = clickType;
        this.page = page;
        this.line = line;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Get the type of click that was performed.
     *
     * @return The type of click that was performed.
     */
    @NotNull
    public ClickType getClickType() {
        return clickType;
    }

    /**
     * Get the page that was clicked.
     *
     * @return The page that was clicked.
     */
    @NotNull
    public Page getPage() {
        return page;
    }

    /**
     * Get the line that was clicked. (Can be null if page was clicked)
     *
     * @return The line that was clicked or null if only page was clicked.
     */
    @Nullable
    public Line getLine() {
        return line;
    }

}

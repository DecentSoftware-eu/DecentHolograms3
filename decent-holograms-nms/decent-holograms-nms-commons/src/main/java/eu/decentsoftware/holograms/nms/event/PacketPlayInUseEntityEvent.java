package eu.decentsoftware.holograms.nms.event;

import eu.decentsoftware.holograms.api.event.DecentHologramsEvent;
import eu.decentsoftware.holograms.api.hologram.ClickType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * This event is called when a player interacts with an entity. It is just used
 * internally for click detection.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@Setter
public class PacketPlayInUseEntityEvent extends DecentHologramsEvent implements Cancellable {

    private final @NonNull Player player;
    private final int entityId;
    private final @NonNull ClickType clickType;
    private boolean cancelled = false;

    public PacketPlayInUseEntityEvent(@NonNull Player player, int entityId, @NonNull ClickType clickType) {
        super(true);
        this.player = player;
        this.entityId = entityId;
        this.clickType = clickType;
    }

}
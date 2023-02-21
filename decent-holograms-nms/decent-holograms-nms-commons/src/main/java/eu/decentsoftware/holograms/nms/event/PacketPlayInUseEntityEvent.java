package eu.decentsoftware.holograms.nms.event;

import eu.decentsoftware.holograms.api.event.DecentHologramsEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;

/**
 * This event is called when a player interacts with an entity. It is just used
 * internally for click detection.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@Setter
@RequiredArgsConstructor
public class PacketPlayInUseEntityEvent extends DecentHologramsEvent implements Cancellable {

    private final @NonNull Player player;
    private final int entityId;
    private final @NonNull ClickType clickType;
    private boolean cancelled;

}
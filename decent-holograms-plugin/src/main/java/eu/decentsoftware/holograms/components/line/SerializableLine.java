package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.components.page.DefaultPage;
import lombok.Data;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to (de)serialize lines from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Data
public class SerializableLine {

    private final @NotNull DefaultLineSettings settings;
    private final @NotNull Location location;
    private final @NotNull ConditionHolder viewConditions;
    private final @NotNull ConditionHolder clickConditions;
    private final @NotNull DefaultActionHolder clickActions;
    private final String content;

    /**
     * Create a new {@link SerializableLine} from the given {@link DefaultLine}.
     *
     * @param line The line.
     * @return The new {@link SerializableLine}.
     */
    @NotNull
    public static SerializableLine fromLine(@NotNull DefaultLine line) {
        return new SerializableLine(
                (DefaultLineSettings) line.getSettings(),
                line.getPositionManager().getLocation(),
                line.getViewConditionHolder(),
                line.getClickConditionHolder(),
                (DefaultActionHolder) line.getClickActionHolder(),
                line.getContent()
        );
    }

    /**
     * Create a {@link DefaultLine} from this {@link SerializableLine}.
     *
     * @param page The parent {@link DefaultPage} of this line.
     * @return The new {@link DefaultLine}.
     */
    @NotNull
    public DefaultLine toLine(@NotNull DefaultPage page) {
        return new DefaultLine(page, settings, location, viewConditions, clickConditions, clickActions, content);
    }

}

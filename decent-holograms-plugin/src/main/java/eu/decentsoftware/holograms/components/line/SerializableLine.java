package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.components.page.DefaultPage;
import eu.decentsoftware.holograms.conditions.DefaultConditionHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to (de)serialize lines from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public class SerializableLine {

    private DefaultLineSettings settings;
    private DefaultConditionHolder viewConditions;
    private DefaultConditionHolder clickConditions;
    private DefaultActionHolder clickActions;
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
                (DefaultConditionHolder) line.getViewConditionHolder(),
                (DefaultConditionHolder) line.getClickConditionHolder(),
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
        if (settings == null) {
            settings = new DefaultLineSettings();
        }
        if (viewConditions == null) {
            viewConditions = new DefaultConditionHolder();
        }
        if (clickConditions == null) {
            clickConditions = new DefaultConditionHolder();
        }
        if (clickActions == null) {
            clickActions = new DefaultActionHolder();
        }
        return new DefaultLine(page, settings, page.getNextLineLocation(), viewConditions, clickConditions, clickActions, content);
    }

}

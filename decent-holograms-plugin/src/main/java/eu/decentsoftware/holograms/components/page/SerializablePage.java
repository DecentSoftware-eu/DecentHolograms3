package eu.decentsoftware.holograms.components.page;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.api.utils.collection.DecentList;
import eu.decentsoftware.holograms.components.hologram.DefaultHologram;
import eu.decentsoftware.holograms.components.line.DefaultLine;
import eu.decentsoftware.holograms.components.line.SerializableLine;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class is used to (de)serialize pages from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Data
public class SerializablePage {

    private final @NotNull List<SerializableLine> lines;
    private final @NotNull ConditionHolder clickConditions;
    private final @NotNull DefaultActionHolder clickActions;

    /**
     * Create a new {@link SerializablePage} from the given {@link DefaultPage}.
     *
     * @param page The page.
     * @return The new {@link SerializablePage}.
     */
    @NotNull
    public static SerializablePage fromPage(@NotNull DefaultPage page) {
        List<SerializableLine> lines = new DecentList<>();
        for (Line line : page.getLineHolder().getLines()) {
            DefaultLine defaultLine = (DefaultLine) line;
            SerializableLine serializableLine = SerializableLine.fromLine(defaultLine);
            lines.add(serializableLine);
        }
        return new SerializablePage(
                lines,
                page.getClickConditionHolder(),
                (DefaultActionHolder) page.getClickActionHolder()
        );
    }

    /**
     * Create a {@link DefaultPage} from this {@link SerializablePage}.
     *
     * @param hologram The parent {@link DefaultHologram} of this page.
     * @return The new {@link DefaultPage}.
     */
    @NotNull
    public DefaultPage toPage(@NotNull DefaultHologram hologram) {
        DefaultPage page = new DefaultPage(hologram, clickConditions, clickActions);
        DecentList<Line> lines = new DecentList<>();
        for (SerializableLine line : this.lines) {
            DefaultLine defaultLine = line.toLine(page);
            lines.add(defaultLine);
        }
        page.getLineHolder().setLines(lines);
        return page;
    }

}

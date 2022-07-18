package eu.decentsoftware.holograms.actions;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionParser;
import eu.decentsoftware.holograms.api.actions.ActionParserManager;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import org.jetbrains.annotations.NotNull;

public class DefaultActionParserManager implements ActionParserManager {

    private final DList<ActionParser> parsers;

    /**
     * Create a new instance of {@link DefaultActionParserManager}. This constructor
     * also registers the {@link DefaultActionParser} as the default parser.
     */
    public DefaultActionParserManager() {
        this.parsers = new DList<>();

        // Register default parser
        this.parsers.add(new DefaultActionParser());
    }

    @Override
    public void register(@NotNull ActionParser parser) {
        this.parsers.add(parser);
    }

    @Override
    public Action parse(@NotNull Section config) {
        for (int i = parsers.size() - 1; i >= 0; i--) {
            ActionParser parser = parsers.get(i);
            Action action = parser.parse(config);
            if (action != null) {
                return action;
            }
        }
        return null;
    }

}

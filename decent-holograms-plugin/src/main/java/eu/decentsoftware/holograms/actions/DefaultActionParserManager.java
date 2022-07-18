package eu.decentsoftware.holograms.actions;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.decentsoftware.holograms.api.actions_new.Action;
import eu.decentsoftware.holograms.api.actions_new.ActionParser;
import eu.decentsoftware.holograms.api.actions_new.ActionParserManager;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import org.jetbrains.annotations.NotNull;

public class DefaultActionParserManager implements ActionParserManager {

    private final DList<ActionParser> parsers = new DList<>();

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

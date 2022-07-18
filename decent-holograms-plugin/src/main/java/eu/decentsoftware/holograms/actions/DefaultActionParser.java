package eu.decentsoftware.holograms.actions;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionParser;
import org.jetbrains.annotations.NotNull;

public class DefaultActionParser implements ActionParser {

    @Override
    public Action parse(@NotNull Section config) {
        String typeName = config.getString("type");
        if (typeName == null) {
            return null;
        }
        DefaultActionType type = DefaultActionType.fromString(typeName);
        if (type == null) {
            return null;
        }
        long delay = config.getLong("delay", 0L);
        double chance = config.getDouble("chance", -1.0D);

        switch (type) {
            case MESSAGE:

        }

        return null;
    }

}

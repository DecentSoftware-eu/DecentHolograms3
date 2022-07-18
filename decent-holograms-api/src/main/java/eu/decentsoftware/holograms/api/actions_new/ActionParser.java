package eu.decentsoftware.holograms.api.actions_new;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.jetbrains.annotations.NotNull;

/**
 * This class is responsible for parsing hologram actions from config files. You
 * can create your own action parser by implementing the {@link ActionParser}
 * interface and registering your parser in the {@link ActionParserManager}.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface ActionParser {

    /**
     * Parse the given config section into an action.
     *
     * @param config The config section.
     * @return The action or null if the config section is not a valid action.
     */
    Action parse(@NotNull Section config);

}

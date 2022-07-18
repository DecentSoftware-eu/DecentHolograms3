package eu.decentsoftware.holograms.api.actions_new;

import org.jetbrains.annotations.NotNull;

/**
 * This manager is responsible for holding action parsers and parsing actions
 * from config files. Actions parsers are used in the reverse order of registration.
 * Meaning, the last registered action parser will be used first.
 *
 * @author d0by
 * @see ActionParser
 * @since 3.0.0
 */
public interface ActionParserManager extends ActionParser {

    /**
     * Register the given action parser.
     *
     * @param parser The action parser.
     */
    void register(@NotNull ActionParser parser);

}

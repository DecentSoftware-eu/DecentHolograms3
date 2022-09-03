package eu.decentsoftware.holograms.components.line.content;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.api.component.line.content.ContentParserManager;
import eu.decentsoftware.holograms.api.utils.collection.DecentList;
import eu.decentsoftware.holograms.components.line.content.parsers.*;
import org.jetbrains.annotations.NotNull;

public class DefaultContentParserManager implements ContentParserManager {

    private final @NotNull DecentList<ContentParser> parsers;

    /**
     * Creates a new instance of {@link DefaultContentParserManager}. This constructor
     * will also register the default parsers.
     */
    public DefaultContentParserManager() {
        this.parsers = new DecentList<>();

        // - Register default parsers -
        //
        // Content parsers are used in a reverse order, so the last registered
        // parser will be used first.
        //
        // Text content parser is always registered first as it is the most
        // generic one being able to parse any content. It's considered to be
        // the fallback parser.

        register(new TextContentParser());
        register(new IconContentParser());
        register(new HeadContentParser());
        register(new SmallHeadContentParser());
        register(new EntityContentParser());
        register(new ImageContentParser());

    }

    /**
     * Register a new content parser. This content parser will be used first.
     *
     * @param parser The content parser to register.
     * @see ContentParser
     */
    @Override
    public void register(@NotNull ContentParser parser) {
        this.parsers.add(parser);
    }

    @Override
    public boolean parse(@NotNull Line line) {
        // Parse content
        for (int i = parsers.size() - 1; i >= 0; i--) {
            ContentParser parser = parsers.get(i);
            if (parser.parse(line)) {
                return true;
            }
        }
        // This should never happen as long as the text
        // content parser is registered.
        return false;
    }

}

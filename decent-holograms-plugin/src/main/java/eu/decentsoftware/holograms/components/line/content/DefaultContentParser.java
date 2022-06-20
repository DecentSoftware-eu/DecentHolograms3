package eu.decentsoftware.holograms.components.line.content;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.content.ContentParser;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import eu.decentsoftware.holograms.components.line.content.parsers.*;
import org.jetbrains.annotations.NotNull;

public class DefaultContentParser implements ContentParser {

    private final DList<ContentParser> parsers;

    /**
     * Creates a new instance of {@link DefaultContentParser}. This constructor
     * will also register the default parsers.
     */
    public DefaultContentParser() {
        this.parsers = new DList<>();

        // - Register default parsers -
        //
        // Content parsers are used in a reverse order, so the last registered
        // parser will be used first.
        //
        // Text content parser is always registered first as it is the most
        // generic one being able to parse any content. It's considered to be
        // the fallback parser.

        this.parsers.add(new TextContentParser());
        this.parsers.add(new IconContentParser());
        this.parsers.add(new HeadContentParser());
        this.parsers.add(new SmallHeadContentParser());
        this.parsers.add(new EntityContentParser());
    }

    @Override
    public boolean parse(@NotNull Line line) {
        // Parse content
        for (int i = parsers.size() - 1; i > 0; i--) {
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

/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.content.parser;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.HologramLine;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the main content parser, which is used to register
 * all the content parsers and parse the content of a line.
 * <p>
 * The registered content parsers are used in a reverse order, so the last registered
 * parser will be used first.
 * <p>
 * To register a content parser, use {@link #register(ContentParser)}. Your content parser
 * is going to the end of the list and will be used first.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ContentParserManager {

    private final List<ContentParser> parsers = new ArrayList<>();

    /**
     * Creates a new instance of {@link ContentParserManager}. This constructor
     * will also register the default parsers.
     */
    public ContentParserManager(@NonNull DecentHolograms plugin) {
        // - Register default parsers -
        //
        // Content parsers are used in a reverse order, so the last registered
        // parser will be used first.
        //
        // Text content parser is always registered first as it is the most
        // generic one being able to parse any content. It's considered to be
        // the fallback parser.

        register(new TextContentParser(plugin));
        register(new IconContentParser(plugin));
        register(new HeadContentParser(plugin));
        register(new SmallHeadContentParser(plugin));
        register(new EntityContentParser(plugin));

    }

    /**
     * Register a new content parser. This content parser will be used first.
     *
     * @param parser The content parser to register.
     * @see ContentParser
     */
    public void register(@NonNull ContentParser parser) {
        this.parsers.add(parser);
    }

    /**
     * Parse the content of the given line and update the line's renderer.
     *
     * @param line The line to parse the content for.
     * @return True if the content was parsed successfully, false otherwise.
     * @see HologramLine
     */
    public boolean parse(@NonNull CoreHologramLine line) {
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

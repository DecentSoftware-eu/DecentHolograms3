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

package eu.decentsoftware.holograms.api.component.line.content;

import org.jetbrains.annotations.NotNull;

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
public interface ContentParserManager extends ContentParser {

    /**
     * Register a content parser.
     *
     * @param parser The content parser to register.
     */
    void register(@NotNull ContentParser parser);

}

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

package eu.decentsoftware.holograms.components.line.content.parsers;

import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.api.component.line.HologramLineRenderer;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to parse string content into a {@link HologramLineRenderer}.
 *
 * @author d0by
 * @see HologramLineRenderer
 * @since 3.0.0
 */
public interface ContentParser {

    /**
     * Parse the content of the given line and update the line's renderer.
     *
     * @param line The line to parse the content for.
     * @return True if the content was parsed successfully, false otherwise.
     * @see HologramLineRenderer
     */
    boolean parse(@NotNull HologramLine line);

}

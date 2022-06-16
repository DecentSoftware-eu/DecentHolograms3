package eu.decentsoftware.holograms.api.component.line.content;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to parse string content into a {@link LineRenderer}.
 *
 * @author d0by
 * @see LineRenderer
 */
public interface ContentParser {

    /**
     * Parse the content of the given line and update the line's renderer.
     *
     * @param line The line to parse the content for.
     * @return True if the content was parsed successfully, false otherwise.
     * @see LineRenderer
     */
    boolean parse(@NotNull Line line);

}

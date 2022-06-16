package eu.decent.holograms.api.component.line.content;

import eu.decent.holograms.api.component.line.LineRenderer;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to parse string content into a {@link LineRenderer}.
 *
 * @author d0by
 * @see LineRenderer
 */
public interface ContentParser {

    /**
     * Parse the given string and return a {@link LineRenderer} object.
     *
     * @param content The content to parse.
     * @see LineRenderer
     */
    @NotNull
    LineRenderer parse(@NotNull String content);

}

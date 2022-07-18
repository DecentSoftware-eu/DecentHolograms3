package eu.decentsoftware.holograms.api.replacements;

import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents the replacement registry. It is responsible for managing
 * all replacements of internal placeholders.
 * <p>
 * There are three types of replacements:
 * <ul>
 *     <li>Default Replacements: Replaced by the plugin.</li>
 *     <li>Normal Replacements: Replaced with a configurable value.</li>
 * </ul>
 *
 * @author d0by
 * @since 1.0.0
 */
public interface ReplacementRegistry {

    /**
     * Reload all replacements from the config.
     */
    void reload();

    /**
     * Shutdown this manager, removing all registered replacements.
     */
    void shutdown();

    /**
     * Replace all registered placeholders, that the given String contains.
     *
     * @param string        The string.
     * @param profile       The profile to replace the placeholders for.
     * @return The resulting String.
     */
    String replace(@NotNull String string, @Nullable Profile profile);


}

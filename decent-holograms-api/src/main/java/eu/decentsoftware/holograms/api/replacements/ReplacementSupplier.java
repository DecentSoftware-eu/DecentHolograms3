package eu.decentsoftware.holograms.api.replacements;

import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.Nullable;

/**
 * This functional interface represents the supplier, that finds the replacements of placeholders.
 *
 * @author d0by
 * @since 3.0.0
 */
@FunctionalInterface
public interface ReplacementSupplier {

    /**
     * Get the replacement from this supplier.
     *
     * @param profile  The profile to get the replacement for.
     * @param argument The argument to get it for.
     * @return The replacement.
     */
    String getReplacement(@Nullable Profile profile, @Nullable String argument);

}

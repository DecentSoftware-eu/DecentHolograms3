package eu.decentsoftware.holograms.api.replacements;

import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a custom placeholder.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface Replacement {

    /**
     * Get the replacement supplier for this placeholder.
     *
     * @return the replacement supplier.
     */
    @NotNull
    ReplacementSupplier getReplacementSupplier();

    /**
     * Get the default replacement for this placeholder.
     *
     * @return The default replacement.
     */
    String getDefaultReplacement();

    /**
     * Get the replacement for this placeholder.
     *
     * @param profile   The profile to get the replacement for.
     * @param argument The argument to get the replacement for.
     * @return The replacement.
     */
    String getReplacement(@Nullable Profile profile, @Nullable String argument);


}

package eu.decentsoftware.holograms.replacements;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.replacements.Replacement;
import eu.decentsoftware.holograms.api.replacements.ReplacementSupplier;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class DefaultReplacement implements Replacement {

    private final @NotNull ReplacementSupplier replacementSupplier;
    private final @Nullable String defaultReplacement;

    /**
     * Create new {@link DefaultReplacement}.
     *
     * @param replacementSupplier The supplier, that finds a replacement for this placeholder.
     */
    public DefaultReplacement(@NotNull ReplacementSupplier replacementSupplier) {
        this(replacementSupplier, null);
    }

    /**
     * Create new {@link DefaultReplacement}.
     *
     * @param replacementSupplier The supplier, that finds a replacement for this placeholder.
     * @param defaultReplacement The default replacement that will be used if the replacement supplier returns null.
     */
    public DefaultReplacement(@NotNull ReplacementSupplier replacementSupplier, @Nullable String defaultReplacement) {
        this.replacementSupplier = replacementSupplier;
        this.defaultReplacement = defaultReplacement;
    }

    @Nullable
    @Override
    public String getReplacement(@Nullable Profile profile, @Nullable String argument) {
        String replacement = replacementSupplier.getReplacement(profile, argument);
        return replacement == null ? defaultReplacement : replacement;
    }

}

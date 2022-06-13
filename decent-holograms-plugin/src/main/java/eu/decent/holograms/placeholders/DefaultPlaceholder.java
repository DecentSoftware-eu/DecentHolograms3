package eu.decent.holograms.placeholders;

import eu.decent.holograms.api.placeholders.Placeholder;
import eu.decent.holograms.api.placeholders.PlaceholderReplacementSupplier;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public class DefaultPlaceholder implements Placeholder {

    private final PlaceholderReplacementSupplier replacementSupplier;
    private final String defaultReplacement;

    /**
     * Create new {@link DefaultPlaceholder}.
     *
     * @param replacementSupplier The supplier, that finds a replacement for this placeholder.
     */
    public DefaultPlaceholder(@NotNull PlaceholderReplacementSupplier replacementSupplier) {
        this(replacementSupplier, null);
    }

    /**
     * Create new {@link DefaultPlaceholder}.
     *
     * @param replacementSupplier The supplier, that finds a replacement for this placeholder.
     * @param defaultReplacement The default replacement that will be used if the replacement supplier returns null.
     */
    public DefaultPlaceholder(@NotNull PlaceholderReplacementSupplier replacementSupplier, String defaultReplacement) {
        this.replacementSupplier = replacementSupplier;
        this.defaultReplacement = defaultReplacement;
    }

    @Override
    public String getReplacement(Player player, String argument) {
        String replacement = replacementSupplier.getReplacement(player, argument);
        return replacement == null ? defaultReplacement : replacement;
    }

}

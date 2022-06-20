package eu.decentsoftware.holograms.components.page.placeholders;

import eu.decentsoftware.holograms.api.replacements.Replacement;
import eu.decentsoftware.holograms.api.replacements.ReplacementSupplier;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public class DefaultReplacement implements Replacement {

    private final ReplacementSupplier replacementSupplier;
    private final String defaultReplacement;

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
    public DefaultReplacement(@NotNull ReplacementSupplier replacementSupplier, String defaultReplacement) {
        this.replacementSupplier = replacementSupplier;
        this.defaultReplacement = defaultReplacement;
    }

    @Override
    public String getReplacement(Player player, String argument) {
        String replacement = replacementSupplier.getReplacement(player, argument);
        return replacement == null ? defaultReplacement : replacement;
    }

}

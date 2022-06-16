package eu.decentsoftware.holograms.conditions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This enum represents all possible Condition types.
 */
@SuppressWarnings("SpellCheckingInspection")
public enum ConditionType {
    MONEY("money", "has money"),
    PERMISSION("permission", "has permission", "perm", "has perm"),
    REGEX("regex", "regex matches", "matches regex", "compare regex"),
    DISTANCE("distance", "near", "is near"),
    EXP("exp", "has exp", "xp", "has xp"),
    ITEM("item", "has item"),
    JAVASCRIPT("javascript", "js"),

    // -- Numbers
    EQUAL("==", "equals", "equal", "equal to", "is equal", "is equal to"),
    LESS("<", "less", "is less", "less than", "isless than"),
    LESS_EQUAL("<=", "less or equal", "is less or equal", "less than or equal", "is less than or equal"),
    GREATER(">", "greater", "is greater", "greater than", "is greater than"),
    GREATER_EQUAL(">=", "greater or equal", "is greater or equal", "greater than or equal", "is greater than or equal"),

    // -- Strings
    STRING_EQUAL("string equals"),
    STRING_EQUAL_IGNORECASE("string equals ignore case"),
    STRING_CONTAINS("string contains"),
    ;

    @Getter
    private final Set<String> aliases;

    ConditionType(String... aliases) {
        this.aliases = new HashSet<>();
        if (aliases != null) {
            this.aliases.addAll(Arrays.asList(aliases));
        }
    }

    /**
     * Find a {@link ConditionType} by the given string.
     *
     * @param string The string.
     * @return The ConditionType or null if the string doesn't match any.
     */
    @Nullable
    public static ConditionType fromString(@NotNull String string) {
        for (ConditionType conditionType : values()) {
            for (String alias : conditionType.getAliases()) {
                if (alias.trim().equalsIgnoreCase(string.trim())) {
                    return conditionType;
                }
            }
        }
        return null;
    }

}

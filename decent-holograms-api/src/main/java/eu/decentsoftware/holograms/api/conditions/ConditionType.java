package eu.decentsoftware.holograms.api.conditions;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConditionType {

    protected final @NotNull String[] aliases;

    /**
     * Create a new instance of {@link ConditionType}.
     *
     * @param aliases The aliases of this type.
     */
    public ConditionType(@NotNull String... aliases) {
        this.aliases = aliases;
    }

    /**
     * Create a new instance of {@link Condition} of this type from
     * the given data.
     *
     * @param data The data of the condition.
     * @return The condition or null if the creation failed.
     */
    @Nullable
    public abstract Condition createCondition(String... data);

    /**
     * Create a new instance of {@link Condition} of this type from
     * the given data in JSON.
     *
     * @param json The data of the condition in JSON.
     * @return The condition or null if the creation failed.
     */
    @Nullable
    public abstract Condition createCondition(@NotNull JsonElement json);

    /**
     * Check if the given name is a valid alias of this type.
     *
     * @param name The name.
     * @return True if the name is a valid alias of this type.
     */
    public boolean isAlias(@NotNull String name) {
        for (String alias : getAliases()) {
            if (alias.trim().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the array of aliases of this type.
     *
     * @return The array of aliases.
     */
    @NotNull
    public String[] getAliases() {
        return aliases;
    }

}

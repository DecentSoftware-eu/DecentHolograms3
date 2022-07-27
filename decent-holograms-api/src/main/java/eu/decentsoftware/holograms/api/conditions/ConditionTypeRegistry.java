package eu.decentsoftware.holograms.api.conditions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * This class is responsible for holding and managing all condition types.
 *
 * @author d0by
 * @see ConditionType
 * @since 3.0.0
 */
public interface ConditionTypeRegistry {

    /**
     * Register a given condition type.
     *
     * @param type The condition type.
     * @see ConditionType
     */
    void register(@NotNull ConditionType type);

    /**
     * Remove a given condition type.
     *
     * @param type The condition type.
     * @see ConditionType
     */
    void remove(@NotNull ConditionType type);

    /**
     * Remove a given condition type by its name.
     *
     * @param name The name.
     * @see ConditionType
     */
    void remove(@NotNull String name);

    /**
     * Get the condition type with the given name.
     *
     * @param name The name.
     * @return The condition type.
     * @see ConditionType
     */
    @Nullable
    ConditionType get(@NotNull String name);

    /**
     * Get all registered condition types.
     *
     * @return The condition types.
     * @see ConditionType
     */
    @NotNull
    Set<ConditionType> getTypes();

}

package eu.decentsoftware.holograms.api.actions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * This class is responsible for holding and managing all action types.
 *
 * @author d0by
 * @see ActionType
 * @since 3.0.0
 */
public interface ActionTypeRegistry {

    /**
     * Register a given action type.
     *
     * @param type The action type.
     * @see ActionType
     */
    void register(@NotNull ActionType type);

    /**
     * Remove a given action type.
     *
     * @param type The action type.
     * @see ActionType
     */
    void remove(@NotNull ActionType type);

    /**
     * Remove a given action type by its name.
     *
     * @param name The name.
     * @see ActionType
     */
    void remove(@NotNull String name);

    /**
     * Get the action type with the given name.
     *
     * @param name The name.
     * @return The action type.
     * @see ActionType
     */
    @Nullable
    ActionType get(@NotNull String name);

    /**
     * Get all registered action types.
     *
     * @return The action types.
     * @see ActionType
     */
    @NotNull
    Set<ActionType> getTypes();

}

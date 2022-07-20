package eu.decentsoftware.holograms.api.actions;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a type of action. In order to create a new type of action,
 * make an instance of this class and register it with {@link ActionTypeRegistry}.
 * <p>
 * Action types are capable of creating instances of {@link Action} from JSON,
 * which is used when loading actions from configuration.
 * <p>
 * Action types have aliases, which are used to identify them in configuration.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class ActionType {

    protected final @NotNull String[] aliases;

    /**
     * Create a new instance of {@link ActionType}.
     *
     * @param aliases The aliases of this type.
     */
    public ActionType(@NotNull String... aliases) {
        this.aliases = aliases;
    }

    /**
     * Create a new instance of {@link Action} of this type.
     *
     * @param json The JSON object.
     * @return The action.
     */
    public abstract Action createAction(@NotNull JsonObject json);

    /**
     * Check if the given name is a valid alias of this type.
     *
     * @param name The name.
     * @return True if the name is a valid alias of this type.
     */
    public boolean isValid(@NotNull String name) {
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

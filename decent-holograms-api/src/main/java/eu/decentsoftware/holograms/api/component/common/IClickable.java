package eu.decentsoftware.holograms.api.component.common;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an object, that can be clicked. It can have actions and
 * conditions.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface IClickable {

    /**
     * Handle a click on this object.
     *
     * @param player The player that clicked this object.
     * @param clickType The type of click that was performed.
     * @return True if the click was handled and actions were executed, false otherwise.
     */
    default boolean onClick(@NotNull Player player, @NotNull ClickType clickType) {
        Profile profile = DecentHologramsAPI.getInstance().getProfileRegistry().get(player.getName());
        if (getClickConditionHolder().check(profile)) {
            getClickActionHolder().execute(profile);
            return true;
        }
        return false;
    }

    /**
     * Get the click conditions of this object.
     *
     * @return The click conditions of this object.
     * @see ConditionHolder
     */
    @NotNull
    ConditionHolder getClickConditionHolder();

    /**
     * Get the click actions of this object.
     *
     * @return The click actions of this object.
     * @see ActionHolder
     */
    @NotNull
    ActionHolder getClickActionHolder();

}

package eu.decentsoftware.holograms.api.component.line;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a line renderer. A line renderer is a class that
 * stores the data of a line and renders it for a player. There are different
 * types of line renderers for different types of lines.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface LineRenderer {

    /**
     * Get the parent {@link Line} of this line renderer.
     *
     * @return The parent {@link Line} of this line renderer.
     */
    @NotNull
    Line getParent();

    /**
     * Get the type of this line renderer.
     *
     * @return The type of this line renderer.
     * @see LineType
     */
    @NotNull
    LineType getType();

    /**
     * Display the parent {@link Line} for a player.
     *
     * @param player The player to display the line for.
     */
    void display(@NotNull Player player);

    /**
     * Update the contents of the parent {@link Line} for a player.
     *
     * @param player The player to update the line for.
     */
    void update(@NotNull Player player);

    /**
     * Hide the parent {@link Line} for a player.
     *
     * @param player The player to hide the line for.
     */
    void hide(@NotNull Player player);

    /**
     * Teleport the parent {@link Line} to a location for a player.
     *
     * @param player The player to teleport the line to.
     * @param location The location to teleport the line to.
     */
    void teleport(@NotNull Player player, @NotNull Location location);

}

package eu.decentsoftware.holograms.api.component.line;

/**
 * This enum contains all the possible line types. The line type determines
 * how the line will be displayed for the player.
 *
 * @author d0by
 */
public enum LineType {
    /**
     * Just a normal text line.
     */
    TEXT,
    /**
     * A line displaying an ItemStack.
     */
    ICON,
    /**
     * A line displaying an ItemStack that doesn't move.
     */
    STATIC_ICON,
    /**
     * A line displaying an ItemStack as a Head of an ArmorStand.
     */
    HEAD,
    /**
     * A line displaying an ItemStack as a Head of a small ArmorStand.
     */
    SMALL_HEAD,
    /**
     * A line displaying an Entity.
     */
    ENTITY,
    /**
     * A line, made of multiple lines, displaying an image.
     */
    IMAGE
}

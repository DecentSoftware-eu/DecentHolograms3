package eu.decentsoftware.holograms.api.utils.color;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Wrapper for {@link Color} class.
 *
 * @author d0by
 * @since 3.0.0
 */
public class DecentColor {

    private final Color color;

    /**
     * Creates new instance of {@link DecentColor} class.
     *
     * @param hex Hex string representation of color.
     */
    public DecentColor(@NotNull String hex) {
        this(Integer.parseInt(hex, 16));
    }

    /**
     * Creates new instance of {@link DecentColor} class.
     *
     * @param red Red component of color.
     * @param green Green component of color.
     * @param blue Blue component of color.
     */
    public DecentColor(int red, int green, int blue) {
        this.color = new Color(red, green, blue);
    }

    /**
     * Creates new instance of {@link DecentColor} class.
     *
     * @param rgb Integer representation of color.
     */
    public DecentColor(int rgb) {
        this.color = new Color(rgb);
    }

    /**
     * Returns hex representation of color.
     *
     * @return Hex representation of color.
     */
    public String toHex() {
        return DecentColorAPI.rgbToHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Returns {@link ChatColor} representation of color.
     *
     * @return {@link ChatColor} representation of color.
     */
    public ChatColor toChatColor() {
        return DecentColorAPI.getClosestColor(color);
    }

}

/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.utils.color;

import com.google.common.collect.ImmutableMap;
import eu.decentsoftware.holograms.nms.utils.Version;
import eu.decentsoftware.holograms.utils.color.patterns.GradientPattern;
import eu.decentsoftware.holograms.utils.color.patterns.Pattern;
import eu.decentsoftware.holograms.utils.color.patterns.RainbowPattern;
import eu.decentsoftware.holograms.utils.color.patterns.SolidPattern;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.Contract;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for parsing colors in a string.
 */
@UtilityClass
public final class DecentColorAPI {

    private static final List<String> SPECIAL_CODES = Arrays.asList("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m");

    private static final Map<Color, ChatColor> COLORS = ImmutableMap.<Color, ChatColor>builder()
            .put(new Color(0), ChatColor.getByChar('0'))
            .put(new Color(170), ChatColor.getByChar('1'))
            .put(new Color(43520), ChatColor.getByChar('2'))
            .put(new Color(43690), ChatColor.getByChar('3'))
            .put(new Color(11141120), ChatColor.getByChar('4'))
            .put(new Color(11141290), ChatColor.getByChar('5'))
            .put(new Color(16755200), ChatColor.getByChar('6'))
            .put(new Color(11184810), ChatColor.getByChar('7'))
            .put(new Color(5592405), ChatColor.getByChar('8'))
            .put(new Color(5592575), ChatColor.getByChar('9'))
            .put(new Color(5635925), ChatColor.getByChar('a'))
            .put(new Color(5636095), ChatColor.getByChar('b'))
            .put(new Color(16733525), ChatColor.getByChar('c'))
            .put(new Color(16733695), ChatColor.getByChar('d'))
            .put(new Color(16777045), ChatColor.getByChar('e'))
            .put(new Color(16777215), ChatColor.getByChar('f'))
            .build();

    private static final List<Pattern> PATTERNS = Arrays.asList(
            new SolidPattern(),
            new GradientPattern(),
            new RainbowPattern()
    );

    /**
     * Processes the given string parsing all colors.
     *
     * @param string The string to parse.
     * @return The parsed string.
     */
    @NonNull
    public static String process(@NonNull String string) {
        // TODO: translate to minimessage format
//        for (Pattern pattern : PATTERNS) {
//            string = pattern.process(string);
//        }
        string = ChatColor.translateAlternateColorCodes('&', string);
        return string;
    }

    /**
     * Processes the given strings parsing all colors.
     *
     * @param strings The strings to parse.
     * @return The parsed strings.
     */
    @NonNull
    public static List<String> process(@NonNull List<String> strings) {
        return strings.stream().map(DecentColorAPI::process).collect(Collectors.toList());
    }

    /**
     * Removes all colors from the given string.
     *
     * @param string The string to remove colors from.
     * @return The string without colors.
     */
    @Contract(pure = true)
    @NonNull
    public static String stripColors(@NonNull String string) {
        return string.replaceAll("<#[\\dA-F]{6}>|[&§][a-f\\dlnokm]|</?[A-Z]{5,8}(:[\\dA-F]{6})?\\d*>", "");
    }

    /**
     * Colors the given string with the given color.
     *
     * @param string The string to color.
     * @param color  The color to use.
     * @return The colored string.
     */
    @NonNull
    public static String color(@NonNull String string, @NonNull Color color) {
        return getColor(color) + string;
    }

    /**
     * Colors the given string with a gradient between the given colors.
     *
     * @param string The string to color.
     * @param start  The start color.
     * @param end    The end color.
     * @return The colored string.
     */
    @NonNull
    public static String color(@NonNull String string, @NonNull Color start, @NonNull Color end) {
        return apply(string, createGradient(start, end, withoutSpecialChar(string).length()));
    }

    /**
     * Colors the given string with a rainbow of the given saturation.
     *
     * @param string     The string to color.
     * @param saturation The saturation of the rainbow.
     * @return The colored string.
     */
    @NonNull
    public static String rainbow(@NonNull String string, float saturation) {
        return apply(string, createRainbow(withoutSpecialChar(string).length(), saturation));
    }

    /**
     * Gets a color from hex code.
     *
     * @param string The hex code of the color
     * @since 1.0.0
     */
    @NonNull
    public static ChatColor getColor(@NonNull String string) {
        Color color = new Color(Integer.parseInt(string, 16));
        return getColor(color);
    }

    /**
     * Gets a ChatColor from a color.
     *
     * @param color The color to get the color from.
     * @return The ChatColor.
     */
    @NonNull
    public static ChatColor getColor(@NonNull Color color) {
        return Version.supportsRGB() ? ChatColor.of(color) : getClosestColor(color);
    }

    /**
     * Gets a ChatColor from a color.
     *
     * @param color The color to get the color from.
     * @param def   The default color to return if hex is not supported.
     * @return The ChatColor.
     */
    @NonNull
    public static ChatColor getColor(@NonNull Color color, @NonNull ChatColor def) {
        return Version.supportsRGB() ? ChatColor.of(color) : def;
    }

    /**
     * Gets the closest color to the given color.
     *
     * @param color The color to get the closest color to.
     * @return The closest color.
     */
    @NonNull
    public static ChatColor getClosestColor(@NonNull Color color) {
        Color nearestColor = null;
        int nearestDistance = Integer.MAX_VALUE;
        for (Color constantColor : COLORS.keySet()) {
            int distance = getDistanceSquared(color, constantColor);
            if (nearestDistance > distance) {
                nearestColor = constantColor;
                nearestDistance = distance;
            }
        }
        return COLORS.get(nearestColor);
    }

    /**
     * Gets the distance squared between the two given colors.
     *
     * @param color1 The first color.
     * @param color2 The second color.
     * @return The distance squared.
     */
    public int getDistanceSquared(@NonNull Color color1, @NonNull Color color2) {
        int dr = color1.getRed() - color2.getRed();
        int dg = color1.getGreen() - color2.getGreen();
        int db = color1.getBlue() - color2.getBlue();
        return dr * dr + dg * dg + db * db;
    }

    /**
     * Applies the given colors to the given string.
     *
     * @param source The string to apply the colors to.
     * @param colors The colors to apply.
     * @return The string with the colors applied.
     */
    @NonNull
    private static String apply(@NonNull String source, ChatColor[] colors) {
        StringBuilder specialColors = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        String[] characters = source.split("");
        int outIndex = 0;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i].equals("&") || characters[i].equals("§")) {
                if (i + 1 < characters.length) {
                    if (characters[i + 1].equals("r")) {
                        specialColors.setLength(0);
                    } else {
                        specialColors.append(characters[i]);
                        specialColors.append(characters[i + 1]);
                    }
                    i++;
                } else
                    stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
            } else
                stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
        }
        return stringBuilder.toString();
    }

    /**
     * Removes all the special color codes from the given string.
     *
     * @param source The string to remove the special color codes from.
     * @return The string without the special color codes.
     */
    @NonNull
    private static String withoutSpecialChar(@NonNull String source) {
        String workingString = source;
        for (String color : SPECIAL_CODES) {
            if (workingString.contains(color)) {
                workingString = workingString.replace(color, "");
            }
        }
        return workingString;
    }

    /**
     * Creates a gradient between the given colors.
     *
     * @param start The start color.
     * @param end   The end color.
     * @param step  The number of steps.
     * @return The gradient as an array of colors.
     */
    @NonNull
    private static ChatColor[] createGradient(@NonNull Color start, @NonNull Color end, int step) {
        ChatColor[] colors = new ChatColor[step];
        int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
        int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
        int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
        int[] direction = new int[] {
                start.getRed() < end.getRed() ? 1 : -1,
                start.getGreen() < end.getGreen() ? 1 : -1,
                start.getBlue() < end.getBlue() ? 1 : -1
        };
        for (int i = 0; i < step; i++) {
            colors[i] = getColor(new Color(
                    start.getRed() + (stepR * i * direction[0]),
                    start.getGreen() + (stepG * i * direction[1]),
                    start.getBlue() + (stepB * i * direction[2])
            ));
        }
        return colors;
    }

    /**
     * Creates a rainbow gradient with the given saturation.
     *
     * @param step       The number of steps.
     * @param saturation The saturation of the rainbow.
     * @return The rainbow as an array of colors.
     */
    @NonNull
    private static ChatColor[] createRainbow(int step, float saturation) {
        ChatColor[] colors = new ChatColor[step];
        double colorStep = (1.00 / step);
        for (int i = 0; i < step; i++) {
            colors[i] = getColor(Color.getHSBColor((float) (colorStep * i), saturation, saturation));
        }
        return colors;
    }

    /**
     * Creates a hex string from the given RGB color.
     *
     * @param red   The red value.
     * @param green The green value.
     * @param blue  The blue value.
     * @return The hex string.
     */
    @Contract(pure = true)
    @NonNull
    public static String rgbToHex(int red, int green, int blue) {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

}

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

package eu.decentsoftware.holograms.utils;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.utils.color.DecentColorAPI;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

/**
 * Common utility methods.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings("unused")
@UtilityClass
public final class Common {

	public static final String NON_ESCAPED_SEMICOLON_REGEX = "(?<!\\\\);";

	/*
	 * 	General
	 */

	/**
	 * Generate a random integer between min and max.
	 *
	 * @param min The min value.
	 * @param max The max value.
	 * @return The random number.
	 */
	public static int irand(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	/*
	 * 	Colorize
	 */

	/**
	 * Colorize the given string replacing all color codes, hex colors, gradients and rainbows.
	 *
	 * @param string The string.
	 * @return The colorized string.
	 */
	@NotNull
	public static String colorize(String string) {
		return DecentColorAPI.process(string);
	}

	/**
	 * Colorize the given string list replacing all color codes, hex colors, gradients and rainbows.
	 *
	 * @param list The string list.
	 * @return The colorized string list.
	 */
	@NotNull
	@Contract("_ -> param1")
	public static List<String> colorize(@NotNull List<String> list) {
		return DecentColorAPI.process(list);
	}

	/*
	 * 	Log
	 */

	/**
	 * Log a message into console.
	 *
	 * @param message The message.
	 */
	public static void log(String message) {
		log(Level.INFO, message);
	}

	/**
	 * Log a message into console.
	 * <p>
	 * This method formats given arguments in the message.
	 * </p>
	 *
	 * @param message The message.
	 * @param args    The arguments
	 */
	public static void log(String message, Object... args) {
		log(String.format(message, args));
	}

	/**
	 * Log a message into console.
	 *
	 * @param level   Level of this message.
	 * @param message The message.
	 */
	public static void log(Level level, String message) {
		DecentHolograms.getInstance().getLogger().log(level, message);
	}

	/**
	 * Log a message into console.
	 * <p>
	 * This method formats given arguments in the message.
	 * </p>
	 *
	 * @param level   Level of this message.
	 * @param message The message.
	 * @param args    The arguments.
	 */
	public static void log(Level level, String message, Object... args) {
		log(level, String.format(message, args));
	}

	/*
	 * 	Debug
	 */

	/**
	 * Print an object into console.
	 *
	 * @param o Object to print.
	 */
	public static void debug(Object o) {
		System.out.println(o);
	}

	/*
	 * 	Tell
	 */

	/**
	 * Send a message to given CommandSender.
	 * <p>
	 * This method will colorize the message.
	 * </p>
	 *
	 * @param player  The CommandSender receiving the message.
	 * @param message The message.
	 */
	public static void tell(@NotNull CommandSender player, String message) {
		player.sendMessage(colorize(message));
	}

	/**
	 * Send a message to given CommandSender.
	 * <p>
	 * This method will colorize the message and format the given arguments into it.
	 * </p>
	 *
	 * @param player  The CommandSender receiving the message.
	 * @param message The message.
	 * @param args    The arguments.
	 */
	public static void tell(CommandSender player, String message, Object... args) {
		tell(player, String.format(message, args));
	}

	/*
	 *  Arrays
	 */

	/**
	 * Implode a string array into a string with given separator.
	 *
	 * @param array     The array.
	 * @param separator The separator string.
	 * @return The imploded string.
	 */
	@NotNull
	public static String implode(String[] array, String separator) {
		return implode(array, separator, 0);
	}

	/**
	 * Implode a string array into a string with given separator.
	 *
	 * @param array     The array.
	 * @param separator The separator string.
	 * @param start     The start index.
	 * @return The imploded string.
	 */
	@NotNull
	public static String implode(String[] array, String separator, int start) {
		return implode(array, separator, start, array.length);
	}

	/**
	 * Implode a string array into a string with given separator.
	 *
	 * @param separator The separator string.
	 * @param array     The array.
	 * @param start     The start index.
	 * @param end       The end index.
	 * @return The imploded string.
	 */
	@NotNull
	public static String implode(String[] array, String separator, int start, int end) {
		if (array == null || array.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++) {
			sb.append(array[i]);
			if (i != end - 1 && separator != null) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * Finds partial matches between a given string and a list of strings.
	 *
	 * @param token      The string to match.
	 * @param collection The list of strings to match against.
	 * @return A list of strings that partially match the given string.
	 */
	@NotNull
	public static List<String> getPartialMatches(@NotNull String token, @NotNull Iterable<String> collection) {
		return getPartialMatches(token, collection, null);
	}

	/**
	 * Finds partial matches between a given string and a list of strings.
	 *
	 * @param token      The string to match.
	 * @param collection The list of strings to match against.
	 * @param result     The list to add the matches to.
	 * @return A list of strings that partially match the given string.
	 */
	@NotNull
	public static List<String> getPartialMatches(@NotNull String token, @NotNull Iterable<String> collection, @Nullable List<String> result) {
		if (result == null) {
			result = Lists.newArrayList();
		}

		for (String s : collection) {
			if (startsWithIgnoreCase(s, token)) {
				result.add(s);
			}
		}

		return result;
	}

	/**
	 * Checks if a string starts with another string, ignoring case.
	 *
	 * @param string The string to check.
	 * @param prefix The prefix.
	 * @return Whether the string starts with the prefix.
	 */
	public static boolean startsWithIgnoreCase(@NotNull String string, @NotNull String prefix) {
		return string.regionMatches(true, 0, prefix, 0, prefix.length());
	}

	/*
	 * 	Text width
	 */

	/**
	 * Get the width of the given character.
	 *
	 * @param c The character.
	 * @return The width.
	 */
	public static int getCharWidth(char c) {
		if (c >= '\u2588' && c <= '\u258F') {
			return ('\u258F' - c) + 2;
		}
		switch (c) {
			case ' ':
				return 4;
			case '\u2714':
				return 8;
			case '\u2718':
				return 7;
			default:
				break;
		}
		MapFont.CharacterSprite characterSprite = MinecraftFont.Font.getChar(c);
		return characterSprite == null ? 0 : characterSprite.getWidth() + 1;
	}

	/**
	 * Get the width of the given string.
	 *
	 * @param text The string.
	 * @return The width.
	 */
	public static int getTextWidth(@NotNull String text) {
		if (text.contains("\n")) {
			throw new IllegalArgumentException("Text cannot contain newline characters.");
		}

		int width = 0;
		boolean isBold = false;
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			int charWidth = getCharWidth(c);

			if (c == ChatColor.COLOR_CHAR && i < chars.length - 1) {
				c = chars[++i];
				isBold = c == 'l' || c == 'L' || (c != 'r' && c != 'R' && isBold);
				charWidth = 0;
			}

			if (isBold && c != ' ' && charWidth > 0) {
				width++;
			}

			width += charWidth;
		}
		return width;
	}

	/**
	 * Expand the given string to the given width.
	 *
	 * @param string The string.
	 * @param width  The target width.
	 * @return The expanded string.
	 */
	@NotNull
	public static String expandToWidth(@NotNull String string, int width) {
		int spaces = (int) Math.round((width - getTextWidth(string)) / 4.0);
		return string + StringUtils.repeat(" ", spaces);
	}

}

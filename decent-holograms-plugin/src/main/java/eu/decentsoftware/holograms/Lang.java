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

package eu.decentsoftware.holograms;

import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.config.FileConfig;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public final class Lang {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    public static final String UPDATE_MESSAGE = "\n&fA newer version of &3DecentHolograms &fis available. Download it from: &7https://www.spigotmc.org/resources/96927/";

    /**
     * The prefix of the plugin. Use "{prefix}" in any other message to
     * display the prefix.
     */
    public static String PREFIX = "&8[&3DH&8] &7";

    /*
     *  Reload methods
     */

    /**
     * The "lang.yml" file.
     */
    private static FileConfig fileConfig;

    /**
     * Get the "lang.yml" file as a {@link FileConfig}.
     *
     * @return The "lang.yml" file as a {@link FileConfig}.
     */
    @Contract(pure = true)
    public static FileConfig getConfig() {
        return fileConfig;
    }

    /**
     * Reload the "lang.yml" file.
     *
     * @since 1.0.0
     */
    public static void reload() {
        if (fileConfig == null) {
            fileConfig = new FileConfig(PLUGIN, "lang.yml");
        } else {
            fileConfig.reload();
        }

        // Reload prefix
        PREFIX = fileConfig.getString("prefix", PREFIX);
    }

    /*
     *  Utility methods
     */

    /**
     * Format the given string for the given player using all the features of
     * this plugin and all the supported plugins as well.
     * <p>
     * This method will replace all the supported placeholders with the
     * correct values and replace all the supported colors.
     *
     * @param string  The string to format.
     * @param profile The profile to format the string for. (Can be null)
     * @return The formatted string.
     */
    @NonNull
    public static String formatString(@NonNull String string, @Nullable Profile profile) {
        return formatString(string, profile, (Object) null);
    }

    /**
     * Format the given string for the given player using all the features of
     * this plugin and all the supported plugins as well.
     * <p>
     * This method will replace all the supported placeholders with the
     * correct values and replace all the supported colors.
     *
     * @param string  The string to format.
     * @param profile The profile to format the string for. (Can be null)
     * @param args    Java style arguments.
     * @return The formatted string.
     */
    @NonNull
    public static String formatString(@NonNull String string, @Nullable Profile profile, Object... args) {
        if (args != null && args.length > 0) {
            string = Common.format(string, args);
        }
        string = string.replace("{prefix}", PREFIX);
        string = PLUGIN.getReplacementRegistry().replace(string, profile);
        Player player;
        if (profile != null && (player = profile.getPlayer()) != null) {
            string = PAPI.setPlaceholders(player, string);
        }
        string = Common.colorize(string);
        return string;
    }

    /**
     * Format the given string for the given player using all the features of
     * this plugin and all the supported plugins as well.
     * <p>
     * This method will replace all the supported placeholders with the
     * correct values and replace all the supported colors.
     *
     * @param string The string to format.
     * @param player The player to format the string for. (Can be null)
     * @return The formatted string.
     */
    @NonNull
    public static String formatString(@NonNull String string, @Nullable Player player) {
        if (player == null) {
            return formatString(string, (Profile) null);
        }
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player.getUniqueId());
        return formatString(string, profile);
    }

    /**
     * Format the given string for using all the features of this plugin
     * and all the supported plugins as well.
     * <p>
     * This method will replace all the supported placeholders with the
     * correct values and replace all the supported colors.
     *
     * @param string The string to format.
     * @param args   Java style arguments.
     * @return The formatted string.
     */
    @NonNull
    public static String formatString(@NonNull String string, Object... args) {
        return formatString(string, null, args);
    }

    /**
     * Send the given string to the given command sender after formatting it.
     *
     * @param sender  The recipient of the message.
     * @param message The message to send.
     * @see #formatString(String, Profile, Object...) for more information.
     */
    public static void tell(@NonNull CommandSender sender, @NonNull String message) {
        if (sender instanceof Player) {
            message = formatString(message, (Player) sender);
        } else {
            message = formatString(message);
        }
        sender.sendMessage(message);
    }

    /**
     * Send a message to the player, after loading it from the config at the given path
     * and formatting it.
     *
     * @param sender The recipient of the message.
     * @param path   The path to the message in the config.
     */
    public static void confTell(@NonNull CommandSender sender, @NonNull String path) {
        confTell(sender, path, (Object) null);
    }

    /**
     * Send a message to the player, after loading it from the config at the given path
     * and formatting it.
     *
     * @param sender The recipient of the message.
     * @param path   The path to the message in the config.
     * @param args   Java style arguments.
     */
    public static void confTell(@NonNull CommandSender sender, @NonNull String path, Object... args) {
        if (fileConfig.isString(path)) {
            tell(sender, fileConfig.getString(path, ""), args);
        } else if (fileConfig.isList(path)) {
            for (String line : fileConfig.getStringList(path)) {
                tell(sender, line, args);
            }
        }
    }

    /**
     * Send the given string to the given command sender after formatting it.
     *
     * @param sender  The recipient of the message.
     * @param message The message to send.
     * @param args    Java style arguments.
     * @see #formatString(String, Profile, Object...) for more information.
     */
    public static void tell(@NonNull CommandSender sender, @NonNull String message, @NonNull Object... args) {
        tell(sender, Common.format(message, args));
    }

    /**
     * Send the version message to a command sender.
     *
     * @param sender The recipient of the message.
     */
    public static void sendVersionMessage(CommandSender sender) {
        Common.tell(sender,
                "\n&fThis server is running &3DecentHolograms v%s&f by &bd0by&f : &7%s",
                PLUGIN.getDescription().getVersion(),
                "https://www.spigotmc.org/resources/96927/"
        );
    }

    /**
     * Notify the given command sender about an update.
     *
     * @param sender The recipient of the message.
     */
    public static void sendUpdateMessage(CommandSender sender) {
        Common.tell(sender, UPDATE_MESSAGE);
    }

}

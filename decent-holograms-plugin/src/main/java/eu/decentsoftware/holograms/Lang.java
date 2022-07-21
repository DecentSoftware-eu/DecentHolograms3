package eu.decentsoftware.holograms;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.config.CFG;
import eu.decentsoftware.holograms.api.utils.config.ConfigValue;
import eu.decentsoftware.holograms.hooks.PAPI;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@UtilityClass
public final class Lang {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();

    // ========== GENERAL ========== //

    /**
     * The prefix of the plugin. Use "{prefix}" in any other message to
     * display the prefix.
     */
    @ConfigValue("PREFIX")
    public static String PREFIX = "&8[&3DecentHolograms&8] &7";

    /**
     * The message that is displayed when a player tries to use something,
     * they don't have permission for.
     */
    @ConfigValue("NO_PERM")
    public static String NO_PERM = "{prefix}&cYou don't have permission to use this.";

    @ConfigValue("RELOADED")
    public static String RELOADED = "{prefix}&aSuccessfully reloaded. (Took {took} ms)";

    // ========== COMMAND ========== //

    @ConfigValue("COMMAND.ONLY_PLAYER")
    public static String COMMAND_ONLY_PLAYER = "{prefix}&cYou must be a player to use this command.";
    @ConfigValue("COMMAND.INVALID_PLAYER")
    public static String COMMAND_INVALID_PLAYER = "{prefix}&cThat player doesn't exist.";
    @ConfigValue("COMMAND.USAGE")
    public static String COMMAND_USAGE = "{prefix}&7Usage: {secondary}%1$s";

    /*
     *  Reload methods
     */

    /**
     * The "lang.yml" file.
     */
    private static File file;

    /**
     * Get the "lang.yml" file.
     *
     * @return The "lang.yml" file.
     * @since 1.0.0
     */
    public static File getFile() {
        if (file == null) {
            file = new File(PLUGIN.getDataFolder(), "lang.yml");
        }
        return file;
    }

    /**
     * Reload the "lang.yml" file.
     *
     * @since 1.0.0
     */
    public static void reload() {
        CFG.load(Lang.class, getFile());
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
    @NotNull
    public String formatString(@NotNull String string, @Nullable Profile profile) {
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
     * @param args    Arguments to replace in the string.
     * @return The formatted string.
     */
    @NotNull
    public String formatString(@NotNull String string, @Nullable Profile profile, Object... args) {
        if (args != null && args.length > 0) {
            string = String.format(string, args);
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
    @NotNull
    public String formatString(@NotNull String string, @Nullable Player player) {
        if (player == null) {
            return formatString(string, (Profile) null);
        }
        Profile profile = PLUGIN.getProfileRegistry().get(player.getName());
        return formatString(string, profile);
    }

    /**
     * Format the given string replacing "{prefix}" with the plugin prefix and colorizing it.
     *
     * @param string The string to format.
     * @return The formatted string.
     */
    @NotNull
    public static String formatString(@NotNull String string) {
        return formatString(string, (Profile) null);
    }

    /**
     * Send the given string to the given command sender after formatting it.
     *
     * @param sender  The command sender.
     * @param message The message to send.
     * @see #formatString(String, Profile) for more information.
     * @see #formatString(String) for more information.
     */
    public void tell(@NotNull CommandSender sender, @NotNull String message) {
        if (sender instanceof Player) {
            message = formatString(message, (Player) sender);
        } else {
            message = formatString(message);
        }
        sender.sendMessage(message);
    }

    /**
     * Send the given string to the given command sender after formatting it.
     *
     * @param sender  The command sender.
     * @param message The message to send.
     * @param args    Java style arguments.
     * @see #formatString(String, Profile) for more information.
     * @see #formatString(String) for more information.
     */
    public void tell(@NotNull CommandSender sender, @NotNull String message, @NotNull Object... args) {
        tell(sender, String.format(message, args));
    }

    /**
     * Send the version message to a command sender.
     *
     * @param sender The command sender.
     */
    public static void sendVersionMessage(CommandSender sender) {
        Common.tell(sender,
                "\n&fThis server is running &3DecentHolograms v%s&f by &bd0by&f : &7%s",
                DecentHologramsAPI.getInstance().getDescription().getVersion(),
                "https://www.spigotmc.org/resources/96927/"
        );
    }

    /**
     * Notify the given command sender about an update.
     *
     * @param sender The command sender.
     */
    public static void sendUpdateMessage(CommandSender sender) {
        Common.tell(sender,
                "\n&fA newer version of &3DecentHolograms &fis available. Download it from: &7%s",
                "https://www.spigotmc.org/resources/96927/"
        );
    }

}

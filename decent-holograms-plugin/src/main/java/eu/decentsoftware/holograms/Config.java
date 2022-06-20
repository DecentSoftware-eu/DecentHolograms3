package eu.decentsoftware.holograms;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.config.CFG;
import eu.decentsoftware.holograms.api.config.ConfigValue;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all the configuration values from config.yml.
 */
@UtilityClass
public final class Config {

    public static final String ADMIN_PERM = "ds.decentholograms.admin";

    private static boolean updateAvailable = false;

    /*
     *  Options
     */

    // -- General

    @ConfigValue("check-for-updates")
    public static boolean CHECK_FOR_UPDATES = true;

    // -- Date & Time

    @ConfigValue("datetime.time-format")
    public static String DATETIME_TIME_FORMAT = "HH:mm:ss";
    @ConfigValue("datetime.date-format")
    public static String DATETIME_DATE_FORMAT = "dd:MM:yyyy";
    @ConfigValue("datetime.zone")
    public static String DATETIME_ZONE = "GMT+0";

    // -- Pinger

    @ConfigValue("pinger.enabled")
    public static boolean PINGER_ENABLED = false;
    @ConfigValue(value = "pinger.update-interval", min = 20, max = 1200)
    public static long PINGER_UPDATE_INTERVAL = 20;
    @ConfigValue(value = "pinger.timeout", min = 50, max = 5000)
    public static int PINGER_TIMEOUT = 500;
    @ConfigValue("pinger.servers")
    public static List<String> PINGER_SERVERS = new ArrayList<>();
    @ConfigValue("pinger.status.online")
    public static String PINGER_STATUS_ONLINE = "&aOnline";
    @ConfigValue("pinger.status.offline")
    public static String PINGER_STATUS_OFFLINE = "&cOffline";
    @ConfigValue("pinger.trim-motd")
    public static boolean PINGER_TRIM_MOTD = true;

    // -- Messages

    @ConfigValue("messages.prefix")
    public static String PREFIX = "&8[&3DecentHolograms&8] &7";

    /*
     *  General Methods
     */

    /**
     * Reload the configuration.
     */
    public static void reload() {
        CFG.load(Config.class, DecentHologramsAPI.getInstance().getConfigFile());
    }

    /**
     * Check if an update is available.
     *
     * @return True if an update is available.
     */
    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }

    /**
     * Set if an update is available.
     *
     * @param updateAvailable True if an update is available.
     */
    public static void setUpdateAvailable(boolean updateAvailable) {
        Config.updateAvailable = updateAvailable;
    }

    /*
     *  Utility Methods
     */

    /**
     * Send a message to command sender. This method replaces '{prefix}' placeholder
     * and translates all color codes including hex colors.
     *
     * @param sender The command sender.
     * @param message The message.
     * @param args Arguments for java string formatting.
     */
    public static void tell(CommandSender sender, String message, Object... args) {
        message = message.replace("{prefix}", PREFIX);
        Common.tell(sender, message, args);
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

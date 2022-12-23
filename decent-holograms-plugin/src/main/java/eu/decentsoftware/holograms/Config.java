package eu.decentsoftware.holograms;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.utils.config.CFG;
import eu.decentsoftware.holograms.api.utils.config.ConfigValue;
import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all the configuration values from config.yml.
 */
@UtilityClass
public final class Config {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private static boolean updateAvailable = false;

    // ========== PERMISSIONS ========== //

    public static final String ADMIN_PERM = "dh.admin";

    // ========== GENERAL ========== //

    @ConfigValue("check-for-updates")
    public static boolean CHECK_FOR_UPDATES = true;
    @ConfigValue("per-hologram-file")
    public static boolean PER_HOLOGRAM_FILE = true;

    // ========== DATETIME ========== //

    @ConfigValue("datetime.time-format")
    public static String DATETIME_TIME_FORMAT = "HH:mm:ss";
    @ConfigValue("datetime.date-format")
    public static String DATETIME_DATE_FORMAT = "dd:MM:yyyy";
    @ConfigValue("datetime.zone")
    public static String DATETIME_ZONE = "GMT+0";

    // ========== PINGER ========== //

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
    @ConfigValue("pinger.status.full")
    public static String PINGER_STATUS_FULL = "&6Full";
    @ConfigValue("pinger.trim-motd")
    public static boolean PINGER_TRIM_MOTD = true;

    /*
     *  Reload methods
     */

    /**
     * The "config.yml" file.
     */
    private static FileConfig config;

    /**
     * Get the "config.yml" file as a {@link FileConfig}.
     *
     * @return The "config.yml" file as a {@link FileConfig}.
     */
    public static FileConfig getConfig() {
        return config;
    }

    /**
     * Reload the "config.yml" file.
     *
     * @since 1.0.0
     */
    public static void reload() {
        if (config == null) {
            config = new FileConfig(PLUGIN, "config.yml");
        } else {
            config.reload();
        }

        CFG.load(Config.class, config.getFile());
    }

    /*
     *  General Methods
     */

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

}

package eu.decentsoftware.holograms;

import dev.dejvokep.boostedyaml.YamlDocument;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.config.CFG;
import eu.decentsoftware.holograms.api.utils.config.ConfigValue;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all the configuration values from config.yml.
 */
@UtilityClass
public final class Config {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();
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
    private static File file;
    private static YamlDocument config;

    /**
     * Get the "config.yml" file.
     *
     * @return The "config.yml" file.
     * @since 1.0.0
     */
    public static File getFile() {
        if (file == null) {
            file = new File(PLUGIN.getDataFolder(), "config.yml");
        }
        return file;
    }

    /**
     * Get the "config.yml" file as a {@link YamlDocument}.
     *
     * @return The "config.yml" file as a {@link YamlDocument}.
     */
    public static YamlDocument getConfig() {
        return config;
    }

    /**
     * Reload the "config.yml" file.
     *
     * @since 1.0.0
     */
    public static void reload() {
        CFG.load(Config.class, getFile());

        try {
            if (config == null) {
                config = YamlDocument.create(getFile());
            } else {
                config.reload();
            }
        } catch (IOException e) {
            PLUGIN.getLogger().warning("Could not reload config.yml");
            e.printStackTrace();
        }
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

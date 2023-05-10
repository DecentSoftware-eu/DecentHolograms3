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

import eu.decentsoftware.holograms.utils.config.CFG;
import eu.decentsoftware.holograms.utils.config.ConfigValue;
import eu.decentsoftware.holograms.utils.config.FileConfig;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all the configuration values from config.yml.
 */
@UtilityClass
public final class Config {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private static boolean updateAvailable = false;
    private static String updateVersion = null;

    /**
     * The regex used for validating names of holograms, animations, etc.
     */
    public static final String NAME_REGEX = "[a-zA-Z0-9_-]+";

    // ========== PERMISSIONS ========== //

    public static final String ADMIN_PERM = "dh.admin";

    // ========== GENERAL ========== //

    @ConfigValue("check-for-updates")
    public static boolean CHECK_FOR_UPDATES = true;

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
    private static FileConfig fileConfig;

    /**
     * Get the "config.yml" file as a {@link FileConfig}.
     *
     * @return The "config.yml" file as a {@link FileConfig}.
     */
    public static FileConfig getFileConfig() {
        return fileConfig;
    }

    /**
     * Reload the "config.yml" file.
     *
     * @since 1.0.0
     */
    public static void reload() {
        if (fileConfig == null) {
            fileConfig = new FileConfig(PLUGIN, "config.yml");
        } else {
            fileConfig.reload();
        }

        CFG.load(PLUGIN, Config.class, fileConfig.getFile());
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

    /**
     * Get the version of the update.
     *
     * @return The version of the update.
     */
    @Nullable
    public static String getUpdateVersion() {
        return updateVersion;
    }

    /**
     * Set the version of the update.
     *
     * @param updateVersion The version of the update.
     */
    public static void setUpdateVersion(@Nullable String updateVersion) {
        Config.updateVersion = updateVersion;
    }

}

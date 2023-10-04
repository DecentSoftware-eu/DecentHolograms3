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

package eu.decentsoftware.holograms.nms.utils;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Enum of supported NMS versions.
 *
 * @author d0by
 * @since 3.0.0
 */
public enum Version {
    v1_8_R1(8),
    v1_8_R2(8),
    v1_8_R3(8),
    v1_9_R1(9),
    v1_9_R2(9),
    v1_10_R1(10),
    v1_11_R1(11),
    v1_12_R1(12),
    v1_13_R1(13),
    v1_13_R2(13),
    v1_14_R1(14),
    v1_15_R1(15),
    v1_16_R1(16),
    v1_16_R2(16),
    v1_16_R3(16),
    v1_17_R1(17),
    v1_18_R1(18),
    v1_18_R2(18),
    v1_19_R1(19),
    v1_19_R2(19),
    v1_19_R3(19),
    v1_20_R1(20),
    v1_20_R2(20),
    ;

    /*
     *  Version
     */

    private final int minor;

    @Contract(pure = true)
    Version(final int minor) {
        this.minor = minor;
    }

    /**
     * Get the minor version number.
     * <p>
     * For example, for 1.12.2, this would return 12.
     *
     * @return The minor version number.
     */
    @Contract(pure = true)
    public int getMinor() {
        return minor;
    }

    /*
     *  Static
     */

    /**
     * The current version of the server.
     */
    public static final Version CURRENT;

    static {
        CURRENT = Version.fromString(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
    }

    /**
     * Parse a Version from string.
     *
     * @param version The string.
     * @return The parsed Version or null.
     */
    @Contract("null -> null")
    @Nullable
    public static Version fromString(final String version) {
        if (version == null) {
            return null;
        }

        for (Version value : Version.values()) {
            if (value.name().equalsIgnoreCase(version)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Check if the current version supports RGB.
     * <p>
     * RGB is supported in 1.16 and above.
     *
     * @return True if the current version supports RGB.
     * @see <a href="https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes">Formatting Codes</a>
     */
    @Contract(pure = true)
    public static boolean supportsRGB() {
        return afterOrEqual(16);
    }

    // ----- Comparing Minor Versions ----- //

    /**
     * Check if the current version is after the given minor version.
     * <p>
     * Example: If the current version is v1_12_R1, then after(11) will return true
     * because v1_12_R1 has a minor version that is higher than 11.
     *
     * @param minor The minor version.
     * @return True if the current version is after the given minor version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean after(final int minor) {
        return CURRENT.getMinor() > minor;
    }

    /**
     * Check if the current version is after or equal to the given minor version.
     * <p>
     * Example: If the current version is v1_12_R1, then afterOrEqual(12) will return true
     * because v1_12_R1 has a minor version that is higher or equal to 12.
     *
     * @param minor The minor version.
     * @return True if the current version is after or equal to the given minor version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean afterOrEqual(final int minor) {
        return CURRENT.getMinor() >= minor;
    }

    /**
     * Check if the current version is the given minor version.
     * <p>
     * Example: If the current version is v1_12_R1, then only is(12) will return true
     * because v1_12_R1 has a minor version of 12.
     *
     * @param minor The minor version.
     * @return True if the current version is the given minor version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean is(final int minor) {
        return CURRENT.getMinor() == minor;
    }

    /**
     * Check if the current version is before the given minor version.
     * <p>
     * Example: If the current version is v1_12_R1, then before(13) will return true
     * because v1_12_R1 has a minor version that is lower than 13.
     *
     * @param minor The minor version.
     * @return True if the current version is before the given minor version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean before(final int minor) {
        return CURRENT.getMinor() < minor;
    }

    /**
     * Check if the current version is before or equal to the given minor version.
     * <p>
     * Example: If the current version is v1_12_R1, then beforeOrEqual(12) will return true
     * because v1_12_R1 has a minor version that is lower or equal to 12.
     *
     * @param minor The minor version.
     * @return True if the current version is before or equal to the given minor version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean beforeOrEqual(final int minor) {
        return CURRENT.getMinor() <= minor;
    }

    // ----- Comparing Versions ----- //

    /**
     * Check if the current version is after the given version.
     * <p>
     * Example: If the current version is v1_12_R1, then after(Version.v1_11_R1) will return true
     * because v1_12_R1 is higher than v1_11_R1.
     *
     * @param version The version.
     * @return True if the current version is after the given version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean after(final @NonNull Version version) {
        return CURRENT.getMinor() > version.getMinor();
    }

    /**
     * Check if the current version is after or equal to the given version.
     * <p>
     * Example: If the current version is v1_12_R1, then afterOrEqual(Version.v1_12_R1) will return true
     * because v1_12_R1 is higher or equal to v1_12_R1.
     *
     * @param version The version.
     * @return True if the current version is after or equal to the given version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean afterOrEqual(final @NonNull Version version) {
        return CURRENT.getMinor() >= version.getMinor();
    }

    /**
     * Check if the current version is the given version.
     * <p>
     * Example: If the current version is v1_12_R1, then is(Version.v1_12_R1) will return true
     * because v1_12_R1 is equal to v1_12_R1.
     *
     * @param version The version.
     * @return True if the current version is the given version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean is(final @NonNull Version version) {
        return CURRENT.getMinor() == version.getMinor();
    }

    /**
     * Check if the current version is before the given version.
     * <p>
     * Example: If the current version is v1_12_R1, then before(Version.v1_13_R1) will return true
     * because v1_12_R1 is lower than v1_13_R1.
     *
     * @param version The version.
     * @return True if the current version is before the given version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean before(final @NonNull Version version) {
        return CURRENT.getMinor() < version.getMinor();
    }

    /**
     * Check if the current version is before or equal to the given version.
     * <p>
     * Example: If the current version is v1_12_R1, then beforeOrEqual(Version.v1_12_R1) will return true
     * because v1_12_R1 is lower or equal to v1_12_R1.
     *
     * @param version The version.
     * @return True if the current version is before or equal to the given version.
     * @see #getMinor()
     */
    @Contract(pure = true)
    public static boolean beforeOrEqual(final @NonNull Version version) {
        return CURRENT.getMinor() <= version.getMinor();
    }

}

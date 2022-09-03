package eu.decentsoftware.holograms.api.utils.reflect;

import org.jetbrains.annotations.Nullable;

/**
 * Enum of supported NMS versions.
 *
 * @author d0by
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
    ;

    /*
     *  Static
     */

    public static final Version CURRENT;

    static {
        CURRENT = Version.fromString(R.getVersion());
    }

    /**
     * Parse a Version from string.
     *
     * @param version The string.
     * @return The parsed Version or null.
     */
    @Nullable
    public static Version fromString(String version) {
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

    public static boolean after(int minor) {
        return CURRENT.getMinor() > minor;
    }

    public static boolean afterOrEqual(int minor) {
        return CURRENT.getMinor() >= minor;
    }

    public static boolean is(int minor) {
        return CURRENT.getMinor() == minor;
    }

    public static boolean before(int minor) {
        return CURRENT.getMinor() < minor;
    }

    public static boolean beforeOrEqual(int minor) {
        return CURRENT.getMinor() <= minor;
    }

    public static boolean supportsHex() {
        return afterOrEqual(16);
    }

    /*
     *  Version
     */

    private final int minor;

    Version(int minor) {
        this.minor = minor;
    }

    public int getMinor() {
        return minor;
    }

}

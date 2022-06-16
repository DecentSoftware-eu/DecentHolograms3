package eu.decentsoftware.holograms.utils;

import eu.decentsoftware.holograms.Config;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class with some useful methods regarding date and time.
 */
@UtilityClass
public class DatetimeUtils {

    private static final DateTimeFormatter TIME_FORMATTER;
    private static final DateTimeFormatter DATE_FORMATTER;

    static {
        TIME_FORMATTER = createDateTimeFormatter(Config.DATETIME_TIME_FORMAT, Config.DATETIME_ZONE);
        DATE_FORMATTER = createDateTimeFormatter(Config.DATETIME_DATE_FORMAT, Config.DATETIME_ZONE);
    }

    /**
     * Get the current time formatted accordingly to the format set in config.
     *
     * @return The formatted time.
     */
    @NotNull
    public static String getTimeFormatted() {
        return TIME_FORMATTER.format(Instant.now());
    }

    /**
     * Get the current date formatted accordingly to the format set in config.
     *
     * @return The formatted date.
     */
    @NotNull
    public static String getDateFormatted() {
        return DATE_FORMATTER.format(Instant.now());
    }

    private static DateTimeFormatter createDateTimeFormatter(@NotNull String pattern, @NotNull String zone) {
        // Create formatter
        DateTimeFormatter formatter;
        try {
            formatter = DateTimeFormatter.ofPattern(pattern);
        } catch (Throwable ignored) {
            formatter = DateTimeFormatter.ISO_INSTANT;
        }
        // Apply time zone
        try {
            formatter.withZone(ZoneId.of(zone));
        } catch (Throwable ignored) {}
        return formatter;
    }

}

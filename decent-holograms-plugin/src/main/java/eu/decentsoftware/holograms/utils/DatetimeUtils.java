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

import eu.decentsoftware.holograms.Config;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class with some useful methods regarding date and time.
 *
 * @author d0by
 * @since 3.0.0
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
        } catch (Exception ignored) {
            formatter = DateTimeFormatter.ISO_INSTANT;
        }
        // Apply time zone
        try {
            formatter.withZone(ZoneId.of(zone));
        } catch (Exception ignored) {
            formatter.withZone(ZoneId.systemDefault());
        }
        return formatter;
    }

}

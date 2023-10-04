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

package eu.decentsoftware.holograms.utils.color.patterns;

import eu.decentsoftware.holograms.utils.color.DecentColorAPI;
import lombok.NonNull;

import java.util.regex.Matcher;

public class SolidPattern implements Pattern {

    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile(
            // "(?:<c(?:olou?r)?:)?(?:&?#|<&?#)(?<color>[0-9A-Fa-f]{6})>?"
            "(?i)<(?:c(?:olou?r)?:)?&?#(?<color>[0-9A-F]{6})>"
    );

    @NonNull
    @Override
    public String process(@NonNull String string) {
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            String color = matcher.group("color");
            string = string.replace(matcher.group(), String.valueOf(DecentColorAPI.getColor(color)));
        }
        return string;
    }

    @NonNull
    @Override
    public String strip(@NonNull String string) {
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            string = string.replace(matcher.group(), "");
        }
        return string;
    }

}

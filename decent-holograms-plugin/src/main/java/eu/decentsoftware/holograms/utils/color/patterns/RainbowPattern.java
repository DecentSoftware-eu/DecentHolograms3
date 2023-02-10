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

import java.util.regex.Matcher;

public class RainbowPattern implements Pattern {

    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("<RAINBOW(\\d{1,3})>(.*?)</RAINBOW>|\\{RAINBOW(\\d{1,3})}(.*?)\\{/RAINBOW}");

    @Override
    public String process(String string) {
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            String saturation = matcher.group(1);
            String content = matcher.group(2);
            if (saturation == null) {
                saturation = matcher.group(3);
                content = matcher.group(4);
            }
            string = string.replace(matcher.group(), DecentColorAPI.rainbow(content, Float.parseFloat(saturation)));
        }

        return string;
    }
}

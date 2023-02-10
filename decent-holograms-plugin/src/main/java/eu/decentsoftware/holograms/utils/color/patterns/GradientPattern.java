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

import java.awt.*;
import java.util.regex.Matcher;

public class GradientPattern implements Pattern {

    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("<([\\dA-Fa-f]{6})>(.*?)</([\\dA-Fa-f]{6})>|\\{([\\dA-Fa-f]{6})}(.*?)\\{/([\\dA-Fa-f]{6})}");

    @Override
    public String process(String string) {
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            String start = matcher.group(1);
            String content = matcher.group(2);
            String end = matcher.group(3);
            if (start == null) {
                start = matcher.group(4);
                content = matcher.group(5);
                end = matcher.group(6);
            }
            string = string.replace(matcher.group(), DecentColorAPI.color(
                    content,
                    new Color(Integer.parseInt(start, 16)),
                    new Color(Integer.parseInt(end, 16))
            ));
        }
        return string;
    }

}

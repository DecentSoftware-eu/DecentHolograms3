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

import eu.decentsoftware.holograms.nms.utils.Version;
import eu.decentsoftware.holograms.utils.color.DecentColorAPI;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;

public class SolidLegacyPattern implements Pattern {

    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("#?[<{]([\\dA-Fa-f]{6})\\|&([0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx])[}>]");

    @Override
    public String process(String string) {
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            ChatColor color = DecentColorAPI.getColor(matcher.group(1));
            ChatColor legacy = ChatColor.getByChar(matcher.group(2).charAt(0));
            string = string.replace(matcher.group(), (Version.supportsHex() ? color : legacy) + "");
        }
        return string;
    }

}

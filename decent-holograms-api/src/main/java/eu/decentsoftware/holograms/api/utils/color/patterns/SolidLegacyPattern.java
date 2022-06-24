package eu.decentsoftware.holograms.api.utils.color.patterns;

import eu.decentsoftware.holograms.api.utils.color.DecentColorAPI;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
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

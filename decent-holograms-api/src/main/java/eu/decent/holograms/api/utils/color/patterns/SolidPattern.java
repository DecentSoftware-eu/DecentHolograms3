package eu.decent.holograms.api.utils.color.patterns;

import eu.decent.holograms.api.utils.color.DecentColorAPI;

import java.util.regex.Matcher;

public class SolidPattern implements Pattern {

    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("#?[<{]([\\dA-Fa-f]{6})[}>]|#([\\dA-Fa-f]{6})");

    @Override
    public String process(String string) {
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            String color = matcher.group(1);
            if (color == null) {
                color = matcher.group(2);
            }
            string = string.replace(matcher.group(), DecentColorAPI.getColor(color) + "");
        }
        return string;
    }

}

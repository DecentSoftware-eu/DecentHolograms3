package eu.decent.holograms.api.utils.color.patterns;

import eu.decent.holograms.api.utils.color.DecentColorAPI;

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

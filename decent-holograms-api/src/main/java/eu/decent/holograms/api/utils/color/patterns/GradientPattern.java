package eu.decent.holograms.api.utils.color.patterns;

import eu.decent.holograms.api.utils.color.DecentColorAPI;

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

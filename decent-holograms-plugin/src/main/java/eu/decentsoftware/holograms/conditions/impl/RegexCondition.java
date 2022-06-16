package eu.decentsoftware.holograms.conditions.impl;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.conditions.DefaultCondition;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCondition extends DefaultCondition {

    private final Pattern pattern;
    private final String input;

    public RegexCondition(@NotNull Pattern pattern, @NotNull String input) {
        this(false, pattern, input);
    }

    public RegexCondition(boolean inverted, @NotNull Pattern pattern, @NotNull String input) {
        super(inverted);
        this.pattern = pattern;
        this.input = input;
    }

    @Override
    public boolean check(@NotNull Profile profile) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

}

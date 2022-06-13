package eu.decent.holograms.conditions.impl;

import eu.decent.holograms.api.profile.Profile;
import eu.decent.holograms.conditions.DefaultCondition;
import eu.decent.holograms.conditions.ConditionType;
import eu.decent.holograms.hooks.PAPI;
import org.jetbrains.annotations.NotNull;

public class ComparingCondition extends DefaultCondition {

    private final ConditionType type;
    private final String compare;
    private final String input;

    public ComparingCondition(@NotNull ConditionType type, @NotNull String compare, @NotNull String input) {
        this(false, type, compare, input);
    }

    public ComparingCondition(boolean inverted, @NotNull ConditionType type, @NotNull String compare, @NotNull String input) {
        super(inverted);
        this.type = type;
        this.compare = compare;
        this.input = input;
    }

    @Override
    public boolean check(@NotNull Profile profile) {
        String compare = PAPI.setPlaceholders(profile.getPlayer(), this.compare);
        String input = PAPI.setPlaceholders(profile.getPlayer(), this.input);
        if (type.name().startsWith("STRING")) {
            switch (type) {
                case STRING_CONTAINS:
                    return input.contains(compare);
                case STRING_EQUAL:
                    return input.equals(compare);
                case STRING_EQUAL_IGNORECASE:
                    return input.equalsIgnoreCase(compare);
            }
        } else {
            int compareInteger = Integer.parseInt(compare);
            int inputInteger = Integer.parseInt(input);
            switch (type) {
                case LESS:
                    return inputInteger < compareInteger;
                case LESS_EQUAL:
                    return inputInteger <= compareInteger;
                case EQUAL:
                    return inputInteger == compareInteger;
                case GREATER_EQUAL:
                    return inputInteger >= compareInteger;
                case GREATER:
                    return inputInteger > compareInteger;
            }
        }
        return false;
    }

}

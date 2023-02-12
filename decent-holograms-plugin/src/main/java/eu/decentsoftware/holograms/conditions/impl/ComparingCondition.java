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

package eu.decentsoftware.holograms.conditions.impl;

import eu.decentsoftware.holograms.conditions.ConditionType;
import eu.decentsoftware.holograms.conditions.Condition;
import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.profile.Profile;
import org.jetbrains.annotations.NotNull;

public class ComparingCondition extends Condition {

    private transient final @NotNull ConditionType type;
    private final @NotNull String compare;
    private final @NotNull String input;

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

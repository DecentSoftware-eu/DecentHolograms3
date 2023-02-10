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

import eu.decentsoftware.holograms.conditions.Condition;
import eu.decentsoftware.holograms.profile.Profile;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCondition extends Condition {

    private final @NotNull Pattern pattern;
    private final @NotNull String input;

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

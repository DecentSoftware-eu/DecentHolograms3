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
import lombok.NonNull;
import org.bukkit.entity.Player;

public class ExpCondition extends Condition {

    private final int minLevel;

    public ExpCondition(int minLevel) {
        this(false, minLevel);
    }

    public ExpCondition(boolean inverted, int minLevel) {
        super(inverted);
        this.minLevel = minLevel;
    }

    @Override
    public boolean check(@NonNull Player player) {
        return player.getLevel() >= minLevel;
    }

}

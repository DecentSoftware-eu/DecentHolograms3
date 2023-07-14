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
import eu.decentsoftware.holograms.hooks.VaultHook;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MoneyCondition extends Condition {

    private final double amount;

    public MoneyCondition(double amount) {
        this.amount = amount;
    }

    public MoneyCondition(boolean inverted, double amount) {
        super(inverted);
        this.amount = amount;
    }

    @Override
    public boolean check(@NotNull Player player) {
        return VaultHook.getMoney(player) >= amount;
    }

}

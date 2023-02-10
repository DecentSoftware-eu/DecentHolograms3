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

package eu.decentsoftware.holograms.hooks;

import lombok.experimental.UtilityClass;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to hook into Vault. It contains methods
 * for interacting with the economy.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class VaultHook {

    private static Economy economy = null;

    /**
     * Get the amount of money a player has.
     *
     * @param player The player to get the money from.
     * @return The amount of money the player has.
     */
    public static double getMoney(@NotNull Player player) {
        if (economy == null && !setupEconomy()) {
            return 0.0;
        }
        return economy.getBalance(player);
    }

    /**
     * Set the amount of money a player has.
     *
     * @param player The player to set the money for.
     * @param amount The amount of money to set.
     * @return True if the operation was successful, false otherwise.
     */
    public static boolean setMoney(@NotNull Player player, double amount) {
        if (economy == null && !setupEconomy()) {
            return false;
        }
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    /**
     * Add money to a player.
     *
     * @param player The player to add the money to.
     * @param amount The amount of money to add.
     * @return True if the operation was successful, false otherwise.
     */
    public static boolean addMoney(@NotNull Player player, double amount) {
        if (economy == null && !setupEconomy()) {
            return false;
        }
        return economy.depositPlayer(player, amount).transactionSuccess();
    }

    /**
     * Remove money from a player.
     *
     * @param player The player to remove the money from.
     * @param amount The amount of money to remove.
     * @return True if the operation was successful, false otherwise.
     */
    public static boolean removeMoney(@NotNull Player player, double amount) {
        if (economy == null && !setupEconomy()) {
            return false;
        }
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    /**
     * Set up the economy hook.
     *
     * @return True if the hook was successful, false otherwise.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        return (economy = rsp.getProvider()) != null;
    }

}
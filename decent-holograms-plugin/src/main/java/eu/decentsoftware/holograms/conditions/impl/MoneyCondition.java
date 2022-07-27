package eu.decentsoftware.holograms.conditions.impl;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.conditions.DefaultCondition;
import eu.decentsoftware.holograms.hooks.VaultHook;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MoneyCondition extends DefaultCondition {

    private final double amount;

    public MoneyCondition(double amount) {
        this.amount = amount;
    }

    public MoneyCondition(boolean inverted, double amount) {
        super(inverted);
        this.amount = amount;
    }

    @Override
    public boolean check(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player == null) {
            return false;
        }
        return VaultHook.getMoney(player) >= amount;
    }

}

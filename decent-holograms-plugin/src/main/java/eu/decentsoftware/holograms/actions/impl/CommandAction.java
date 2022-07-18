package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandAction extends DefaultAction {

    protected final @NotNull String command;

    public CommandAction(@NotNull String command) {
        this.command = command;
    }

    public CommandAction(long delay, double chance, @NotNull String command) {
        super(delay, chance);
        this.command = command;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            Bukkit.dispatchCommand(player, command);
        }
    }

}

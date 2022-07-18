package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class ConsoleAction extends DefaultAction {

    protected final @NotNull String command;

    public ConsoleAction(@NotNull String command) {
        this.command = command;
    }

    public ConsoleAction(long delay, double chance, @NotNull String command) {
        super(delay, chance);
        this.command = command;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

}

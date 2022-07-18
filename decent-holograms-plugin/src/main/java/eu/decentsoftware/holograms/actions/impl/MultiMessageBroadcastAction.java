package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiMessageBroadcastAction extends MultiMessageAction {

    public MultiMessageBroadcastAction(@NotNull List<String> messages) {
        super(messages);
    }

    public MultiMessageBroadcastAction(long delay, double chance, @NotNull List<String> messages) {
        super(delay, chance, messages);
    }

    @Override
    public void execute(@NotNull Profile profile) {
        for (String message : messages) {
            Bukkit.broadcastMessage(Lang.formatString(message, profile));
        }
    }

}

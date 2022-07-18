package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatAction extends DefaultAction {

    protected final @NotNull String message;

    public ChatAction(@NotNull String message) {
        this.message = message;
    }

    public ChatAction(long delay, double chance, @NotNull String message) {
        super(delay, chance);
        this.message = message;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            player.chat(message);
        }
    }

}

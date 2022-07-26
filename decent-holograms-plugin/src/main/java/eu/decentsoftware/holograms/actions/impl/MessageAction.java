package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends DefaultAction {

    protected final @NotNull String message;

    public MessageAction(@NotNull String message) {
        this.message = message;
    }

    public MessageAction(long delay, double chance, @NotNull String message) {
        super(delay, chance);
        this.message = message;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            Lang.tell(player, message);
        }
    }

}

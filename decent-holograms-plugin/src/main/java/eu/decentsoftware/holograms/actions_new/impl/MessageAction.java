package eu.decentsoftware.holograms.actions_new.impl;

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.actions_new.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends DefaultAction {

    private final @NotNull String message;

    public MessageAction(@NotNull String message) {
        super(0, -1.0);
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

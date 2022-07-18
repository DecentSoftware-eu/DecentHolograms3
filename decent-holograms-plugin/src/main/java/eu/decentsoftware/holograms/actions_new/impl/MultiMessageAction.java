package eu.decentsoftware.holograms.actions_new.impl;

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.actions_new.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiMessageAction extends DefaultAction {

    private final @NotNull List<String> messages;

    public MultiMessageAction(@NotNull List<String> messages) {
        super(0, -1.0);
        this.messages = messages;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            for (String message : messages) {
                Lang.tell(player, message);
            }
        }
    }

}

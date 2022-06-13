package eu.decent.holograms.components.page;

import eu.decent.holograms.actions.DefaultActionHolder;
import eu.decent.holograms.api.actions.ActionHolder;
import eu.decent.holograms.api.component.hologram.Hologram;
import eu.decent.holograms.api.component.page.Page;
import eu.decent.holograms.api.component.page.PageLineHolder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DefaultPage implements Page {

    private final Hologram parent;
    private final PageLineHolder lineHolder;
    private final ActionHolder actionHolder;

    public DefaultPage(@NotNull Hologram parent) {
        this.parent = parent;
        this.lineHolder = new DefaultPageLineHolder(this);
        this.actionHolder = new DefaultActionHolder();
    }

    @Override
    public Hologram getParent() {
        return parent;
    }

    @NotNull
    @Override
    public PageLineHolder getLineHolder() {
        return lineHolder;
    }

    @Override
    public ActionHolder getActions() {
        return actionHolder;
    }

    @Override
    public void display(@NotNull Player player) {

    }

    @Override
    public void hide(@NotNull Player player) {

    }

    @Override
    public void update(@NotNull Player player) {

    }

}

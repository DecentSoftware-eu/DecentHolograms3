package eu.decentsoftware.holograms.components.page;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.component.page.PageLineHolder;
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

    @NotNull
    @Override
    public Hologram getParent() {
        return parent;
    }

    @NotNull
    @Override
    public PageLineHolder getLineHolder() {
        return lineHolder;
    }

    @NotNull
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
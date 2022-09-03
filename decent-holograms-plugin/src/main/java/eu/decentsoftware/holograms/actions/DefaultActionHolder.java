package eu.decentsoftware.holograms.actions;

import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.utils.collection.DecentList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultActionHolder implements ActionHolder {

    private final @NotNull List<Action> actions;

    public DefaultActionHolder() {
        this(new DecentList<>());
    }

    public DefaultActionHolder(@NotNull List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public void addAction(@NotNull Action action) {
        this.actions.add(action);
    }

    @Override
    public void removeAction(@NotNull Action action) {
        this.actions.remove(action);
    }

    @Override
    public void removeAction(int index) {
        this.actions.remove(index);
    }

    @Override
    public void clearActions() {
        this.actions.clear();
    }

    @NotNull
    @Override
    public List<Action> getActions() {
        return this.actions;
    }

}

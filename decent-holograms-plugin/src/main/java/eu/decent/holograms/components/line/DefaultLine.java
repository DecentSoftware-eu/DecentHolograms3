package eu.decent.holograms.components.line;

import eu.decent.holograms.actions.DefaultActionHolder;
import eu.decent.holograms.api.actions.ActionHolder;
import eu.decent.holograms.api.component.common.PositionManager;
import eu.decent.holograms.api.component.line.*;
import eu.decent.holograms.api.component.page.Page;
import eu.decent.holograms.api.conditions.ConditionHolder;
import eu.decent.holograms.components.common.DefaultPositionManager;
import eu.decent.holograms.conditions.DefaultConditionHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class DefaultLine implements Line {

    private final Page parent;
    private final LineSettings settings;
    private final PositionManager positionManager;
    private final ActionHolder actionHolder;
    private final ConditionHolder conditionHolder;
    private LineRenderer renderer;
    private String content;
    private LineType type;

    /**
     * Creates a new instance of {@link DefaultLine}.
     *
     * @param parent The parent page of the line.
     * @param location The location of the line.
     */
    public DefaultLine(@NotNull Page parent, @NotNull Location location) {
        this.parent = parent;
        this.settings = new DefaultLineSettings();
        this.positionManager = new DefaultPositionManager(location);
        this.actionHolder = new DefaultActionHolder();
        this.conditionHolder = new DefaultConditionHolder();
    }

    @NotNull
    @Override
    public Page getParent() {
        return parent;
    }

    @NotNull
    @Override
    public LineType getType() {
        return type;
    }

    @NotNull
    @Override
    public LineSettings getSettings() {
        return settings;
    }

    @NotNull
    @Override
    public PositionManager getPositionManager() {
        return positionManager;
    }

    @NotNull
    @Override
    public LineRenderer getRenderer() {
        return null;
    }

    @NotNull
    @Override
    public ActionHolder getActions() {
        return actionHolder;
    }

    @NotNull
    @Override
    public ConditionHolder getConditions() {
        return conditionHolder;
    }

    @NotNull
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(@NotNull String content) {
        this.content = content;

    }
}

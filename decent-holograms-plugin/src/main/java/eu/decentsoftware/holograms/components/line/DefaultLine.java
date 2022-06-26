package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.common.PositionManager;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.line.LineSettings;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.components.common.DefaultPositionManager;
import eu.decentsoftware.holograms.conditions.DefaultConditionHolder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultLine implements Line {

    private final Page parent;
    private final LineSettings settings;
    private final PositionManager positionManager;
    private final ConditionHolder viewConditions;
    private final ConditionHolder clickConditions;
    private final ActionHolder clickActions;
    private LineRenderer renderer;
    private String content;

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
        this.viewConditions = new DefaultConditionHolder();
        this.clickConditions = new DefaultConditionHolder();
        this.clickActions = new DefaultActionHolder();
    }

    @NotNull
    @Override
    public Page getParent() {
        return parent;
    }

    @NotNull
    @Override
    public LineType getType() {
        return renderer != null ? renderer.getType() : LineType.UNKNOWN;
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

    @Nullable
    @Override
    public LineRenderer getRenderer() {
        return renderer;
    }

    @Override
    public void setRenderer(@NotNull LineRenderer renderer) {
        this.renderer = renderer;
    }

    @NotNull
    @Override
    public ConditionHolder getViewConditionHolder() {
        return viewConditions;
    }

    @NotNull
    @Override
    public ConditionHolder getClickConditionHolder() {
        return clickConditions;
    }

    @NotNull
    @Override
    public ActionHolder getClickActionHolder() {
        return clickActions;
    }

    @Nullable
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(@NotNull String content) {
        this.content = StringUtils.left(content, 256); // Limit content to 256 characters.

        // -- Parse content and update line accordingly
        DecentHologramsAPI.getInstance().getContentParser().parse(this);
    }

}

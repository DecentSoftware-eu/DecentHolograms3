package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.api.DecentHolograms;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DefaultLine implements Line {

    private final @NotNull Page parent;
    private final @NotNull UUID uid;
    private final @NotNull LineSettings settings;
    private final @NotNull PositionManager positionManager;
    private final @NotNull ConditionHolder viewConditions;
    private final @NotNull ConditionHolder clickConditions;
    private final @NotNull ActionHolder clickActions;
    private LineRenderer renderer;
    private String content;

    /**
     * Creates a new instance of {@link DefaultLine}.
     *
     * @param parent   The parent page of the line.
     * @param location The location of the line.
     */
    public DefaultLine(@NotNull Page parent, @NotNull Location location) {
        this.parent = parent;
        this.uid = UUID.randomUUID();
        this.settings = new DefaultLineSettings();
        this.positionManager = new DefaultPositionManager(location);
        this.viewConditions = new DefaultConditionHolder();
        this.clickConditions = new DefaultConditionHolder();
        this.clickActions = new DefaultActionHolder();
    }

    /**
     * Creates a new instance of {@link DefaultLine}.
     *
     * @param parent          The parent page of the line.
     * @param settings        The settings of the line.
     * @param location        The location of the line.
     * @param viewConditions  The conditions that must be met to view the line.
     * @param clickConditions The conditions that must be met to click the line.
     * @param clickActions    The actions that will be executed when the line is clicked.
     * @param content         The content of the line.
     */
    public DefaultLine(@NotNull Page parent, @NotNull LineSettings settings, @NotNull Location location,
                       @NotNull ConditionHolder viewConditions, @NotNull ConditionHolder clickConditions,
                       @NotNull ActionHolder clickActions, @NotNull String content) {
        this.parent = parent;
        this.uid = UUID.randomUUID();
        this.settings = settings;
        this.positionManager = new DefaultPositionManager(location);
        this.viewConditions = viewConditions;
        this.clickConditions = clickConditions;
        this.clickActions = clickActions;
        this.setContent(content);
    }

    @NotNull
    @Override
    public UUID getUid() {
        return uid;
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
        DecentHolograms.getInstance().getContentParserManager().parse(this);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Line other = (Line) obj;
        return this.uid.equals(other.getUid());
    }
}

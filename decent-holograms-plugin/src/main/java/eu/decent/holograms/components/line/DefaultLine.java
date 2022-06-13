package eu.decent.holograms.components.line;

import eu.decent.holograms.actions.DefaultActionHolder;
import eu.decent.holograms.api.actions.ActionHolder;
import eu.decent.holograms.api.component.line.Line;
import eu.decent.holograms.api.component.line.LineSettings;
import eu.decent.holograms.api.component.line.LineType;
import eu.decent.holograms.api.component.page.Page;
import eu.decent.holograms.api.conditions.ConditionHolder;
import eu.decent.holograms.conditions.DefaultConditionHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class DefaultLine implements Line {

    private final Page parent;
    private final LineSettings settings;
    private final ActionHolder actionHolder;
    private final ConditionHolder conditionHolder;
    private LineType type;
    private String content;
    private Location location;

    /**
     * Creates a new instance of {@link DefaultLine}.
     *
     * @param parent The parent page of the line.
     * @param location The location of the line.
     * @param content The content of the line.
     */
    public DefaultLine(@NotNull Page parent, @NotNull Location location, @NotNull String content) {
        this.parent = parent;
        this.location = location;
        this.type = LineType.TEXT;
        this.settings = new DefaultLineSettings();
        this.actionHolder = new DefaultActionHolder();
        this.conditionHolder = new DefaultConditionHolder();
        this.setContent(content);
    }

    @NotNull
    @Override
    public Page getParent() {
        return parent;
    }

    @NotNull
    @Override
    public LineSettings getSettings() {
        return settings;
    }

    @NotNull
    @Override
    public LineType getType() {
        return type;
    }

    @Override
    public ActionHolder getActions() {
        return actionHolder;
    }

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

        // TODO: parse content and set type
    }

    @NotNull
    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(@NotNull Location location) {
        this.location = location;

        // TODO: teleport the line
    }

}

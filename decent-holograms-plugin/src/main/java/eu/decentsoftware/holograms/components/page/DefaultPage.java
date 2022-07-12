package eu.decentsoftware.holograms.components.page;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.common.PositionManager;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineRenderer;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.component.page.PageLineHolder;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.conditions.DefaultConditionHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DefaultPage implements Page {

    private final Hologram parent;
    private final PageLineHolder lineHolder;
    private final ConditionHolder clickConditions;
    private final ActionHolder clickActions;

    public DefaultPage(@NotNull Hologram parent) {
        this.parent = parent;
        this.lineHolder = new DefaultPageLineHolder(this);
        this.clickConditions = new DefaultConditionHolder();
        this.clickActions = new DefaultActionHolder();
    }

    @Override
    public void display(@NotNull Player player) {
        for (Line line : lineHolder.getLines()) {
            LineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                renderer.display(player);
            }
        }
    }

    @Override
    public void hide(@NotNull Player player) {
        for (Line line : lineHolder.getLines()) {
            LineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().hide(player);
            }
        }
    }

    @Override
    public void update(@NotNull Player player) {
        for (Line line : lineHolder.getLines()) {
            LineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().update(player);
            }
        }
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        for (Line line : lineHolder.getLines()) {
            LineRenderer renderer = line.getRenderer();
            if (renderer != null) {
                line.getRenderer().teleport(player, location);
            }
        }
    }

    @Override
    public void recalculate() {
        int pageIndex = getParent().getPageHolder().getIndex(this);
        Set<Player> viewers = getParent().getVisibilityManager().getViewerPlayers(pageIndex);
        Location location = getParent().getPositionManager().getActualLocation();
        for (Line line : getLineHolder().getLines()) {
            PositionManager positionManager = line.getPositionManager();
            positionManager.setLocation(location);
            Location actualLocation = positionManager.getActualLocation();
            for (Player player : viewers) {
                LineRenderer renderer = line.getRenderer();
                if (renderer != null) {
                    line.getRenderer().teleport(player, actualLocation);
                }
            }
            location = location.add(0, line.getSettings().getHeight(), 0);
        }
    }

    @Override
    public Location getNextLineLocation() {
        double height = 0;
        for (Line line : lineHolder.getLines()) {
            height += line.getSettings().getHeight();
        }
        Location location = getParent().getPositionManager().getActualLocation();
        double yOffset = getParent().getSettings().isDownOrigin() ? -height : height;
        return location.add(0, yOffset, 0);
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
    public ConditionHolder getClickConditionHolder() {
        return clickConditions;
    }

    @NotNull
    @Override
    public ActionHolder getClickActionHolder() {
        return clickActions;
    }

}

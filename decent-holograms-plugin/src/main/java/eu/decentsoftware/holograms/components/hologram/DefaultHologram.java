package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.common.PositionManager;
import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramPageHolder;
import eu.decentsoftware.holograms.api.component.hologram.HologramSettings;
import eu.decentsoftware.holograms.api.component.hologram.HologramVisibilityManager;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.components.common.DefaultPositionManager;
import eu.decentsoftware.holograms.conditions.DefaultConditionHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class DefaultHologram implements Hologram {

    private final String name;
    private final HologramSettings settings;
    private final DefaultPositionManager positionManager;
    private final HologramVisibilityManager visibilityManager;
    private final HologramPageHolder pageHolder;
    private final ConditionHolder conditionHolder;
    private final ActionHolder actionHolder;

    /**
     * Creates a new instance of {@link DefaultHologram} with the given name.
     *
     * @param name The name of the hologram.
     * @param location The location of the hologram.
     */
    public DefaultHologram(@NotNull String name, @NotNull Location location) {
        this(name, location, true);
    }

    /**
     * Creates a new instance of {@link DefaultHologram} with the given name.
     *
     * @param name The name of the hologram.
     * @param location The location of the hologram.
     * @param persistent Whether the hologram is persistent.
     */
    public DefaultHologram(@NotNull String name, @NotNull Location location, boolean persistent) {
        this.name = name;
        this.positionManager = new DefaultPositionManager(location);
        this.settings = new DefaultHologramSettings(persistent);
        this.visibilityManager = new DefaultHologramVisibilityManager(this);
        this.pageHolder = new DefaultHologramPageHolder(this);
        this.conditionHolder = new DefaultConditionHolder();
        this.actionHolder = new DefaultActionHolder();
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public HologramSettings getSettings() {
        return settings;
    }

    @NotNull
    @Override
    public PositionManager getPositionManager() {
        return positionManager;
    }

    @NotNull
    @Override
    public HologramVisibilityManager getVisibilityManager() {
        return visibilityManager;
    }

    @NotNull
    @Override
    public HologramPageHolder getPageHolder() {
        return pageHolder;
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

}

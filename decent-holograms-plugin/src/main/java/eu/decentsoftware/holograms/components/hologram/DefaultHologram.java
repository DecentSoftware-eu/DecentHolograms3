package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.actions.DefaultActionHolder;
import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.common.PositionManager;
import eu.decentsoftware.holograms.api.component.hologram.*;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.components.common.DefaultPositionManager;
import eu.decentsoftware.holograms.conditions.DefaultConditionHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

public class DefaultHologram implements Hologram {

    private final String name;
    private final HologramConfig file;
    private final HologramSettings settings;
    private final DefaultPositionManager positionManager;
    private final HologramVisibilityManager visibilityManager;
    private final HologramPageHolder pageHolder;
    private final ConditionHolder conditionHolder;
    private final ActionHolder actionHolder;

    private final AtomicLong lastTick;

    /**
     * Creates a new instance of {@link DefaultHologram} with the given name.
     *
     * @param name     The name of the hologram.
     * @param location The location of the hologram.
     */
    public DefaultHologram(@NotNull String name, @NotNull Location location) {
        this(name, location, true, true);
    }

    /**
     * Creates a new instance of {@link DefaultHologram} with the given name.
     *
     * @param name       The name of the hologram.
     * @param location   The location of the hologram.
     * @param enabled    Whether the hologram is enabled or not.
     * @param persistent Whether the hologram is persistent.
     */
    public DefaultHologram(@NotNull String name, @NotNull Location location, boolean enabled, boolean persistent) {
        this.name = name;
        this.file = new DefaultHologramConfig(this);
        this.positionManager = new DefaultPositionManager(location);
        this.settings = new DefaultHologramSettings(false, persistent);
        this.visibilityManager = new DefaultHologramVisibilityManager(this);
        this.pageHolder = new DefaultHologramPageHolder(this);
        this.conditionHolder = new DefaultConditionHolder();
        this.actionHolder = new DefaultActionHolder();
        this.lastTick = new AtomicLong(0);

        // Start the ticking.
        this.startTicking();

        // Loads the hologram from the file.
        this.file.load(() -> getSettings().setEnabled(enabled));
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void tick() {
        if (!getSettings().isEnabled()) {
            // Do not tick if the hologram is disabled.
            return;
        }

        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastTick.get();

        // If the location is bound, update the location.
        if (positionManager.isLocationBound()) {
            Location location = positionManager.getActualLocation();
            for (int i = 0; i < pageHolder.getPages().size(); i++) {
                Page page = pageHolder.getPages().get(i);
                for (Player player : visibilityManager.getViewerPlayers(i)) {
                    page.teleport(player, location);
                }
            }
        }

        // Update the visibility of the hologram if the location is bound
        // or if the time difference is greater than 500ms.
        if (positionManager.isLocationBound() || 500L < timeDifference) {
            visibilityManager.updateVisibility();
        }

        // Update the content of the hologram.
        if (settings.getUpdateInterval() * 50L < timeDifference) {
            visibilityManager.updateContents();
        }

        // Update the last tick.
        lastTick.set(currentTime);
    }

    @Override
    public void destroy() {
        this.stopTicking();
        visibilityManager.destroy();
    }

    @NotNull
    @Override
    public HologramConfig getConfig() {
        return file;
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

package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.common.PositionManager;
import eu.decentsoftware.holograms.api.component.hologram.*;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.components.common.DefaultPositionManager;
import eu.decentsoftware.holograms.conditions.DefaultConditionHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

public class DefaultHologram implements Hologram {

    private final @NotNull String name;
    private final @NotNull HologramConfig file;
    private final @NotNull HologramSettings settings;
    private final @NotNull DefaultPositionManager positionManager;
    private final @NotNull HologramVisibilityManager visibilityManager;
    private final @NotNull HologramPageHolder pageHolder;
    private final @NotNull ConditionHolder conditionHolder;
    private final @NotNull AtomicLong lastVisibilityUpdate;
    private final @NotNull AtomicLong lastContentUpdate;

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
        this.lastVisibilityUpdate = new AtomicLong(0);
        this.lastContentUpdate = new AtomicLong(0);

        // Start the ticking.
        this.startTicking();

        // Load the hologram from the file.
        this.getConfig().reload().thenRun(() -> getSettings().setEnabled(enabled));
    }

    protected DefaultHologram(@NotNull String name, @NotNull Location location, @NotNull HologramSettings settings,
                              @NotNull ConditionHolder viewConditionHolder) {
        this.name = name;
        this.file = new DefaultHologramConfig(this);
        this.positionManager = new DefaultPositionManager(location);
        this.settings = settings;
        this.visibilityManager = new DefaultHologramVisibilityManager(this);
        this.pageHolder = new DefaultHologramPageHolder(this);
        this.conditionHolder = viewConditionHolder;
        this.lastVisibilityUpdate = new AtomicLong(0);
        this.lastContentUpdate = new AtomicLong(0);

        // Start the ticking.
        this.startTicking();
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

        // If the location is bound, update the location.
        if (positionManager.isLocationBound() || settings.isRotateHorizontal()
                || settings.isRotateVertical() || settings.isRotateHeads()) {
            pageHolder.getPages().forEach(Page::recalculate);
        }

        // Update the visibility of the hologram if the time difference is greater than 500ms.
        if (500L < (currentTime - lastVisibilityUpdate.get())) {
            visibilityManager.updateVisibility();
            lastVisibilityUpdate.set(currentTime);
        }

        // Update the content of the hologram.
        if (settings.getUpdateInterval() * 50L < (currentTime - lastContentUpdate.get())) {
            visibilityManager.updateContents();
            lastContentUpdate.set(currentTime);
        }
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
    public ConditionHolder getViewConditionHolder() {
        return conditionHolder;
    }

}

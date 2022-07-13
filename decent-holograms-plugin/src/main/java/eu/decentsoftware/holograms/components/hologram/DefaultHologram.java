package eu.decentsoftware.holograms.components.hologram;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.decentsoftware.holograms.api.component.common.PositionManager;
import eu.decentsoftware.holograms.api.component.hologram.*;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.api.utils.S;
import eu.decentsoftware.holograms.components.common.DefaultPositionManager;
import eu.decentsoftware.holograms.conditions.DefaultConditionHolder;
import org.bukkit.Location;
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
        this(name, location, null, enabled, persistent);
    }

    /**
     * Creates a new instance of {@link DefaultHologram} with the given name.
     *
     * @param name       The name of the hologram.
     * @param location   The location of the hologram.
     * @param config     The config of the hologram.
     * @param enabled    Whether the hologram is enabled or not.
     * @param persistent Whether the hologram is persistent.
     */
    protected DefaultHologram(@NotNull String name, @NotNull Location location, Section config, boolean enabled, boolean persistent) {
        this.name = name;
        if (config != null) {
            this.file = new DefaultHologramConfig(this, config);
        } else {
            this.file = new DefaultHologramConfig(this);
        }
        this.positionManager = new DefaultPositionManager(location);
        this.settings = new DefaultHologramSettings(false, persistent);
        this.visibilityManager = new DefaultHologramVisibilityManager(this);
        this.pageHolder = new DefaultHologramPageHolder(this);
        this.conditionHolder = new DefaultConditionHolder();
        this.lastTick = new AtomicLong(0);

        // Start the ticking.
        this.startTicking();

        // Load the hologram from the file.
        this.getConfig().load()
                .thenRun(() -> getSettings().setEnabled(enabled));
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
            S.async(() -> pageHolder.getPages().forEach(Page::recalculate));
        }

        // Update the visibility of the hologram if the time difference is greater than 500ms.
        if (500L < timeDifference) {
            S.async(visibilityManager::updateVisibility);
        }

        // Update the content of the hologram.
        if (settings.getUpdateInterval() * 50L < timeDifference) {
            S.async(visibilityManager::updateContents);
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
    public ConditionHolder getViewConditionHolder() {
        return conditionHolder;
    }

}

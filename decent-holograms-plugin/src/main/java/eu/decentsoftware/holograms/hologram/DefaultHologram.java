/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.hologram;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.HologramConfig;
import eu.decentsoftware.holograms.api.hologram.HologramSettings;
import eu.decentsoftware.holograms.api.hologram.HologramVisibilityManager;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.hologram.component.DefaultPositionManager;
import eu.decentsoftware.holograms.hologram.page.DefaultHologramPage;
import eu.decentsoftware.holograms.ticker.Ticked;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class DefaultHologram implements Hologram, Ticked {

    private final String name;
    private final HologramConfig config;
    private final HologramSettings settings;
    private final DefaultPositionManager positionManager;
    private final HologramVisibilityManager visibilityManager;
    private final List<HologramPage> pages = new ArrayList<>();
    private final ConditionHolder viewConditions;
    private final DefaultHologramEntityIDProvider entityIDProvider = new DefaultHologramEntityIDProvider(DecentHolograms.getInstance().getNMSManager().getAdapter());

    @Getter(AccessLevel.NONE)
    private final AtomicLong lastVisibilityUpdate = new AtomicLong(0);
    @Getter(AccessLevel.NONE)
    private final AtomicLong lastContentUpdate = new AtomicLong(0);

    /**
     * Creates a new instance of {@link DefaultHologram} with the given name.
     *
     * @param name     The name of the hologram.
     * @param location The location of the hologram.
     */
    public DefaultHologram(@NonNull String name, @NonNull Location location) {
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
    public DefaultHologram(@NonNull String name, @NonNull Location location, boolean enabled, boolean persistent) {
        this.name = name;
        this.config = new DefaultHologramConfig(this);
        this.positionManager = new DefaultPositionManager(location);
        this.settings = new DefaultHologramSettings(false, persistent);
        this.visibilityManager = new DefaultHologramVisibilityManager(this);
        this.viewConditions = new ConditionHolder();
        this.addPage(); // We always need at least one page.

        // Start the ticking.
        this.startTicking();

        // Load the hologram from the file.
        this.getConfig().reload().thenRun(() -> getSettings().setEnabled(enabled));
    }

    public DefaultHologram(@NonNull String name, @NonNull Location location, @NonNull HologramSettings settings,
                           @NonNull ConditionHolder viewConditions) {
        this.name = name;
        this.config = new DefaultHologramConfig(this);
        this.positionManager = new DefaultPositionManager(location);
        this.settings = settings;
        this.visibilityManager = new DefaultHologramVisibilityManager(this);
        this.viewConditions = viewConditions;
        this.addPage(); // We always need at least one page.

        // Start the ticking.
        this.startTicking();
    }

    public DefaultHologram copy(final @NonNull String newName) {
        // TODO: implement copying
        return null;
    }

    @Override
    public void tick() {
        if (!settings.isEnabled()) {
            // Do not tick if the hologram is disabled.
            return;
        }

        long currentTime = System.currentTimeMillis();

        // If the location is bound, update the location.
        if (positionManager.isLocationBound()
                || settings.isRotateHorizontal()
                || settings.isRotateVertical()
                || settings.isRotateHeads()
        ) {
            recalculate();
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
        this.getVisibilityManager().destroy();
    }

    @Override
    public void delete() {
        this.destroy();
        this.getConfig().delete();
    }

    @Override
    public void recalculate() {
        pages.forEach(HologramPage::recalculate);
    }

    @Override
    public HologramPage getPage(int index) {
        return pages.get(index);
    }

    @Override
    public int getIndex(final @NonNull HologramPage page) {
        return pages.contains(page) ? pages.indexOf(page) : -1;
    }

    @NonNull
    @Override
    public HologramPage addPage() {
        DefaultHologramPage page = new DefaultHologramPage(this, entityIDProvider);
        pages.add(page);
        return page;
    }

    @NonNull
    @Override
    public HologramPage addPage(int index) {
        DefaultHologramPage page = new DefaultHologramPage(this, entityIDProvider);
        pages.add(index, page);

        // Shift the player page indexes in visibility manager.
        shiftPlayerPages(index, 1);

        return page;
    }

    @NonNull
    @Override
    public Hologram removePage(int index) {
        pages.remove(index);

        // Shift the player page indexes in visibility manager.
        shiftPlayerPages(index, -1);

        return this;
    }

    @NonNull
    @Override
    public Hologram clearPages() {
        pages.clear();

        // Reset the player page indexes in visibility manager to 0.
        visibilityManager.getPlayerPages().replaceAll((k, v) -> 0);

        return this;
    }

    @NonNull
    @Override
    public List<HologramPage> getPages() {
        return ImmutableList.copyOf(pages);
    }

    public void setPages(List<HologramPage> pages) {
        this.pages.clear();
        this.pages.addAll(pages);
    }

    /**
     * Shift the player page indexes in visibility manager by the given amount at the given index.
     *
     * @param index The index to start shifting from.
     * @param shift The amount to shift by.
     */
    private void shiftPlayerPages(int index, int shift) {
        for (Map.Entry<UUID, Integer> entry : visibilityManager.getPlayerPages().entrySet()) {
            if (entry.getValue() >= index) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null) {
                    visibilityManager.setPage(player, entry.getValue() + shift);
                }
            }
        }
    }

}

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

package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramPageHolder;
import eu.decentsoftware.holograms.api.component.hologram.HologramVisibilityManager;
import eu.decentsoftware.holograms.api.component.page.Page;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultHologramPageHolder implements HologramPageHolder {

    private final @NotNull Hologram parent;
    private final @NotNull List<Page> pages;

    /**
     * Creates a new instance of {@link DefaultHologramPageHolder} with the given parent.
     *
     * @param parent The parent hologram.
     */
    public DefaultHologramPageHolder(@NotNull Hologram parent) {
        this.parent = parent;
        this.pages = new ArrayList<>();
    }

    @NotNull
    @Override
    public Hologram getParent() {
        return parent;
    }

    @NotNull
    @Override
    public List<Page> getPages() {
        return pages;
    }

    @Override
    public void addPage(int index, @NotNull Page page) {
        HologramPageHolder.super.addPage(index, page);

        // Shift the player page indexes in visibility manager.
        shiftPlayerPages(index, 1);
    }

    @Override
    public void removePage(int index) {
        HologramPageHolder.super.removePage(index);

        // Shift the player page indexes in visibility manager.
        shiftPlayerPages(index, -1);
    }

    @Override
    public void clearPages() {
        HologramPageHolder.super.clearPages();

        // Reset the player page indexes in visibility manager to 0.
        getParent().getVisibilityManager().getPlayerPages().replaceAll((k, v) -> 0);
    }

    /**
     * Shift the player page indexes in visibility manager by the given amount at the given index.
     *
     * @param index The index to start shifting from.
     * @param shift The amount to shift by.
     */
    private void shiftPlayerPages(int index, int shift) {
        HologramVisibilityManager visibilityManager = getParent().getVisibilityManager();
        for (Map.Entry<String, Integer> entry : visibilityManager.getPlayerPages().entrySet()) {
            if (entry.getValue() >= index) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null) {
                    visibilityManager.setPage(player, entry.getValue() + shift);
                }
            }
        }
    }

}

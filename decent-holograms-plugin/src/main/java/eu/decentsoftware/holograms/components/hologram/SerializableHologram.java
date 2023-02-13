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

import eu.decentsoftware.holograms.api.component.page.HologramPage;
import eu.decentsoftware.holograms.components.page.DefaultHologramPage;
import eu.decentsoftware.holograms.components.page.SerializablePage;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to (de)serialize holograms from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public class SerializableHologram {

    private final Location location;
    private DefaultHologramSettings settings;
    private ConditionHolder viewConditions;
    private final @NotNull List<SerializablePage> pages;

    /**
     * Create a new instance of {@link SerializableHologram} from the given {@link DefaultHologram}.
     *
     * @param hologram The hologram.
     * @return The new {@link SerializableHologram}.
     */
    @Contract("_ -> new")
    @NotNull
    public static SerializableHologram fromHologram(@NotNull DefaultHologram hologram) {
        List<SerializablePage> pages = new ArrayList<>();
        for (HologramPage page : hologram.getPages()) {
            DefaultHologramPage defaultPage = (DefaultHologramPage) page;
            SerializablePage serializablePage = SerializablePage.fromPage(defaultPage);
            pages.add(serializablePage);
        }
        return new SerializableHologram(
                hologram.getPositionManager().getLocation(),
                (DefaultHologramSettings) hologram.getSettings(),
                hologram.getViewConditions(),
                pages
        );
    }

    /**
     * Create a {@link DefaultHologram} from this {@link SerializableHologram}.
     *
     * @return The new {@link DefaultHologram}.
     */
    @NotNull
    public DefaultHologram toHologram(@NotNull String name) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null.");
        }
        if (settings == null) {
            settings = new DefaultHologramSettings(true, true);
        }
        if (viewConditions == null) {
            viewConditions = new ConditionHolder();
        }
        DefaultHologram hologram = new DefaultHologram(name, location, settings, viewConditions);
        List<HologramPage> pages = new ArrayList<>();
        for (SerializablePage page : this.pages) {
            DefaultHologramPage defaultPage = page.toPage(hologram);
            pages.add(defaultPage);
        }
        hologram.setPages(pages);
        return hologram;
    }

}

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

package eu.decentsoftware.holograms.hologram.serialization;

import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;
import eu.decentsoftware.holograms.hologram.DefaultHologram;
import eu.decentsoftware.holograms.hologram.line.DefaultHologramLine;
import eu.decentsoftware.holograms.hologram.page.DefaultHologramPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to (de)serialize pages from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public class SerializablePage {

    private final @NotNull List<SerializableLine> lines;
    private ClickConditionHolder clickConditions;
    private ClickActionHolder clickActions;

    /**
     * Create a new {@link SerializablePage} from the given {@link DefaultHologramPage}.
     *
     * @param page The page.
     * @return The new {@link SerializablePage}.
     */
    @NotNull
    public static SerializablePage fromPage(@NotNull DefaultHologramPage page) {
        List<SerializableLine> lines = new ArrayList<>();
        for (HologramLine line : page.getLines()) {
            DefaultHologramLine defaultLine = (DefaultHologramLine) line;
            SerializableLine serializableLine = SerializableLine.fromLine(defaultLine);
            lines.add(serializableLine);
        }
        return new SerializablePage(
                lines,
                page.getClickConditions(),
                page.getClickActions()
        );
    }

    /**
     * Create a {@link DefaultHologramPage} from this {@link SerializablePage}.
     *
     * @param hologram The parent {@link DefaultHologram} of this page.
     * @return The new {@link DefaultHologramPage}.
     */
    @NotNull
    public DefaultHologramPage toPage(@NotNull DefaultHologram hologram) {
        if (clickConditions == null) {
            clickConditions = new ClickConditionHolder();
        }
        if (clickActions == null) {
            clickActions = new ClickActionHolder();
        }
        DefaultHologramPage page = new DefaultHologramPage(
                hologram,
                hologram.getEntityIDProvider(), clickConditions,
                clickActions
        );
        for (SerializableLine line : this.lines) {
            DefaultHologramLine defaultLine = line.toLine(page);
            page.addLine(defaultLine);
        }
        return page;
    }

}

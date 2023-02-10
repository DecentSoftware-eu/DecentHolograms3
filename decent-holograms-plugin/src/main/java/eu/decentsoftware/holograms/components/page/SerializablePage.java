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

package eu.decentsoftware.holograms.components.page;

import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.api.component.line.HologramLine;
import eu.decentsoftware.holograms.components.hologram.DefaultHologram;
import eu.decentsoftware.holograms.components.line.DefaultHologramLine;
import eu.decentsoftware.holograms.components.line.SerializableLine;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to (de)serialize pages from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Data
public class SerializablePage {

    private final @NotNull List<SerializableLine> lines;
    private final @NotNull ConditionHolder clickConditions;
    private final @NotNull ActionHolder clickActions;

    /**
     * Create a new {@link SerializablePage} from the given {@link DefaultPage}.
     *
     * @param page The page.
     * @return The new {@link SerializablePage}.
     */
    @NotNull
    public static SerializablePage fromPage(@NotNull DefaultPage page) {
        List<SerializableLine> lines = new ArrayList<>();
        for (HologramLine line : page.getLineHolder().getLines()) {
            DefaultHologramLine defaultLine = (DefaultHologramLine) line;
            SerializableLine serializableLine = SerializableLine.fromLine(defaultLine);
            lines.add(serializableLine);
        }
        return new SerializablePage(
                lines,
                page.getClickConditionHolder(),
                page.getClickActionHolder()
        );
    }

    /**
     * Create a {@link DefaultPage} from this {@link SerializablePage}.
     *
     * @param hologram The parent {@link DefaultHologram} of this page.
     * @return The new {@link DefaultPage}.
     */
    @NotNull
    public DefaultPage toPage(@NotNull DefaultHologram hologram) {
        DefaultPage page = new DefaultPage(hologram, clickConditions, clickActions);
        List<HologramLine> lines = new ArrayList<>();
        for (SerializableLine line : this.lines) {
            DefaultHologramLine defaultLine = line.toLine(page);
            lines.add(defaultLine);
        }
        page.getLineHolder().setLines(lines);
        return page;
    }

}

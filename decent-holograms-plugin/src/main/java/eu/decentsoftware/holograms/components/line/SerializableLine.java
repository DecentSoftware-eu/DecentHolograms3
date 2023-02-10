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

package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.components.page.DefaultHologramPage;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to (de)serialize lines from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public class SerializableLine {

    private DefaultHologramLineSettings settings;
    private ConditionHolder viewConditions;
    private ConditionHolder clickConditions;
    private ActionHolder clickActions;
    private final String content;

    /**
     * Create a new {@link SerializableLine} from the given {@link DefaultHologramLine}.
     *
     * @param line The line.
     * @return The new {@link SerializableLine}.
     */
    @NotNull
    public static SerializableLine fromLine(@NotNull DefaultHologramLine line) {
        return new SerializableLine(
                (DefaultHologramLineSettings) line.getSettings(),
                line.getViewConditionHolder(),
                line.getClickConditionHolder(),
                line.getClickActionHolder(),
                line.getContent()
        );
    }

    /**
     * Create a {@link DefaultHologramLine} from this {@link SerializableLine}.
     *
     * @param page The parent {@link DefaultHologramPage} of this line.
     * @return The new {@link DefaultHologramLine}.
     */
    @NotNull
    public DefaultHologramLine toLine(@NotNull DefaultHologramPage page) {
        if (settings == null) {
            settings = new DefaultHologramLineSettings();
        }
        if (viewConditions == null) {
            viewConditions = new ConditionHolder();
        }
        if (clickConditions == null) {
            clickConditions = new ConditionHolder();
        }
        if (clickActions == null) {
            clickActions = new ActionHolder();
        }
        return new DefaultHologramLine(page, settings, page.getNextLineLocation(), viewConditions, clickConditions, clickActions, content);
    }

}

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

package eu.decentsoftware.holograms.internal;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.core.CoreHologramSettings;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PluginHologramSettings extends CoreHologramSettings {

    private ActionExecutionStrategy actionExecutionStrategy = Config.DEFAULTS_ACTION_EXECUTION_STRATEGY;

    public PluginHologramSettings(boolean enabled) {
        super(enabled);
    }

    public void set(@NonNull CoreHologramSettings otherSettings) {
        super.set(otherSettings);
        if (otherSettings instanceof PluginHologramSettings) {
            this.actionExecutionStrategy = ((PluginHologramSettings) otherSettings).actionExecutionStrategy;
        }
    }

}

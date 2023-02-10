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

package eu.decentsoftware.holograms.ticker;

import eu.decentsoftware.holograms.DecentHologramsPlugin;

/**
 * This interface represents a ticked object.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface Ticked {

    /**
     * Tick the object. This method is called every tick.
     */
    void tick();

    /**
     * Register the object to the ticker.
     */
    default void startTicking() {
        DecentHologramsPlugin.getInstance().getTicker().register(this);
    }

    /**
     * Unregister the object from the ticker.
     */
    default void stopTicking() {
        DecentHologramsPlugin.getInstance().getTicker().unregister(this);
    }

}
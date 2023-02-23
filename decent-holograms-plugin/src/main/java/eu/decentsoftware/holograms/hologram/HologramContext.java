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

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;

import java.util.UUID;

@Getter
@Setter
public class HologramContext {

    private UUID mover;
    private int moverDistance;

    @Contract(pure = true)
    public HologramContext() {
        this.mover = null;
        this.moverDistance = 0;
    }

}

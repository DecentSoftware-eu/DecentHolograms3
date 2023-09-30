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

package eu.decentsoftware.holograms.utils.location;

import lombok.*;

/**
 * This class represents a 3D position. It holds three double values for the x, y and z axis.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Position3D {

    private double x;
    private double y;
    private double z;

}

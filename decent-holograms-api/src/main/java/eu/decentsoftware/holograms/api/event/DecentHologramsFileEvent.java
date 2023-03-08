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

package eu.decentsoftware.holograms.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * Event that is fired when a file inside watched folder changes or is created/deleted.
 *
 * @author JesusChrist69
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public class DecentHologramsFileEvent extends DecentHologramsEvent {

    private final File file;
    private final FileAction fileAction;

    public enum FileAction {
        CREATE,
        EDIT,
        DELETE
    }

}

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

package eu.decentsoftware.holograms.editor;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.editor.move.MoveController;
import eu.decentsoftware.holograms.editor.scroll.ScrollListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

@Getter
public class Editor {

    private final MoveController moveController;
    private final ScrollListener scrollListener;

    public Editor(DecentHolograms plugin) {
        this.moveController = new MoveController(plugin);
        this.scrollListener = new ScrollListener();

        Bukkit.getPluginManager().registerEvents(scrollListener, plugin);
    }

    public void reload() {

    }

    public void shutdown() {
        this.moveController.shutdown();

        HandlerList.unregisterAll(scrollListener);
    }

}

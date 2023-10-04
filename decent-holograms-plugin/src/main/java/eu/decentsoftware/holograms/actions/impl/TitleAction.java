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

package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.actions.Action;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitleAction extends Action {

    protected final String title;
    protected final String subtitle;
    protected final int fadeIn;
    protected final int stay;
    protected final int fadeOut;

    public TitleAction(@NonNull DecentHolograms plugin, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        super(plugin);
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public TitleAction(@NonNull DecentHolograms plugin, long delay, double chance, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        super(plugin, delay, chance);
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void execute(@NotNull Player player) {
        NMSAdapter nmsAdapter = plugin.getNMSManager().getAdapter();
        nmsAdapter.resetTitle(player);
        nmsAdapter.sendTitle(player, title, subtitle, Math.max(fadeIn, 1), Math.max(stay, 1), Math.max(fadeOut, 1));
    }

}

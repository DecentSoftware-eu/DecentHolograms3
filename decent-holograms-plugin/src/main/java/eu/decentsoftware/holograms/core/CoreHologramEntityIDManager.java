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

package eu.decentsoftware.holograms.core;

import eu.decentsoftware.holograms.nms.NMSAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for providing entity ids for individual lines and
 * line entities in a hologram.
 * <p>
 * Entity IDs provided by this class are shared between all pages of a hologram.
 * This means that if you have a hologram with 3 lines and 2 pages, the entity
 * ids for the first line on the first page and the first line on the second page
 * will be the same. This makes it possible to use the same entity for the same
 * line on different pages and prevent flickering caused by entity removal and
 * creation.
 *
 * @author d0by
 * @since 3.0.0
 */
public class CoreHologramEntityIDManager {

    private final NMSAdapter nmsAdapter;
    private final List<List<Integer>> perLineEntityIds = new ArrayList<>();

    public CoreHologramEntityIDManager(NMSAdapter nmsAdapter) {
        this.nmsAdapter = nmsAdapter;
    }

    /**
     * Returns the entity id for the line entity at the given index on the given
     * line.
     * <p>
     * Each line can have multiple entities. For example, an item line has 2
     * entities - the item and the armor stand. This method returns the entity id
     * for the entity at the given index.
     *
     * @param lineIndex       The index of the line.
     * @param lineEntityIndex The index of the line entity.
     * @return The entity id.
     */
    public int getEntityId(int lineIndex, int lineEntityIndex) {
        if (perLineEntityIds.size() <= lineIndex) {
            perLineEntityIds.add(new ArrayList<>());
        }

        List<Integer> ids = perLineEntityIds.get(lineIndex);
        if (ids.size() <= lineEntityIndex) {
            ids.add(nmsAdapter.getFreeEntityId());
        }

        return ids.get(lineEntityIndex);
    }

}

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

package eu.decentsoftware.holograms.hologram.line.renderer;

import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineType;
import eu.decentsoftware.holograms.nms.utils.Version;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.hooks.MiniMessageHook;
import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TextLineRenderer extends LineRenderer {

    private final @NotNull String text;
    private final String hoverText;
    private final int eid;

    public TextLineRenderer(@NotNull HologramLine parent, @NotNull String text) {
        this(parent, text, null);
    }

    public TextLineRenderer(@NotNull HologramLine parent, @NotNull String text, String hoverText) {
        super(parent, HologramLineType.TEXT);
        this.text = text;
        this.hoverText = hoverText;
        this.eid = NMS.getFreeEntityId();
    }

    /**
     * Get the formatted text of the line for the given player.
     *
     * @param player The player to get the text for.
     * @return The formatted text of the line.
     */
    @NotNull
    private String getFormattedText(@NotNull Player player) {
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player.getUniqueId());
        String formattedText = text;

        if (hoverText != null) {
            // Check if the player in watching the line and if so, use the hover text.
            if (profile != null && getParent().equals(profile.getContext().getWatchedLine())) {
                formattedText = hoverText;
            }
        }

        // Replace custom replacements
        formattedText = PLUGIN.getReplacementRegistry().replace(formattedText, profile);
        // Replace PAPI placeholders
        formattedText = PAPI.setPlaceholders(player, formattedText);
        // Colorize
        formattedText = Common.colorize(formattedText);

        return formattedText;
    }

    @Override
    public void display(@NotNull Player player) {
        Location loc = getParent().getPositionManager().getActualLocation();
        String formattedText = getFormattedText(player);

        // Create the metadata objects
        Object metaEntity = NMS.getMetaEntityProperties(false, false, false,
                false, true, false, false);
        Object metaArmorStand = NMS.getMetaArmorStandProperties(false, false, true,
                true);
        Object metaName;
        if (Version.is(8)) {
            metaName = NMS.getMetaEntityCustomName(MiniMessageHook.serializeMinecraftLegacy(formattedText));
        } else {
            metaName = NMS.getMetaEntityCustomName(MiniMessageHook.serializeMinecraft(formattedText));
        }
        Object metaNameVisible = NMS.getMetaEntityCustomNameVisible(!formattedText.isEmpty());

        // Spawn the fake armor stand entity
        NMS.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, loc);
        // Send the metadata
        NMS.sendEntityMetadata(player, eid, metaEntity, metaArmorStand, metaName, metaNameVisible);
    }

    @Override
    public void update(@NotNull Player player) {
        String formattedText = getFormattedText(player);

        // Create the metadata objects
        Object metaName;
        if (Version.is(8)) {
            metaName = NMS.getMetaEntityCustomName(MiniMessageHook.serializeMinecraftLegacy(formattedText));
        } else {
            metaName = NMS.getMetaEntityCustomName(MiniMessageHook.serializeMinecraft(formattedText));
        }
        Object metaNameVisible = NMS.getMetaEntityCustomNameVisible(!formattedText.isEmpty());

        // Send the metadata
        NMS.sendEntityMetadata(player, eid, metaName, metaNameVisible);
    }

    @Override
    public void hide(@NotNull Player player) {
        // Destroy the fake armor stand entity
        NMS.removeEntity(player, eid);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        // Teleport the fake armor stand entity
        NMS.teleportEntity(player, eid, location, false);
    }

}

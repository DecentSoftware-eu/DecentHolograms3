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

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.api.hologram.line.HologramLineType;
import eu.decentsoftware.holograms.hooks.MiniMessageHook;
import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.nms.utils.Version;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.ticker.Ticked;
import eu.decentsoftware.holograms.utils.Common;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TextLineRenderer extends LineRenderer implements Ticked {

    /**
     * The cache of formatted text for each player. This formatted text already
     * has all placeholders and replacements replaced. It is not colored, and it
     * is not animated.
     *
     * @implNote This is only used if the line contains any animations. If it does not,
     * then this cache is not used.
     */
    private final Map<UUID, String> formattedTextCache = new ConcurrentHashMap<>();
    private final int eid;
    private boolean containsAnimations;
    private final String hoverText; // TODO: Hover text
    @Getter
    private String text;

    public TextLineRenderer(
            @NotNull DecentHolograms plugin,
            @NotNull String text,
            @NotNull HologramLine parent
    ) {
        this(plugin, text, null, parent);
    }

    public TextLineRenderer(
            @NotNull DecentHolograms plugin,
            @NotNull String text,
            String hoverText,
            @NotNull HologramLine parent
    ) {
        super(plugin, parent, HologramLineType.TEXT);
        this.hoverText = hoverText;
        this.eid = nmsAdapter.getFreeEntityId();
        this.setText(text);
    }

    /**
     * Set the text of this line. This method will also check if the line contains
     * any animations, and if it does, it will start ticking.
     *
     * @param text The new text of the line.
     */
    public void setText(@NotNull String text) {
        this.text = text;
        if (this.text.equalsIgnoreCase("{empty}")) {
            this.text = "";
        }
        this.containsAnimations = plugin.getAnimationRegistry().containsAnimation(text);
        if (this.containsAnimations) {
            this.startTicking();
        } else {
            this.stopTicking();
        }
    }

    @Override
    public void tick() {
        if (!containsAnimations) {
            return;
        }

        for (Player viewerPlayer : getViewerPlayers()) {
            String formattedText = formattedTextCache.get(viewerPlayer.getUniqueId());
            if (formattedText == null) {
                formattedText = getFormattedText(viewerPlayer);
                formattedTextCache.put(viewerPlayer.getUniqueId(), formattedText);
            }
            formattedText = plugin.getAnimationRegistry().animate(formattedText);
            formattedText = Common.colorize(formattedText);
            update(viewerPlayer, formattedText);
        }
    }

    /**
     * Get the formatted text of the line for the given player.
     *
     * @param player The player to get the text for.
     * @return The formatted text of the line.
     */
    @NotNull
    private String getFormattedText(@NotNull Player player) {
        Profile profile = plugin.getProfileRegistry().getProfile(player.getUniqueId());
        String formattedText = text;

        // Check if the player in watching the line and if so, use the hover text.
        if (hoverText != null && profile != null && getParent().equals(profile.getContext().getWatchedLine())) {
            formattedText = hoverText;
        }

        formattedText = plugin.getReplacementRegistry().replace(formattedText, profile);
        formattedText = PAPI.setPlaceholders(player, formattedText);

        if (containsAnimations) {
            formattedTextCache.put(player.getUniqueId(), formattedText);
            formattedText = plugin.getAnimationRegistry().animate(formattedText);
        }
        formattedText = Common.colorize(formattedText);

        return formattedText;
    }

    @Override
    public void display(@NotNull Player player) {
        Location loc = getParent().getPositionManager().getActualLocation();
        String formattedText = getFormattedText(player);

        // Create the metadata objects
        Object metaEntity = nmsAdapter.getMetaEntityProperties(false, false, false,
                false, true, false, false);
        Object metaArmorStand = nmsAdapter.getMetaArmorStandProperties(false, false, true,
                true);
        Object metaName = getMetaName(formattedText);
        Object metaNameVisible = nmsAdapter.getMetaEntityCustomNameVisible(!formattedText.isEmpty());

        // Spawn the fake armor stand entity
        nmsAdapter.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, loc);
        // Send the metadata
        nmsAdapter.sendEntityMetadata(player, eid, metaEntity, metaArmorStand, metaName, metaNameVisible);
    }

    @Override
    public void update(@NotNull Player player) {
        String formattedText = getFormattedText(player);
        update(player, formattedText);
    }

    private void update(@NotNull Player player, @NotNull String text) {
        // Create the metadata objects
        Object metaName = getMetaName(text);
        boolean isNameInvisible = text.isEmpty() || text.replaceAll("§.", "").isEmpty();
        Object metaNameVisible = nmsAdapter.getMetaEntityCustomNameVisible(!isNameInvisible);

        // Send the metadata
        nmsAdapter.sendEntityMetadata(player, eid, metaName, metaNameVisible);
    }

    @Override
    public void hide(@NotNull Player player) {
        // Destroy the fake armor stand entity
        nmsAdapter.removeEntity(player, eid);

        // Remove the cached text
        formattedTextCache.remove(player.getUniqueId());
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        // Teleport the fake armor stand entity
        nmsAdapter.teleportEntity(player, eid, location, true);
    }

    private Object getMetaName(@NotNull String formattedText) {
        if (Version.is(8)) {
            return nmsAdapter.getMetaEntityCustomName(MiniMessageHook.serializeToString(formattedText, true));
        } else {
            return nmsAdapter.getMetaEntityCustomName(MiniMessageHook.serializeToIChatBaseComponent(formattedText, Version.before(16)));
        }
    }

}

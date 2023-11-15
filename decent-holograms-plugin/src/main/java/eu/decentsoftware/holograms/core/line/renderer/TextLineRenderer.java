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

package eu.decentsoftware.holograms.core.line.renderer;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.HologramLineType;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.hooks.MiniMessageHook;
import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.nms.utils.Version;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.ticker.Ticked;
import eu.decentsoftware.holograms.utils.Common;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
    private boolean containsAnimations;
    private String text;

    public TextLineRenderer(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramLine parent,
            @NonNull String text
    ) {
        super(plugin, parent, HologramLineType.TEXT);
        this.setText(text);
        this.startTicking();
    }

    @Override
    public void destroy() {
        this.stopTicking();
        super.destroy();
    }

    /**
     * Set the text of this line. This method will also check if the line contains
     * any animations, and if it does, it will start ticking.
     *
     * @param text The new text of the line.
     */
    public void setText(@NonNull String text) {
        this.text = text;
        if (this.text.equalsIgnoreCase("{empty}")) {
            this.text = "";
        }
        this.containsAnimations = this.plugin.getAnimationRegistry().containsAnimation(text);
    }

    @Override
    public double getHeight() {
        return 0.25d;
    }

    @Override
    public double getWidth(@NonNull Player player) {
        return Math.min(5d, Common.getTextWidth(getFormattedText(player)) / 100d);
    }

    @Override
    public void tick() {
        if (!this.containsAnimations) {
            return;
        }

        for (Player viewerPlayer : getViewerPlayers()) {
            String formattedText = this.formattedTextCache.get(viewerPlayer.getUniqueId());
            if (formattedText == null) {
                formattedText = getFormattedText(viewerPlayer);
                this.formattedTextCache.put(viewerPlayer.getUniqueId(), formattedText);
            }
            formattedText = this.plugin.getAnimationRegistry().animate(formattedText);
            formattedText = Common.colorize(formattedText);
            updateContent(viewerPlayer, formattedText);
        }
    }

    /**
     * Get the formatted text of the line for the given player.
     *
     * @param player The player to get the text for.
     * @return The formatted text of the line.
     */
    @NonNull
    private String getFormattedText(@NonNull Player player) {
        Profile profile = this.plugin.getProfileRegistry().getProfile(player.getUniqueId());
        String formattedText = this.text;
        formattedText = this.plugin.getReplacementRegistry().replace(formattedText, profile);
        formattedText = PAPI.setPlaceholders(player, formattedText);
        if (this.containsAnimations) {
            this.formattedTextCache.put(player.getUniqueId(), formattedText);
            formattedText = this.plugin.getAnimationRegistry().animate(formattedText);
        }
        formattedText = Common.colorize(formattedText);

        return formattedText;
    }

    @Override
    public void display(@NonNull Player player) {
        String formattedText = getFormattedText(player);

        Object metaEntity = this.nmsAdapter.getMetaEntityProperties(
                false,
                false,
                false,
                false,
                true,
                false,
                false
        );
        Object metaArmorStand = this.nmsAdapter.getMetaArmorStandProperties(
                false,
                false,
                true,
                true
        );
        Object metaName = getMetaName(formattedText);
        Object metaNameVisible = this.nmsAdapter.getMetaEntityCustomNameVisible(!formattedText.isEmpty());

        this.nmsAdapter.spawnEntityLiving(player, getEntityId(0), UUID.randomUUID(), EntityType.ARMOR_STAND, this.parent.getActualBukkitLocation());
        this.nmsAdapter.sendEntityMetadata(player, getEntityId(0), metaEntity, metaArmorStand, metaName, metaNameVisible);
    }

    @Override
    public void hide(@NonNull Player player) {
        this.nmsAdapter.removeEntity(player, getEntityId(0));

        this.formattedTextCache.remove(player.getUniqueId());
    }

    @Override
    public void updateContent(@NonNull Player player) {
        String formattedText = getFormattedText(player);
        updateContent(player, formattedText);
    }

    private void updateContent(@NonNull Player player, @NonNull String text) {
        Object metaName = getMetaName(text);
        boolean isNameInvisible = text.isEmpty() || text.replaceAll("§.", "").isEmpty();
        Object metaNameVisible = this.nmsAdapter.getMetaEntityCustomNameVisible(!isNameInvisible);

        this.nmsAdapter.sendEntityMetadata(player, getEntityId(0), metaName, metaNameVisible);
    }

    @Override
    public void updateLocation(@NonNull Player player, @NonNull Location location) {
        this.nmsAdapter.teleportEntity(player, getEntityId(0), location, true);
    }

    private Object getMetaName(@NonNull String formattedText) {
        if (Version.is(8)) {
            return this.nmsAdapter.getMetaEntityCustomName(MiniMessageHook.serializeToString(formattedText, true));
        } else {
            return this.nmsAdapter.getMetaEntityCustomName(MiniMessageHook.serializeToIChatBaseComponent(formattedText, Version.before(16)));
        }
    }

}

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

package eu.decentsoftware.holograms;

import com.cryptomorin.xseries.XSound;
import eu.decentsoftware.holograms.editor.scroll.DecentPlayerScrollEvent;
import eu.decentsoftware.holograms.editor.scroll.ScrollDirection;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This listener sends update notifications to players with the permission
 * {@link Config#ADMIN_PERM} when an update is available.
 *
 * @author d0by
 * @since 3.0.0
 */
class UpdateNotificationListener implements Listener {

    @EventHandler
    public void onJoin(@NonNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (Config.isUpdateAvailable() && player.hasPermission(Config.ADMIN_PERM)) {
            Lang.sendUpdateMessage(player);
        }
    }

    // TODO TESTING ZONE

    private final Map<String, Value> playerValueMap = new ConcurrentHashMap<>();

    @EventHandler
    public void onScroll(DecentPlayerScrollEvent event) {
        Player player = event.getPlayer();
        ScrollDirection direction = event.getScrollDirection();
        Value currentValue = playerValueMap.computeIfAbsent(player.getName(), s -> Value.ADD_LINE);
        Value newValue = direction == ScrollDirection.UP ? currentValue.next() : currentValue.previous();
        playerValueMap.put(player.getName(), newValue);

        DecentHolograms.getInstance().getNMSManager().getAdapter().sendTitle(
                player,
                "",
                Lang.formatString(String.format(
                        "&7%s&8 ∙ &a&l%s&8 ∙ &7%s",
                        newValue.previous().getDisplayName(),
                        newValue.getDisplayName(),
                        newValue.next().getDisplayName()
                )),
                0,
                200,
                0
        );

        XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player, 1, 1);
    }

    private enum Value {
        ADD_LINE("Add Line"),
        INSERT_LINE("Insert Line"),
        EDIT_LINE("Edit Line"),
        REMOVE_LINE("Remove Line"),
        MOVE_LINE("Move Line"),
        ;

        @Getter
        private final String displayName;

        Value(String displayName) {
            this.displayName = displayName;
        }

        public Value next() {
            return values()[(ordinal() + 1) % values().length];
        }

        public Value previous() {
            return values()[(ordinal() - 1 + values().length) % values().length];
        }

    }

}

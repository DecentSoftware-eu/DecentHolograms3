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

package eu.decentsoftware.addons.holograms;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.addons.DecentHologramsAddon;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Random;

/**
 * Damage Display addon for displaying damage taken & health regain holograms
 *
 * @author JesusChrist69, d0by
 * @since 3.0.0
 */
public class DamageDisplayAddon implements DecentHologramsAddon, Listener {

    private static final DecentHologramsAPI API = DecentHologramsAPI.getInstance();
    private static final Random RANDOM = new Random();
    private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

    private int durationDamage;
    private String appearanceDamage;
    private String criticalAppearanceDamage;
    private boolean displayForPlayersDamage;
    private boolean displayForMobsDamage;
    private boolean zeroDamageDamage;
    private double heightOffsetDamage;
    private boolean enabledDamage;

    private int durationHeal;
    private String appearanceHeal;
    private boolean displayForPlayersHeal;
    private boolean displayForMobsHeal;
    private double heightOffsetHeal;
    private boolean enabledHeal;

    @Override
    public void onLoad() {
        registerListener(this);

        FileConfiguration conf = loadFile(getPlugin().getDataFolder().getAbsolutePath() + "/addons/", "damageDisplayConfig.yml");
        enabledDamage = conf.getBoolean("damage-display.enabled", true);
        durationDamage = conf.getInt("damage-display.duration", 40);
        appearanceDamage = conf.getString("damage-display.appearance", "&c{damage}");
        criticalAppearanceDamage = conf.getString("damage-display.crit-appearance", "&4&lCrit!&4 {damage}");
        heightOffsetDamage = conf.getDouble("damage-display.height-offset", 0.0);
        displayForPlayersDamage = conf.getBoolean("damage-display.players", true);
        displayForMobsDamage = conf.getBoolean("damage-display.mobs", true);
        zeroDamageDamage = conf.getBoolean("damage-display.show-zero-damage", true);

        enabledHeal = conf.getBoolean("healing-display.enabled", true);
        durationHeal = conf.getInt("healing-display.duration", 40);
        appearanceHeal = conf.getString("healing-display.appearance", "&a+ {heal}");
        heightOffsetHeal = conf.getDouble("healing-display.height-offset", 0.0);
        displayForPlayersHeal = conf.getBoolean("healing-display.players", true);
        displayForMobsHeal = conf.getBoolean("healing-display.mobs", true);
    }

    @Override
    public void onReload() {
        onUnload();
        onLoad();
    }

    @Override
    public void onUnload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegain(EntityRegainHealthEvent e) {
        if (!enabledHeal || e.isCancelled()) return;

        double heal = e.getAmount();
        if (heal <= 0) return;

        Entity entity = e.getEntity();
        if (!(entity instanceof LivingEntity) || entity instanceof ArmorStand) return;
        if (entity instanceof Player && !displayForPlayersHeal) return;
        if (!(entity instanceof Player) && !displayForMobsHeal) return;

        Location location = randomizeLocation(entity.getLocation().clone().add(0, 1 + heightOffsetHeal, 0));
        String text = appearanceHeal.replace("{heal}", FORMAT.format(heal));
        Hologram hologram = API.createHologram(location, Collections.singletonList(text));
        Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), hologram::destroy, durationHeal);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (!enabledDamage || e.isCancelled()) return;

        double damage = e.getFinalDamage();
        if (damage <= 0 && !zeroDamageDamage) return;

        Entity entity = e.getEntity();
        if (!(entity instanceof LivingEntity) || entity instanceof ArmorStand) return;
        if (entity instanceof Player && !displayForPlayersDamage) return;
        if (!(entity instanceof Player) && !displayForMobsDamage) return;

        Location location = randomizeLocation(entity.getLocation().clone().add(0, 1 + heightOffsetDamage, 0));
        String appearance;
        Entity damager = (e instanceof EntityDamageByEntityEvent) ? ((EntityDamageByEntityEvent) e).getDamager() : null;
        if (damager instanceof Player && isCritical((Player) damager)) {
            appearance = this.criticalAppearanceDamage;
        } else {
            appearance = this.appearanceDamage;
        }
        String text = appearance.replace("{damage}", FORMAT.format(damage));
        Hologram hologram = API.createHologram(location, Collections.singletonList(text));
        Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), hologram::destroy, durationDamage);
    }

    /**
     * It takes a location, and returns a new location that is a random distance away from the original location
     *
     * @param location The location to randomize
     * @return A new location with randomized coordinates.
     */
    private Location randomizeLocation(Location location) {
        return location.add(
                RANDOM.nextFloat() - 0.5D,
                RANDOM.nextFloat() - 0.5D,
                RANDOM.nextFloat() - 0.5D
        );
    }

    /**
     * If the player is not falling, is on the ground, is in a vehicle, is blind, is on a ladder, or is on a vine, then
     * they are not critical
     *
     * @param player The player to check
     * @return A boolean value.
     */
    private boolean isCritical(Player player) {
        if (player == null) return false;
        if (player.getFallDistance() <= 0.0F ||
                player.isOnGround() ||
                player.isInsideVehicle() ||
                player.hasPotionEffect(PotionEffectType.BLINDNESS) ||
                player.getLocation().getBlock().getType() == Material.LADDER ||
                player.getLocation().getBlock().getType() == Material.VINE
        ) {
            return false;
        }
        try {
            // Slow Falling is not in all versions
            if (player.hasPotionEffect(PotionEffectType.getByName("SLOW_FALLING"))) {
                return false;
            }
        } catch (Exception ignored) {
        }
        return true;
    }

}

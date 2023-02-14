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

package eu.decentsoftware.holograms.utils;

import eu.decentsoftware.holograms.DecentHolograms;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

/**
 * Utility class for scheduling tasks.
 *
 * @author d0by
 */
@UtilityClass
@SuppressWarnings({"unused", "deprecation"})
public final class SchedulerUtil {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    public static void cancel(int id) {
        Bukkit.getScheduler().cancelTask(id);
    }

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, runnable);
    }

    public static void async(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, runnable, delay);
    }

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(PLUGIN, runnable);
    }

    public static void run(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(PLUGIN, runnable, delay);
    }

    public static int scheduleAsync(Runnable runnable, long interval) {
        return scheduleAsync(runnable, 0L, interval);
    }

    public static int scheduleAsync(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().scheduleAsyncRepeatingTask(PLUGIN, runnable, delay, interval);
    }

    public static int scheduleSync(Runnable runnable, long interval) {
        return scheduleSync(runnable, 0L, interval);
    }

    public static int scheduleSync(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(PLUGIN, runnable, delay, interval);
    }

}
